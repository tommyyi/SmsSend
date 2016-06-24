package com.example.administrator.smssend;

import android.content.Context;
import android.view.View;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/24.
 */
public interface TestImp
{
    void init();

    void test();

    void byManual();

    void byAuto();

    void fillUI(String s);

    String getInfo(Context context) throws IOException;

    void send1();

    void send2();

    void UpdateProgress(String tag, String info);
}
