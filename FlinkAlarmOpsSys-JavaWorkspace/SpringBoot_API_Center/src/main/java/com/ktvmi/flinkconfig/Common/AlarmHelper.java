package com.ktvmi.flinkconfig.Common;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.ktvmi.flinkconfig.EntityClass.*;
import com.ktvmi.flinkconfig.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AlarmHelper {

    public HashMap<String, CountsQueue> countsQueueHashMap=new HashMap<>();
    public HashMap<String,AlarmRule> alarmRuleHashMap=new HashMap<>();
    private int springTime=0;
    private ConfigService configService;
    /**
     * 构造函数
     * 从Nacos获取持久AlarmRuleHashMap
     * 根据AlarmRuleHashMap.keySet()初始化CountsQueueHashMap
     */
    public AlarmHelper() {
        try {
            String serverAddr = "192.168.1.130:8848";
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            configService = NacosFactory.createConfigService(properties);
            String dataId = "Flink.AlarmRuleHashMap";
            String group = "BaseService";
            String content = configService.getConfig(dataId, group, 5000);
            alarmRuleHashMap=JSON.parseObject(content,alarmRuleHashMap.getClass());
        }
   catch (NacosException e){
            e.printStackTrace();
   }
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
    private ResponseMsg configPublich(HashMap<String,AlarmRule> alarmRuleHashMap){
        String maptoStr=JSON.toJSONString(alarmRuleHashMap);
        try {
            configService.publishConfig("Flink.AlarmRuleHashMap","BaseService",maptoStr);
        }
        catch (NacosException e){
            e.printStackTrace();
            ResponseMsg badmsg = new ResponseMsg("Catch Exception"+e.getMessage(), 233);
            return badmsg;
        }
        ResponseMsg goodmsg = new ResponseMsg("Delete Success", 0);
        return goodmsg;
    }
    public ResponseMsg publish(AlarmRule alarmRule){
        alarmRuleHashMap.put(alarmRule.getJobid()+alarmRule.getRulecontent(),alarmRule);
       return configPublich(alarmRuleHashMap);
    }
    public ResponseMsg remove(AlarmRule alarmRule){
        alarmRuleHashMap.remove(alarmRule.getJobid()+alarmRule.getRulecontent());
        return configPublich(alarmRuleHashMap);
    }
    public ResponseMsg update(AlarmRuleMap alarmRule){
        AlarmRule oldRule=alarmRuleHashMap.remove(alarmRule.getJobid()+alarmRule.getOldrule());
        AlarmRule newRule=new AlarmRule(alarmRule.getJobid(),alarmRule.getRulecontent(),oldRule.getTimewindow(),oldRule.getCounts(),alarmRule.getNewruleid());
        alarmRuleHashMap.put(alarmRule.getJobid()+alarmRule.getRulecontent(),newRule);
        return configPublich(alarmRuleHashMap);
    }
//    String oldAlarmId="FlinkJob."+alarmRuleMap.getJobid()+"."+alarmRuleMap.getOldruleid();
//    String config=configService.getConfig(oldAlarmId,"AlarmRules",3000);//获取时间窗口和计数值
//    AlarmRule alarmRule=JSON.parseObject(config,AlarmRule.class);//实例化旧数据
//            alarmRule.setRuleid(alarmRuleMap.getNewruleid());//新数据覆盖
//            alarmRule.setRulecontent(alarmRuleMap.getRulecontent());
//            configService.publishConfig("FlinkJob."+alarmRule.getJobid()+"."+alarmRule.getRuleid(),"AlarmRules", JSON.toJSONString(alarmRule));//新发布
//            configService.removeConfig(oldAlarmId,"AlarmRules");//旧的删除
}
