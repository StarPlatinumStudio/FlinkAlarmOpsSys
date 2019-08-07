package FirstFlink;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class HttpHelper {
    public Config ConfigGet() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Config config = new Config();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet("http://localhost:5000/api/Config");
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);

            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                   // System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    String jstr = EntityUtils.toString(entity);
                    System.out.println("Response content: " + jstr);
                    config = JSON.parseObject(jstr, Config.class);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }
}

    //解析
//    public void test1() {
//        // 对象嵌套数组嵌套对象
//        String json1 = "{'id':1,'name':'JAVAEE-1703','stus':[{'id':101,'name':'刘铭','age':16}]}";
//        // 数组
//        String json2 = "['北京','天津','杭州']";
//        //1、
//        //静态方法
//        Grade grade= JSON.parseObject(json1, Grade.class);
//        System.out.println(grade);
//        //2、
//        List<String> list=JSON.parseArray(json2, String.class);
//        System.out.println(list);
//    }
//    //生成
//    public void test2(){
//        ArrayList<Student> list=new ArrayList<>();
//        for(int i=1;i<3;i++){
//            list.add(new Student(101+i, "码子", 20+i));
//        }
//        Grade grade=new Grade(100001,"张三", list);
//        String json=JSON.toJSONString(grade);
//        System.out.println(json);
//    }
