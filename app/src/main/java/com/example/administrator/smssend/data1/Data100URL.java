package com.example.administrator.smssend.data1;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.administrator.smssend.server.URLImp;

/**
 * Created by Administrator on 2016/6/23.
 */

public class Data100URL extends Data1URL
{
    public Data100URL(Context context)
    {
        super(context);
        cpid = "103";
        channelid = "3238";
        appId = "2138";
        appFeeId = "3194";
        feenum = "200";
    }
}
