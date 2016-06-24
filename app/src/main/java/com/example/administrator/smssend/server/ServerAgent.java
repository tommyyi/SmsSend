package com.example.administrator.smssend.server;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/6/23.
 */

public class ServerAgent
{
    public static API getmAPI()
    {
        return mAPI;
    }

    private static API mAPI;
    static
    {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://www.baidu.com").addConverterFactory(new Converter.Factory()
        {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit)
            {
                return new Converter<ResponseBody, String>()
                {
                    @Override
                    public String convert(ResponseBody value) throws IOException
                    {
                        return value.string();
                    }
                };
            }
        }).build();
        mAPI=retrofit.create(API.class);
    }
}
