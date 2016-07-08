package com.example.administrator.smssend.server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/6/23.
 */

public interface ApiInterface
{
    @GET()
    Call<String> getInfo(@Url String url);

    @FormUrlEncoded
    @POST
    Call<String> notify(@Url String url, @Field("hRet") String hRet, @Field("status") String status, @Field("cpparam") String cpparam);

    @POST
    Call<String> notify(@Url String url,@Body String info);

    @POST
    Call<String> notifyByQuery(@Url String url, @Query("hRet") String hRet, @Query("status") String status, @Query("cpparam") String cpparam);
}
