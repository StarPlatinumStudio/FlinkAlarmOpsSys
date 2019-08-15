package alpha.basicFJ.model.Source;

import alpha.basicFJ.model.EntityClass.Config;
import alpha.basicFJ.model.EntityClass.InputJsonBean;
import alpha.basicFJ.model.HelperClass.KeywordHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 自定义FlatMapFunction
 */
public class SourceFromSocket extends RichFlatMapFunction<String, String> {
    private List<String> keys;
    private String serverAddr;
    private String dataId;
    private String group;
    //private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        init();
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    public SourceFromSocket(String serverAddr, String dataId, String group) throws NacosException {
        this.serverAddr = serverAddr;
        this.dataId = dataId;
        this.group = group;
//        init();
    }

    private void setKeys(List<String> keys) {
        this.keys = keys;
    }

    private void init() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        configService.publishConfig("flag", group, "false");
        String content = configService.getConfig(dataId, group, 5000);
        Config config = JSON.parseObject(content, Config.class);
        setKeys(config.getKeys());
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                Config config = JSON.parseObject(configInfo, Config.class);
               // log.debug(">>>" + configInfo);
                setKeys(config.getKeys());
            }
            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }

    @Override
    public void flatMap(String in, Collector<String> stringCollector) throws Exception {
        InputJsonBean jsonBean;
        try {
            byte[] bytes = in.getBytes("utf-8");
            jsonBean = JSON.parseObject(bytes, InputJsonBean.class);
        } catch (Exception ex) {
            stringCollector.collect("JSON Str Parse Fail\n");
            return;
        }
        try {
            String responseStr = jsonBean.getResponse();
            KeywordHelper keywordHelper = new KeywordHelper();
           // log.debug(">>>this.keys.size:" + this.keys.size());
            if (jsonBean.getStatus() == 0) {
                String mathResult=keywordHelper.match(responseStr, this.keys);
                if (!mathResult.equals(""))
                    stringCollector.collect(mathResult+"\n");
               // else stringCollector.collect("keywords not math:" + responseStr+"\n");
            } else
                stringCollector.collect("Data Status Error\n");
        } catch (Exception ex) {
            stringCollector.collect(ex.toString()+"\n");
        }
    }
}

