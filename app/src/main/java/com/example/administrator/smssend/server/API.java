package com.example.administrator.smssend.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/6/23.
 */

public interface API
{
    @GET("")
    Call<String> getInfo(@Url String url);
}
