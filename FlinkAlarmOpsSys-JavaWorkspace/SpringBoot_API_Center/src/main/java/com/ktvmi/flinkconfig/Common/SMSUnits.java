package com.ktvmi.flinkconfig.Common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SMSUnits {
    private static String readInputStream(InputStream is){
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int len=0;
            byte[] buffer=new byte[1024];
            while ((len=is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            is.close();
            baos.close();
            byte[] result=baos.toByteArray();
            String temp=new String(result);
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }
    public  String sendGet(String param) {
        String result = "";
        String urlstr="https://teresalanguagecenter.cn/home/SMSforAndroid"+"?tel="+param;
        BufferedReader in = null;
        int i=0;
        String str="";
        try {
            URL url=new URL(urlstr);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            int responseCode=conn.getResponseCode();
            if(responseCode==200){
                InputStream is=conn.getInputStream();
                String text=readInputStream(is);
                return text;
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}