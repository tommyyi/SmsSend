package com.example.administrator.smssend;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestImp;
import com.example.administrator.smssend.databinding.ActivityMainBinding;
import com.example.administrator.smssend.server.URLImp;
import com.example.administrator.smssend.server1.FirstURL;
import com.example.administrator.smssend.server1.ResponseItem;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding mActivityMainBinding;
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
        mTestImp = new MyTest(this, new FirstURL(this), mActivityMainBinding.processInfo);
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
        ((TestBase) mTestImp).unregister();
    }

    private class MyTest extends TestBase implements TestImp
    {
        MyTest(Activity activity, URLImp urlImp, TextView textView)
        {
            super(activity, urlImp, textView);
        }

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
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            MyTest.super.UpdateProgress("imei:",imei);
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
                        subscriber.onNext(getInfo());
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
        public void send1()
        {
            String number = mActivityMainBinding.number1.getText().toString();
            String message = mActivityMainBinding.content1.getText().toString();
            if (number.equals("") || message.equals(""))
            {
                MyTest.super.UpdateProgress("没有短信号码或内容，无法发送第1条短信","");
                return;
            }

            send1(number, message);
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
                    String content2 = mActivityMainBinding.content2.getText().toString();
                    String port = mActivityMainBinding.port.getText().toString();
                    String portNumberStr = mActivityMainBinding.portnumber.getText().toString();
                    if (port.equals("") || content2.equals(""))
                    {
                        MyTest.super.UpdateProgress("没有短信号码或内容，无法发送第2条短信","");
                        return;
                    }
                    short portNumber;
                    if (portNumberStr.equals(""))
                    {
                        portNumber = 0;
                    }
                    else
                    {
                        portNumber = Short.valueOf(portNumberStr);
                    }

                    MyTest.this.send2(port, portNumber, content2);
                }
            });
        }
    }
}
