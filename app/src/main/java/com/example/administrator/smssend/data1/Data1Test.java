package com.example.administrator.smssend.data1;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestImp;
import com.example.administrator.smssend.server.URLImp;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/24.
 */
public class Data1Test extends TestBase implements TestImp
{
    private Data1Activity mActivity;

    public Data1Test(Data1Activity activity, URLImp urlImp, TextView textView)
    {
        super(activity, urlImp, textView);
        mActivity = activity;
    }

    @Override
    public void init()
    {
        mActivity.mActivityMainBinding.byManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    mActivity.setState(true);
                }
                else
                {
                    mActivity.setState(false);
                }
            }
        });
        mActivity.setState(false);
        TelephonyManager telephonyManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        Data1Test.super.UpdateProgress("imei:", imei);
    }

    @Override
    public void test()
    {
        boolean byManual = mActivity.mActivityMainBinding.byManual.isChecked();
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
        Data1ResponseItem data1ResponseItem = JSONObject.parseObject(s, Data1ResponseItem.class);
        mActivity.mActivityMainBinding.number1.setText(data1ResponseItem.getInit_sms_number());
        mActivity.mActivityMainBinding.content1.setText(data1ResponseItem.getInit_sms());

        mActivity.mActivityMainBinding.port.setText(data1ResponseItem.getPort());
        mActivity.mActivityMainBinding.portnumber.setText(data1ResponseItem.getPortnumber());
        mActivity.mActivityMainBinding.content2.setText(data1ResponseItem.getCmd());
    }

    @Override
    public void send1()
    {
        String number = mActivity.mActivityMainBinding.number1.getText().toString();
        String message = mActivity.mActivityMainBinding.content1.getText().toString();
        if (number.equals("") || message.equals(""))
        {
            Data1Test.super.UpdateProgress("没有短信号码或内容，无法发送第1条短信", "");
            return;
        }

        send1(number, message);
    }

    @Override
    public void send2()
    {
        final long delay = Long.valueOf(mActivity.mActivityMainBinding.delay.getText().toString());
        String tag = "延时:";
        String info = mActivity.mActivityMainBinding.delay.getText().toString() + "秒\r\n";
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
                String content2 = mActivity.mActivityMainBinding.content2.getText().toString();
                String port = mActivity.mActivityMainBinding.port.getText().toString();
                String portNumberStr = mActivity.mActivityMainBinding.portnumber.getText().toString();
                if (port.equals("") || content2.equals(""))
                {
                    Data1Test.super.UpdateProgress("没有短信号码或内容，无法发送第2条短信", "");
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

                Data1Test.this.send2(port, portNumber, content2);
            }
        });
    }
}