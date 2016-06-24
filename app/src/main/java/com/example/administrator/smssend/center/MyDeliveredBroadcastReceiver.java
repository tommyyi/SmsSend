package com.example.administrator.smssend.center;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/24.
 */
public class MyDeliveredBroadcastReceiver extends android.content.BroadcastReceiver
{
    private TestBase mTestBase;

    public MyDeliveredBroadcastReceiver(TestBase testBase)
    {
        mTestBase = testBase;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String tag = "对方已收到:";
        String number = intent.getStringExtra("number");
        mTestBase.UpdateProgress(tag, number);
    }
}
