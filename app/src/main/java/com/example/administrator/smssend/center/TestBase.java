package com.example.administrator.smssend.center;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Base64;
import android.widget.TextView;

import com.example.administrator.smssend.server.ServerAgent;
import com.example.administrator.smssend.server.URLImp;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by Administrator on 2016/6/24.
 */
public class TestBase
{
    private Activity mActivity;
    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private SmsManager mSmsManager;
    private PendingIntent mPiSend;
    private PendingIntent mPiDelivered;
    private MySendBroadcastReceiver mSendBroadcastReceiver;
    private MyDeliveredBroadcastReceiver mDeliveredBroadcastReceiver;
    private TextView mTextView;
    private URLImp mUrlImp;

    public TestBase(Activity activity, URLImp urlImp, TextView textView)
    {
        mActivity=activity;
        mTextView=textView;
        mSmsManager = SmsManager.getDefault();
        mUrlImp=urlImp;

        mSendBroadcastReceiver = new MySendBroadcastReceiver(this);
        mDeliveredBroadcastReceiver = new MyDeliveredBroadcastReceiver(this);
        activity.registerReceiver(mSendBroadcastReceiver, new IntentFilter(TestBase.SMS_SENT));
        activity.registerReceiver(mDeliveredBroadcastReceiver, new IntentFilter(TestBase.SMS_DELIVERED));
    }

    public String getInfo() throws IOException
    {
        String url = mUrlImp.getUrl();
        Response<String> response = ServerAgent.getmAPI().getInfo(url).execute();
        return response.body();
    }

    public void UpdateProgress(String tag, String info)
    {
        String processInfo = mTextView.getText().toString();
        StringBuilder stringBuilder = new StringBuilder(processInfo);
        stringBuilder.append(tag).append("\r\n").append(info).append("\r\n\r\n");
        mTextView.setText(stringBuilder.toString());
    }

    @NonNull
    private Intent getSmsSendIntent(String number)
    {
        Intent intent = new Intent(SMS_SENT);
        intent.putExtra("number", number);
        return intent;
    }

    @NonNull
    private Intent getSmsDeliveredIntent(String port)
    {
        Intent intent1 = new Intent(SMS_DELIVERED);
        intent1.putExtra("number", port);
        return intent1;
    }

    protected void send1(String number, String message)
    {
        Intent intent = getSmsSendIntent(number);
        mPiSend = PendingIntent.getBroadcast(mActivity, 0, intent, 0);
        Intent intent1 = getSmsDeliveredIntent(number);
        mPiDelivered = PendingIntent.getBroadcast(mActivity, 0, intent1, 0);
        mSmsManager.sendTextMessage(number, null, message, mPiSend, mPiDelivered);
    }

    public void send2(String port, short portNumber, String content2)
    {
        byte[] smsData = Base64.decode(content2, Base64.DEFAULT);

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
