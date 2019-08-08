package com.ktvmi.flinkconfig.Controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import java.util.List;
import java.util.Properties;

import com.ktvmi.flinkconfig.EntityClass.AlarmConfig;
import com.ktvmi.flinkconfig.EntityClass.AlarmRule;
import com.ktvmi.flinkconfig.EntityClass.ResponseMsg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

@RestController
@RequestMapping({"/nacosconfig"})
public class NacosController {
    private  String config="";
    /**
     * 获取配置信息
     */
    @NacosValue(value = "${useLocalCache:false}",autoRefreshed = true)
    private boolean useLocalCache;//运行时可被动态更新了。

    @NacosInjected
    private ConfigService configService;//注册默认服务

    @GetMapping("/getalarm")
    public String getalarm(String id)throws NacosException{
        String content = configService.getConfig("FlinkJob."+id+".properties.alarm", "BaseService", 5000);
        System.out.println(id+"content"+content);
        return content;
    }
    @PostMapping("/alarm")
    public ResponseMsg setalarm(@Valid @RequestBody AlarmRule alarmRule)throws NacosException{
        try {
            configService.publishConfig(alarmConfig.getDataId(),alarmConfig.getGroup(),alarmConfig.getContent());
        }
        catch (NacosException ex){
            ResponseMsg badmsg = new ResponseMsg("Catch Exception"+ex.getMessage(), 233);
            return badmsg;
        }
        ResponseMsg goodmsg = new ResponseMsg("Success", 0);
        return goodmsg;
    }

    @RequestMapping(value = {"config/get"},method = {RequestMethod.GET})
    @ResponseBody
    public boolean get() {
        return this.useLocalCache;
    }

    @RequestMapping(value = {"config/publish"},method = {RequestMethod.GET})
    @ResponseBody
    public boolean publish() throws NacosException {
        configService.publishConfig("example2", "DEFAULT_GROUP", "9527");
        return true;
    }



    @NacosConfigListener(dataId = "example2")
    public void onMessage(String config){

        this.config=config;
        System.out.println("NacosConfigListener:"+this.config);
    }
    /**
     * 启动服务发现
     */
    @NacosInjected//依赖注入
    private NamingService namingService;//注入 Nacos 的 NamingService 实例
    @RequestMapping(
            value = {"discovery/get"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return this.namingService.getAllInstances(serviceName);
    }
}
