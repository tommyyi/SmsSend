package com.example.guangzhou_genyuan.tocp;

import android.content.Context;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.example.administrator.smssend.server.UrlInterface;

/**
 * 构造用于获取指令的url，这里appFeeId和feenum对应一个2元的代码，如果您使用的是其它价格的代码，请替换
 */

public class URL2order
{
    private final String mImei;
    private final String mImsi;
    String cpid;
    String channelid;
    String appId;
    String appFeeId;
    String feenum;
    private String miccid;
    private String mOsModel;
    private String mNetInfo;

    public URL2order(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        mImei = telephonyManager.getDeviceId();
        mImsi = telephonyManager.getSubscriberId();

        cpid = "103";
        channelid = "3238";
        appId = "2138";
        appFeeId = "3194";
        feenum = "200";

        mOsModel= Build.BRAND;
        miccid = "89860090010671234669";
        //miccid = telephonyManager.getSimSerialNumber();

        WifiManager wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if(connectionInfo!=null)
            mNetInfo= "wifi";
        else
            mNetInfo="GPRS";
    }

    public String getUrl(String ip)
    {
        return "http://120.25.220.193:8099/send/getCommand.jsp?type=1&cpid=" + cpid + "&channelId=" + channelid + "&appId=" + appId + "&appFeeId=" + appFeeId + "&feenum=" + feenum + "&imei=" + mImei + "&imsi=" + mImsi + "&netInfo=" + mNetInfo + "&osInfo=4.1&osModel=" + mOsModel + "&iccid=" + miccid + "&clientIp=" + ip + "&extra=";
    }
}
