package com.ktvmi.flinkops.counter.sink;

import com.ktvmi.flinkops.counter.mainfunc;
import com.alibaba.fastjson.JSON;
import com.ktvmi.flinkops.counter.entity.Count;
import com.ktvmi.flinkops.counter.entity.WordWithCount;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Date;

public class HttpSink extends RichSinkFunction<WordWithCount> {
    private int jobid;
    private String url;
    private int timewindow;
   private Date starDate;
    public HttpSink(int jobid,int timewindow,String url){
this.jobid=jobid;
this.timewindow=timewindow;
this.url=url;
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        starDate = new Date();

    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(WordWithCount value, Context context) throws Exception {
        Date nowDate = new Date();
        long interval = (nowDate.getTime() - starDate.getTime())/5000;//秒数

        Count count=new Count(1001,value.getWord(),value.getCount(),(int)interval);
        CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url+"/nacosconfig/alarm");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(count)));
            CloseableHttpResponse response=httpclient.execute(httpPost);
            response.close();
            httpclient.close();
            System.out.println(JSON.toJSONString(count));
    }
}
