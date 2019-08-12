package com.ktvmi.flinkconfig.Common;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.ktvmi.flinkconfig.EntityClass.AlarmRule;
import com.ktvmi.flinkconfig.EntityClass.Count;
import com.ktvmi.flinkconfig.EntityClass.CountsQueue;
import com.ktvmi.flinkconfig.EntityClass.Job;
import com.ktvmi.flinkconfig.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AlarmHelper {
    @NacosInjected
    private ConfigService configService;//注册默认服务
    HashMap<String, CountsQueue> countsQueueHashMap=new HashMap<>();
    HashMap<String,AlarmRule> alarmRuleHashMap=new HashMap<>();
    private int springTime=0;

    /**
     * 构造函数
     * 从Nacos获取持久AlarmRuleHashMap
     * 根据AlarmRuleHashMap.keySet()初始化CountsQueueHashMap
     */
    public AlarmHelper()throws NacosException {
    alarmRuleHashMap= JSON.parseObject(configService.getConfig("Flink.AlarmRules","BaseService",3000),alarmRuleHashMap.getClass());
        for (String akey:alarmRuleHashMap.keySet()) {
            CountsQueue countsQueue=new CountsQueue();
            countsQueueHashMap.put(akey,countsQueue);
        }
    }

    /**
     * 接收计数信息，调整30分钟长度的计数队列
     * 一旦FlinkTime大于SpringTime，递归(伪)调整全部队列的时间
     * @param count
     */
    public void getCount(Count count){
        String key=count.getJobId()+count.getContent();
        CountsQueue queue=countsQueueHashMap.get(key);
        queue.addTime();
        if (count.getFlinkTime()!=springTime){
            while (springTime<=count.getFlinkTime()){
                for (String k:countsQueueHashMap.keySet())//String key:countsQueueHashMap.keySet()
                {
                    CountsQueue countsQueue=countsQueueHashMap.get(key);
                    if (countsQueue.getQueueTime()<springTime){
                        countsQueue.addTime();
                        countsQueue.pushCount(0);
                        countsQueueHashMap.put(k,countsQueue);
                    }
                }
                springTime++;
            }
        }
        queue.pushCount(count.getCounts());//计入值
        countsQueueHashMap.put(key,queue);
        queue.isAlarm();
    }
}
