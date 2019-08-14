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
//            JSONObject  jsonObject = JSONObject.parseObject(content);
//            Map<String,Object> map = (Map<String,Object>)jsonObject;
//            for (int i = 0; i < jsonObject.size(); i++) {
//              AlarmRule alarmRule=new AlarmRule();
//                Object[] obj = (Object[])jsonObject.get(i);
//                alarmRule.setJobid(Integer.parseInt(obj[0].toString()));
//                alarmRule.setCounts(Integer.parseInt(obj[1].toString()));
//                alarmRule.setTimewindow(Integer.parseInt(obj[2].toString()));
//                alarmRule.setRulecontent(obj[3].toString());
//                alarmRule.setRuleid(Integer.parseInt(obj[4].toString()));
//                alarmRuleHashMap.put(alarmRule.getJobid()+alarmRule.getRulecontent(),alarmRule);
//            }

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
                    CountsQueue countsQueue=countsQueueHashMap.get(key);
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
            System.out.println("“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==");
            System.out.println("“警告:======已到达阀值“" + JSON.toJSONString(alarmRuleHashMap));
            System.out.println("“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==“警告、”==");
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
