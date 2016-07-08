package com.example.administrator.smssend.server;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/6/23.
 */

public class ServerAgent
{
    public static ApiInterface getAPI()
    {
        return M_API_INTERFACE;
    }

    private static final ApiInterface M_API_INTERFACE;

    static
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.baidu.com").addConverterFactory(new Converter.Factory()
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

            @Override
            public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit)
            {
                return new Converter<String, RequestBody>()
                {
                    @Override
                    public RequestBody convert(String value) throws IOException
                    {
                        return RequestBody.create(MediaType.parse("text/html"),value);
                    }
                };
            }
        }).build();
        M_API_INTERFACE = retrofit.create(ApiInterface.class);
    }
}
