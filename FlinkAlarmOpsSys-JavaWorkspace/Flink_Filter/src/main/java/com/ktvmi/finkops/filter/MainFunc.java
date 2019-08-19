package com.ktvmi.finkops.filter;
import com.ktvmi.finkops.filter.entity.Config;
import com.ktvmi.finkops.filter.source.KeyWordFlatMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import java.util.Properties;

public class MainFunc {
    public static void main(String args[])throws Exception{
        Config config=new Config();
        String serverAddr = args[0];
        String dataId = "FlinkJob."+args[1]+".properties";
        String group = "BaseService";
        String hostName=args[2];//writeToSocket
        int port=Integer.parseInt(args[3]);//writeToSocket port
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        try {
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            config= JSON.parseObject(content, Config.class);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
           e.printStackTrace();
        }
//        Config config=globalConfig.getConfig();
        //设置流处理环境：set up the streaming execution environment
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        //获取数据

        DataStream<String> stream=env
              .socketTextStream(config.getHostname(),new Long(config.getPort()).intValue())
                .flatMap(new KeyWordFlatMap(serverAddr,dataId,group));
       //stream.print();//.setParallelism(1);
      //  stream.addSink(new SocketClientSink("127.0.0.1",9001,new SimpleStringSchema(),0,true)).setParallelism(1);
      stream.writeToSocket(hostName,port,new SimpleStringSchema());
      //stream.print();
        env.execute("StreamExecutionEnvironment Execute");
    }
 }


