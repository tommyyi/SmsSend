package com.example.administrator.smssend.center;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.smssend.server.URLImp;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/24.
 */
public interface TestImp
{
    void init();

    void test();

    void byAuto();

    void fillUI(String s);

    void byManual();

    void send1();

    void send2();
}
