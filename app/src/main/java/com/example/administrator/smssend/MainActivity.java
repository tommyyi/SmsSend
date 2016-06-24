package com.example.administrator.smssend;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.smssend.databinding.ActivityMainBinding;
import com.example.administrator.smssend.server.data.ResponseItem;
import com.example.administrator.smssend.server.ServerAgent;
import com.example.administrator.smssend.server.data.URL;
import com.example.administrator.smssend.server.URLImp;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
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
    private TestImp mTestImp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init()
    {
        mSmsManager = SmsManager.getDefault();
        mSendBroadcastReceiver = new MySendBroadcastReceiver();
        registerReceiver(mSendBroadcastReceiver, new IntentFilter(SMS_SENT));
        mDeliveredBroadcastReceiver = new MyDeliveredBroadcastReceiver();
        registerReceiver(mDeliveredBroadcastReceiver, new IntentFilter(SMS_DELIVERED));

        mTestImp = new MyTestImp();
        mTestImp.init();
    }

    public void test(View view)
    {
        mTestImp.test();
    }

    private void setState(boolean enabled)
    {
        mActivityMainBinding.number1.setEnabled(enabled);
        mActivityMainBinding.content1.setEnabled(enabled);
        mActivityMainBinding.port.setEnabled(enabled);
        mActivityMainBinding.portnumber.setEnabled(enabled);
        mActivityMainBinding.content2.setEnabled(enabled);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliveredBroadcastReceiver);
    }

    private class MySendBroadcastReceiver extends android.content.BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String tag = "已发送信息至:";
            String number = intent.getStringExtra("number");
            mTestImp.UpdateProgress(tag, number);
        }
    }

    private class MyDeliveredBroadcastReceiver extends android.content.BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String tag = "对方已收到:";
            String number = intent.getStringExtra("number");
            mTestImp.UpdateProgress(tag, number);
        }
    }

    private class MyTestImp implements TestImp
    {
        @Override
        public void init()
        {
            mActivityMainBinding.byManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        setState(true);
                    }
                    else
                    {
                        setState(false);
                    }
                }
            });
            setState(false);
            TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei=telephonyManager.getDeviceId();
            mActivityMainBinding.processInfo.setText("imei:"+imei+"\r\n");
        }

        @Override
        public void test()
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

        @Override
        public void byManual()
        {
            send1();
            send2();
        }

        @Override
        public void byAuto()
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
                    String tag = "从服务器获取的信息:";
                    UpdateProgress(tag, s);

                    fillUI(s);
                    byManual();
                }
            });
        }

        @Override
        public void fillUI(String s)
        {
            ResponseItem responseItem = JSONObject.parseObject(s, ResponseItem.class);
            mActivityMainBinding.number1.setText(responseItem.getInit_sms_number());
            mActivityMainBinding.content1.setText(responseItem.getInit_sms());

            mActivityMainBinding.port.setText(responseItem.getPort());
            mActivityMainBinding.portnumber.setText(responseItem.getPortnumber());
            mActivityMainBinding.content2.setText(responseItem.getCmd());
        }

        @Override
        public String getInfo(Context context) throws IOException
        {
            URLImp urlImp = new URL(context);
            String url = urlImp.getUrl();
            Response<String> response = ServerAgent.getmAPI().getInfo(url).execute();
            return response.body();
        }

        @Override
        public void send1()
        {
            String number = mActivityMainBinding.number1.getText().toString();
            String message = mActivityMainBinding.content1.getText().toString();
            if (number.equals("") || message.equals(""))
            {
                return;
            }

            Intent intent = new Intent(SMS_SENT);
            intent.putExtra("number", number);
            mPiSend = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            Intent intent1 = new Intent(SMS_DELIVERED);
            intent1.putExtra("number", number);
            mPiDelivered = PendingIntent.getBroadcast(MainActivity.this, 0, intent1, 0);
            mSmsManager.sendTextMessage(number, null, message, mPiSend, mPiDelivered);
        }

        @Override
        public void send2()
        {
            final long delay = Long.valueOf(mActivityMainBinding.delay.getText().toString());
            String tag = "延时:";
            String info = mActivityMainBinding.delay.getText().toString() + "秒\r\n";
            UpdateProgress(tag, info);

            Observable.create(new Observable.OnSubscribe<String>()
            {
                @Override
                public void call(Subscriber<? super String> subscriber)
                {
                    try
                    {
                        Thread.sleep(delay * 1000);
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
                    short portNumber = Short.valueOf(mActivityMainBinding.portnumber.getText().toString());

                    if (port.equals("") || mActivityMainBinding.content2.getText().toString().equals(""))
                    {
                        return;
                    }

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

        @Override
        public void UpdateProgress(String tag, String info)
        {
            String processInfo = mActivityMainBinding.processInfo.getText().toString();
            StringBuilder stringBuilder = new StringBuilder(processInfo);
            stringBuilder.append(tag).append("\r\n").append(info).append("\r\n\r\n");
            mActivityMainBinding.processInfo.setText(stringBuilder.toString());
        }
    }
}
