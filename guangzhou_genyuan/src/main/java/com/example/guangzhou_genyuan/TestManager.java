package com.example.guangzhou_genyuan;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestManagerInterface;
import com.example.administrator.smssend.server.UrlInterface;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/24.
 */
public class TestManager extends TestBase implements TestManagerInterface
{
    private final MainActivity mActivity;

    public TestManager(MainActivity activity, UrlInterface urlInterface, TextView textView)
    {
        super(activity, urlInterface, textView);
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
        TestManager.super.UpdateProgress("imei:", imei);
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
        UpdateProgress("获取支付信息使用的URL：",getUrl(mActivity.mActivityMainBinding.ip.getText().toString()));

        Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                try
                {
                    subscriber.onNext(getChargeInfo(mActivity.mActivityMainBinding.ip.getText().toString()));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>()
        {
            @Override
            public void call(String chargeInfo)
            {
                String tag = "从服务器获取的信息:";
                UpdateProgress(tag, chargeInfo);

                fillUI(chargeInfo);
                byManual();
            }
        });
    }

    @Override
    public void fillUI(String s)
    {
        ServiceResponse serviceResponse = JSONObject.parseObject(s, ServiceResponse.class);
        mActivity.mActivityMainBinding.number1.setText(serviceResponse.getInit_sms_number());
        mActivity.mActivityMainBinding.content1.setText(serviceResponse.getInit_sms());

        mActivity.mActivityMainBinding.port.setText(serviceResponse.getPort());
        mActivity.mActivityMainBinding.portnumber.setText(serviceResponse.getPortnumber());
        mActivity.mActivityMainBinding.content2.setText(serviceResponse.getCmd());
    }

    @Override
    public void send1()
    {
        String number = mActivity.mActivityMainBinding.number1.getText().toString();
        String message = mActivity.mActivityMainBinding.content1.getText().toString();
        if (number.equals("") || message.equals(""))
        {
            TestManager.super.UpdateProgress("没有短信号码或内容，无法发送第1条短信", "");
            return;
        }

        sendTextMessage(number, message);
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
                    TestManager.super.UpdateProgress("没有短信号码或内容，无法发送第2条短信", "");
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

                byte[] smsData = Base64.decode(content2, Base64.DEFAULT);
                TestManager.this.sendDataMessage(port, portNumber, smsData);
            }
        });
    }
}
