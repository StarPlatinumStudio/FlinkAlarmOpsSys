package com.ktvmi.flinkconfig.Common;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.context.annotation.Configuration;
//启动服务发现
@Configuration
@EnableNacosDiscovery(globalProperties = @NacosProperties(serverAddr = "192.168.97.209:8848"))
public class NacosConfiguration {
}
