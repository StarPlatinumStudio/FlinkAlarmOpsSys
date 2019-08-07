package alpha.basicFJ.model.HelperClass;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpHelper {
    public String StrGet(String apiUrl) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String jstr="";
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(apiUrl);
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
                    jstr = EntityUtils.toString(entity);
                    System.out.println("Response content: " + jstr);
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
        return jstr;
    }
}
