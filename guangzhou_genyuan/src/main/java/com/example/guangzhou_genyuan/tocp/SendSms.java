package com.example.guangzhou_genyuan.tocp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * 发送短信，并监听是否发送成功
 */
public class SendSms
{
    private static final String PROCESS = "PROCESS";
    private static final String SEND_OK = "send ok";
    private static final String DELIVER_OK = "deliver ok";
    private final SmsManager mSmsManager;
    private PendingIntent mPendingIntentSendOk;
    private PendingIntent mPendingIntentDeliverOk;
    private Context mContext;

    /**
     * @return获取广播监听的次数
     */
    public int getCount()
    {
        return mCount;
    }

    /**
     * 增加广播监听的次数
     */
    public void increaseCount()
    {
        mCount++;
    }

    /**
     * 广播监听的次数
     */
    private int mCount=0;

    /**
     * 监听短信的发送是否成功
     */
    public static class SendOkReceiver extends BroadcastReceiver
    {
        private int mCount;

        public SendOkReceiver(int count)
        {
            mCount = count;
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            mCount--;
            if (mCount == 0)
            {
                context.unregisterReceiver(this);
            }
            Log.e(PROCESS, SEND_OK);
        }
    }

    /**
     * 监听对方是否收到短信
     */
    public static class DeliverOkReceiver extends BroadcastReceiver
    {
        private int mCount;

        public DeliverOkReceiver(int count)
        {
            mCount = count;
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            mCount--;
            if (mCount == 0)
            {
                context.unregisterReceiver(this);
            }
            Log.e(PROCESS, DELIVER_OK);
        }
    }

    public SendSms(Context context)
    {
        mContext = context;
        mSmsManager = SmsManager.getDefault();
    }

    public void register()
    {
        SendOkReceiver sendOkReceiver = new SendOkReceiver(mCount);
        IntentFilter filter = new IntentFilter(mContext.getPackageName() + SEND_OK);
        mContext.registerReceiver(sendOkReceiver, filter);

        DeliverOkReceiver deliverOkReceiver = new DeliverOkReceiver(mCount);
        filter = new IntentFilter(mContext.getPackageName() + DELIVER_OK);
        mContext.registerReceiver(deliverOkReceiver, filter);
    }

    protected void sendTextMessage(String number, String message)
    {
        int requestCode = 0;
        Intent intent = new Intent(mContext.getPackageName() + SEND_OK);
        mPendingIntentSendOk = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        intent = new Intent(mContext.getPackageName() + DELIVER_OK);
        mPendingIntentDeliverOk = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        mSmsManager.sendTextMessage(number, null, message, mPendingIntentSendOk, mPendingIntentDeliverOk);
    }

    protected void sendDataMessage(String port, short portNumber, byte[] smsData)
    {
        int requestCode = 0;
        Intent intent = new Intent(mContext.getPackageName() + SEND_OK);
        mPendingIntentSendOk = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        intent = new Intent(mContext.getPackageName() + DELIVER_OK);
        mPendingIntentDeliverOk = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        mSmsManager.sendDataMessage(port, null, portNumber, smsData, mPendingIntentSendOk, mPendingIntentDeliverOk);
    }
}
