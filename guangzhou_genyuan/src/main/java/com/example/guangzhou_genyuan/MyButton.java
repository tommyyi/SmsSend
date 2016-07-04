package com.example.guangzhou_genyuan;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Administrator on 2016/7/1.
 */
public class MyButton extends Button
{
    private Handler mHandler;
    private Runnable mRunnable;
    private float mDegree=30;
    private float mCount=0;

    public MyButton(Context context)
    {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mHandler = new Handler();
        mRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                invalidate();
            }
        };
    }

    @Override
    public void draw(Canvas canvas)
    {
        mDegree= (float) (30*Math.sin(mCount++));
        canvas.save();
        canvas.rotate(mDegree,getWidth()/2,getHeight()/2);
        super.draw(canvas);
        canvas.restore();
        mHandler.postDelayed(mRunnable,50);
    }
}
