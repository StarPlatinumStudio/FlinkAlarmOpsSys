package com.ktvmi.flinkconfig.Controller;


import com.alibaba.fastjson.JSON;
import com.ktvmi.flinkconfig.EntityClass.AlarmRule;
import com.ktvmi.flinkconfig.EntityClass.Greeting;
import com.ktvmi.flinkconfig.EntityClass.WordWithCount;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HomeController {
    private static final String template="Hello,%s!";
    private final AtomicLong counter=new AtomicLong();
@RequestMapping("/greeting")//确保HTTP请求/greeting被映射到greeting()方法
////@RequestParam将查询字符串参数的值绑定name到方法的name参数中greeting()。如果name请求中不存在该参数，defaultValue则使用“World”。
public Greeting greeting(@RequestParam(value = "name",defaultValue = "World") String name){
    return new Greeting(counter.incrementAndGet(),
            String.format(template,name));
}

}
