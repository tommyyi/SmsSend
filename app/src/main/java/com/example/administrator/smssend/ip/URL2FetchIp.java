package com.example.administrator.smssend.ip;

import com.example.administrator.smssend.server.ServerAgent;
import com.example.administrator.smssend.server.URLImp;

import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/4.
 */
public class URL2FetchIp implements URLImp
{
    public static final String TAG_1 = "您现在的 IP：<code>";
    public static final String TAG_2 = "</code>";

    @Override
    public String getUrl(String ip)
    {
        return "http://www.ip.cn/";
    }

    public String getIp() throws Exception
    {
        Response<String> response = ServerAgent.getAPI().getInfo(new URL2FetchIp().getUrl(null)).execute();
        String content = response.body();

        return content.split(TAG_1)[1].split(TAG_2)[0];
    }
}
