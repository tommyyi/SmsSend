package com.example.administrator.smssend;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.smssend.databinding.ActivityMainBinding;
import com.example.administrator.smssend.server.ServerAgent;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{

    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private ActivityMainBinding mActivityMainBinding;
    private SmsManager mSmsManager;
    private PendingIntent mPiSend;
    private PendingIntent mPiDelivered;
    private MySendBroadcastReceiver mSendBroadcastReceiver;
    private MyDeliveredBroadcastReceiver mDeliveredBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    public void test(View view)
    {
        boolean byManual = mActivityMainBinding.byManual.isChecked();
        if (byManual)
        {
            byManual();
        }
        else
        {
            byAuto();
        }
    }

    private void byManual()
    {
        send1(null);
        send2(null);
    }

    private void byAuto()
    {
        Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                try
                {
                    subscriber.onNext(getInfo(MainActivity.this.getApplicationContext()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                String processInfo = mActivityMainBinding.processInfo.getText().toString();
                StringBuilder stringBuilder=new StringBuilder(processInfo);
                stringBuilder.append("从服务器获取的信息:").append("\r\n").append(s).append("\r\n\r\n");
                mActivityMainBinding.processInfo.setText(stringBuilder.toString());

                fillUI(s);
                send1(null);
                send2(null);
            }
        });
    }

    private void fillUI(String s)
    {
        Container container = JSONObject.parseObject(s, Container.class);
        mActivityMainBinding.number1.setText(container.getInit_sms_number());
        mActivityMainBinding.content1.setText(container.getInit_sms());

        mActivityMainBinding.port.setText(container.getPort());
        mActivityMainBinding.portnumber.setText(container.getPortnumber());
        mActivityMainBinding.content2.setText(container.getCmd());
    }

    private static String getInfo(Context context) throws IOException
    {
        Response<String> response = ServerAgent.getmAPI().getInfo(URL.creator(context)).execute();
        return response.body();
    }

    private class MySendBroadcastReceiver extends android.content.BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String number = intent.getStringExtra("number");
            String processInfo = mActivityMainBinding.processInfo.getText().toString();
            StringBuilder stringBuilder=new StringBuilder(processInfo);
            stringBuilder.append("已发送信息至:").append(number).append("\r\n");
            mActivityMainBinding.processInfo.setText(stringBuilder.toString());
        }
    }

    private class MyDeliveredBroadcastReceiver extends android.content.BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String number = intent.getStringExtra("number");
            String processInfo = mActivityMainBinding.processInfo.getText().toString();
            StringBuilder stringBuilder=new StringBuilder(processInfo);
            stringBuilder.append("对方已收到:").append(number).append("\r\n");
            mActivityMainBinding.processInfo.setText(stringBuilder.toString());
        }
    }

    private void init()
    {
        mSmsManager = SmsManager.getDefault();
        mSendBroadcastReceiver = new MySendBroadcastReceiver();
        registerReceiver(mSendBroadcastReceiver, new IntentFilter(SMS_SENT));
        mDeliveredBroadcastReceiver = new MyDeliveredBroadcastReceiver();
        registerReceiver(mDeliveredBroadcastReceiver, new IntentFilter(SMS_DELIVERED));

        mActivityMainBinding.byManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    mActivityMainBinding.number1.setEnabled(true);
                    mActivityMainBinding.content1.setEnabled(true);
                    mActivityMainBinding.port.setEnabled(true);
                    mActivityMainBinding.portnumber.setEnabled(true);
                    mActivityMainBinding.content2.setEnabled(true);
                }
                else
                {
                    resetbymanual();
                }
            }
        });
        resetbymanual();
    }

    private void resetbymanual()
    {
        mActivityMainBinding.number1.setEnabled(false);
        mActivityMainBinding.content1.setEnabled(false);
        mActivityMainBinding.port.setEnabled(false);
        mActivityMainBinding.portnumber.setEnabled(false);
        mActivityMainBinding.content2.setEnabled(false);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliveredBroadcastReceiver);
    }

    public void send1(View view)
    {
        String number = mActivityMainBinding.number1.getText().toString();
        String message = mActivityMainBinding.content1.getText().toString();
        if(number.equals("")||message.equals(""))
            return;

        Intent intent = new Intent(SMS_SENT);
        intent.putExtra("number", number);
        mPiSend = PendingIntent.getBroadcast(this, 0, intent, 0);
        Intent intent1 = new Intent(SMS_DELIVERED);
        intent1.putExtra("number", number);
        mPiDelivered = PendingIntent.getBroadcast(this, 0, intent1, 0);
        mSmsManager.sendTextMessage(number, null, message, mPiSend, mPiDelivered);
    }

    public void send2(View view)
    {
        final long delay = Long.valueOf(mActivityMainBinding.delay.getText().toString());
        String processInfo = mActivityMainBinding.processInfo.getText().toString();
        StringBuilder stringBuilder=new StringBuilder(processInfo);
        stringBuilder.append("延时:").append(mActivityMainBinding.delay.getText().toString()).append("秒\r\n");
        mActivityMainBinding.processInfo.setText(stringBuilder.toString());

        Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                try
                {
                    Thread.sleep(delay*1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                subscriber.onNext("ok");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                byte[] smsData = Base64.decode(mActivityMainBinding.content2.getText().toString(), Base64.DEFAULT);
                String port = mActivityMainBinding.port.getText().toString();
                short portNumber = java.lang.Short.valueOf(mActivityMainBinding.portnumber.getText().toString());

                if(port.equals("")||mActivityMainBinding.content2.getText().toString().equals(""))
                    return;

                Intent intent = new Intent(SMS_SENT);
                intent.putExtra("number", port);
                mPiSend = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                Intent intent1 = new Intent(SMS_DELIVERED);
                intent1.putExtra("number", port);
                mPiDelivered = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, 0);
                mSmsManager.sendDataMessage(port, null, portNumber, smsData, mPiSend, mPiDelivered);
            }
        });
    }
}
