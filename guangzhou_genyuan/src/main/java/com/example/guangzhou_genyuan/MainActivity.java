package com.example.guangzhou_genyuan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.smssend.center.TestBase;
import com.example.administrator.smssend.center.TestManagerInterface;
import com.example.guangzhou_genyuan.databinding.ActivityMainBinding;
import com.example.guangzhou_genyuan.tocp.Operation;
import com.example.guangzhou_genyuan.tocp.URL2FetchIp;

public class MainActivity extends AppCompatActivity
{
    public ActivityMainBinding mActivityMainBinding;
    private TestManagerInterface mTestManagerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mTestManagerInterface = new TestManager(this, new URL(this), mActivityMainBinding.processInfo);
        mTestManagerInterface.init();

        ((TestBase) mTestManagerInterface).register();
    }

    private void animateBg()
    {
        AnimationDrawable animationDrawable=(AnimationDrawable)mActivityMainBinding.send2.getBackground();
        animationDrawable.start();
    }

    public void test(View view)
    {
        mTestManagerInterface.test();
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
        ((TestBase) mTestManagerInterface).unregister();
    }

    public void rotateAnimRun(final View view)
    {
        ObjectAnimator anim = ObjectAnimator//
                                            .ofFloat(view, "zhy", 1.0F,  0.0F)//
                                            .setDuration(5000);//
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }

    public void verticalRun(final View view)
    {
        final int height = 100;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
//        animator.setTarget(view);
        animator.setDuration(5000).start();
        //		animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                view.setRotation((float)animation.getAnimatedValue()*360);
            }
        });
    }
}
