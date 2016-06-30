package com.example.administrator.smssend.center;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.widget.TextView;

import com.example.administrator.smssend.server.ServerAgent;
import com.example.administrator.smssend.server.URLImp;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by Administrator on 2016/6/24.
 */
public abstract class TestBase
{
    public static final String NUMBER = "number";
    private final Activity mActivity;
    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private final SmsManager mSmsManager;
    private PendingIntent mPiSend;
    private PendingIntent mPiDelivered;
    private final MySendBroadcastReceiver mSendBroadcastReceiver;
    private final MyDeliveredBroadcastReceiver mDeliveredBroadcastReceiver;
    private final TextView mTextView;
    private final URLImp mUrlImp;

    protected TestBase(Activity activity, URLImp urlImp, TextView textView)
    {
        mActivity=activity;
        mTextView=textView;
        mSmsManager = SmsManager.getDefault();
        mUrlImp=urlImp;

        mSendBroadcastReceiver = new MySendBroadcastReceiver(this);
        mDeliveredBroadcastReceiver = new MyDeliveredBroadcastReceiver(this);
        register();
    }

    public void register()
    {
        mActivity.registerReceiver(mSendBroadcastReceiver, new IntentFilter(TestBase.SMS_SENT));
        mActivity.registerReceiver(mDeliveredBroadcastReceiver, new IntentFilter(TestBase.SMS_DELIVERED));
    }

    protected String getChargeInfo(String ip) throws IOException
    {
        String url = mUrlImp.getUrl(ip);
        Response<String> response = ServerAgent.getmAPI().getInfo(url).execute();
        return response.body();
    }

    public String getUrl(String ip)
    {
        return mUrlImp.getUrl(ip);
    }

    public void UpdateProgress(String tag, String info)
    {
        String processInfo = mTextView.getText().toString();
        mTextView.setText(processInfo + tag + "\r\n" + info + "\r\n\r\n");
    }

    @NonNull
    private Intent getSmsSendIntent(String number)
    {
        Intent intent = new Intent(SMS_SENT);
        intent.putExtra(NUMBER, number);
        return intent;
    }

    @NonNull
    private Intent getSmsDeliveredIntent(String port)
    {
        Intent intent1 = new Intent(SMS_DELIVERED);
        intent1.putExtra(NUMBER, port);
        return intent1;
    }

    protected void sendTextMessage(String number, String message)
    {
        Intent intent = getSmsSendIntent(number);
        mPiSend = PendingIntent.getBroadcast(mActivity, 0, intent, 0);
        Intent intent1 = getSmsDeliveredIntent(number);
        mPiDelivered = PendingIntent.getBroadcast(mActivity, 0, intent1, 0);
        mSmsManager.sendTextMessage(number, null, message, mPiSend, mPiDelivered);
    }

    protected void sendDataMessage(String port, short portNumber, byte[] smsData)
    {
        Intent intent = getSmsSendIntent(port);
        mPiSend = PendingIntent.getBroadcast(mActivity, 0, intent, 0);
        Intent intent1 = getSmsDeliveredIntent(port);
        mPiDelivered = PendingIntent.getBroadcast(mActivity, 0, intent1, 0);
        mSmsManager.sendDataMessage(port, null, portNumber, smsData, mPiSend, mPiDelivered);
    }

    public void unregister()
    {
        mActivity.unregisterReceiver(mSendBroadcastReceiver);
        mActivity.unregisterReceiver(mDeliveredBroadcastReceiver);
    }
}
