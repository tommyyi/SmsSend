package com.example.guangzhou_genyuan;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestManagerImp;
import com.example.guangzhou_genyuan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    public ActivityMainBinding mActivityMainBinding;
    private TestManagerImp mTestImp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mTestImp = new TestManager(this, new URL(this), mActivityMainBinding.processInfo);
        mTestImp.init();

        ((TestBase)mTestImp).register();
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
