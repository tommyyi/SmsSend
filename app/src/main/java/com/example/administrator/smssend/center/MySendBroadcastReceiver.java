package com.example.administrator.smssend.center;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/6/24.
 */
public class MySendBroadcastReceiver extends android.content.BroadcastReceiver
{
    private final TestBase mTestBase;

    public MySendBroadcastReceiver(TestBase testBase)
    {
        mTestBase = testBase;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String tag = "已发送信息至:";
        String number = intent.getStringExtra(TestBase.NUMBER);
        mTestBase.UpdateProgress(tag, number);
    }
}
