package com.ktvmi.flinkconfig.Common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.ktvmi.flinkconfig.EntityClass.*;
import com.ktvmi.flinkconfig.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
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
            alarmRuleHashMap=JSONObject.parseObject(content,alarmRuleHashMap.getClass());
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
            while (springTime<count.getFlinkTime()){
                for (String k:countsQueueHashMap.keySet())//String key:countsQueueHashMap.keySet()
                {
                    CountsQueue countsQueue=countsQueueHashMap.get(k);
                    if (countsQueue.getQueueTime()<springTime){
                        countsQueue.addTime();
                        countsQueue.add(0);
                        countsQueueHashMap.put(k,countsQueue);
                    }
                }
                springTime++;
            }
        }
        queue.add(count.getCounts());//计入值
        countsQueueHashMap.put(key,queue);
        AlarmRule alarmRule=JSONObject.parseObject(JSON.toJSONString(alarmRuleHashMap.get(key)),AlarmRule.class);
        int timeWindow=alarmRule.getTimewindow();
        int ruleCounts=alarmRule.getCounts();
        if(queue.isAlarm(timeWindow,ruleCounts)) {
            SMSUnits smsUnits=new SMSUnits();
            System.out.println("警报规则{"+alarmRuleHashMap.get(key).getRulecontent()+"}目前发生频率超过"+timeWindow+"分钟"+ruleCounts+"次，已经达到预警阀值。报警短信已发送至号码为18250156433的手机，请留意。PS:一小时内警报最多发送五次。\n短信编号：："+smsUnits.sendGet("18250156433"));
        }
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
        String key=alarmRule.getJobid()+alarmRule.getRulecontent();
        alarmRuleHashMap.put(key,alarmRule);
        countsQueueHashMap.put(key,new CountsQueue());
       return configPublich(alarmRuleHashMap);
    }
    public ResponseMsg remove(AlarmRule alarmRule){
        String key=alarmRule.getJobid()+alarmRule.getRulecontent();
        alarmRuleHashMap.remove(key);
        countsQueueHashMap.remove(key);
        return configPublich(alarmRuleHashMap);
    }
    public ResponseMsg update(AlarmRuleMap alarmRule){
        String oldKey=alarmRule.getJobid()+alarmRule.getOldrule();
        String newKey=alarmRule.getJobid()+alarmRule.getRulecontent();
        AlarmRule oldRule=JSONObject.parseObject(JSON.toJSONString(alarmRuleHashMap.remove(oldKey)),AlarmRule.class);//DEBUG:com.alibaba.fastjson.JSONObject cannot be cast to com.ktvmi.flinkconfig.EntityClass.AlarmRule

        AlarmRule newRule=new AlarmRule(alarmRule.getJobid(),alarmRule.getRulecontent(),oldRule.getTimewindow(),oldRule.getCounts(),alarmRule.getNewruleid());
        alarmRuleHashMap.put(newKey,newRule);

        countsQueueHashMap.remove(oldKey);
        countsQueueHashMap.put(newKey,new CountsQueue());
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
