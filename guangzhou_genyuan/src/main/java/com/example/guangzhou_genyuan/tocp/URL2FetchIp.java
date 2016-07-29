package com.example.guangzhou_genyuan.tocp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 用于获取当前手机所在网络的网关的IP地址
 */
public class URL2FetchIp
{
    public static final String TAG_1 = "您现在的 IP：<code>";
    public static final String TAG_2 = "</code>";
    private static final String IP_SERVER = "http://www.ip.cn/";

    public String getIp() throws Exception
    {
        URL url=new URL(IP_SERVER);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        InputStream inputStream=urlConnection.getInputStream();
        Reader reader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(reader);
        StringBuilder stringBuilder=new StringBuilder();
        while (true)
        {
            String temp=bufferedReader.readLine();
            if(temp==null)
                break;
            stringBuilder.append(temp);
        }

        bufferedReader.close();
        reader.close();
        inputStream.close();
        return stringBuilder.toString().split(TAG_1)[1].split(TAG_2)[0];
    }
}
