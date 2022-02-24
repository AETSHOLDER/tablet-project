package com.example.paperlessmeeting_demo.network;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by WP on 2020/7/21.
 */

public class AddTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String perferences = null;
        if (Hawk.contains("token")) {
            perferences = Hawk.get("token");
            builder.addHeader("Authorization", "Bearer "+perferences);
        }
        return chain.proceed(builder.build());
    }
}

