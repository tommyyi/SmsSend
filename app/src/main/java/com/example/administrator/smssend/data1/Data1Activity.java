package com.example.administrator.smssend.data1;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.smssend.R;
import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestImp;
import com.example.administrator.smssend.databinding.ActivityMainBinding;

public class Data1Activity extends AppCompatActivity
{
    public ActivityMainBinding mActivityMainBinding;
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
        mTestImp = new Data1Test(this, new Data1URL(this), mActivityMainBinding.processInfo);
        mTestImp.init();
    }

    public void test(View view)
    {
        mTestImp.test();
    }

    public void setState(boolean enabled)
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
}
