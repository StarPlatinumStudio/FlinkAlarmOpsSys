package Sink;

import Entity.*;
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
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class HttpSink extends RichSinkFunction<WordWithCount> {
    private int jobid;
    private int timewindow;
    private int flinktime=0;
    public HttpSink(int jobid,int timewindow){
this.jobid=jobid;
this.timewindow=timewindow;
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        TimerTask myTask=new TimerTask()
        {
            @Override
            public void run()
            {
              flinktime++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(myTask,5000);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(WordWithCount value, Context context) throws Exception {
        Count count=new Count(1001,value.getWord(),value.getCount(),flinktime);
        CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/nacosconfig/alarm");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(count)));
            CloseableHttpResponse response=httpclient.execute(httpPost);
            response.close();
            httpclient.close();
            System.out.println(JSON.toJSONString(count));
    }
}
