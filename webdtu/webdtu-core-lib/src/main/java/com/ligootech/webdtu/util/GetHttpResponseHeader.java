package com.ligootech.webdtu.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class GetHttpResponseHeader {

    public static void main (String[] arges) {

        try {
            String url = "http://127.0.0.1:8080/fileOperate/downloadById?id=2016032316540514438649070640&device_code=BC52B";
            //URL testUrl = new URL("http://a.tgbus.com/download/33747/1");
            URL testUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) testUrl.openConnection();
            conn.setFollowRedirects(true);
            int ret = conn.getResponseCode();
            String fn=URLDecoder.decode(conn.getURL().toString(),"UTF-8");
            System.out.println("------------"+ret+fn);
            System.out.println("------------"+ret+fn.substring(fn.lastIndexOf("/")+1));
            InputStream in=conn.getInputStream();
            InputStreamReader dis=new InputStreamReader(in);
            java.io.BufferedReader bf=new java.io.BufferedReader(dis);
            //String r =bf.readLine();
            String r ;
            int k=0;
            while((r=bf.readLine()) !=null)
            {
                System.out.println(r);
                //r=bf.readLine();
                k++;
                if(k>15)break;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}