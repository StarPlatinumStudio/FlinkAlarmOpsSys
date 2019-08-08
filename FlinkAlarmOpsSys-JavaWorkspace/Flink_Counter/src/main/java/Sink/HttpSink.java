package Sink;

import Entity.AlarmRule;
import Entity.WordWithCount;
import Source.tumbMain;
import com.alibaba.fastjson.JSON;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

public class HttpSink extends RichSinkFunction<WordWithCount> {
    private int jobid;
    private int timewindow;
    public HttpSink(int jobid,int timewindow){
this.jobid=jobid;
this.timewindow=timewindow;
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(WordWithCount value, Context context) throws Exception {
        AlarmRule alarmRule=new AlarmRule(jobid,value.getWord(),timewindow,value.getCount());

        CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://192.168.1.137:8080/alarm");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(alarmRule)));
            CloseableHttpResponse response=httpclient.execute(httpPost);
            response.close();
            httpclient.close();
    }
}
