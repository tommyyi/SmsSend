package com.example.administrator.smssend.center;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/24.
 */
public class MySendBroadcastReceiver extends android.content.BroadcastReceiver
{
    private TestBase mTestBase;

    public MySendBroadcastReceiver(TestBase testBase)
    {
        mTestBase = testBase;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String tag = "已发送信息至:";
        String number = intent.getStringExtra("number");
        mTestBase.UpdateProgress(tag, number);
    }
}
