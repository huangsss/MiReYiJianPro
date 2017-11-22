package com.huangj.mireyijianpro.net;

import com.huangj.mireyijianpro.common.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huangasys on 2017/11/22.15:35
 */

public class HttpManager {
     private static HttpManager sHttpManager;

     private HttpManager(){

     }

     public static HttpManager getHttpManager(){
         if (sHttpManager == null){
             synchronized (HttpManager.class){
                 if (sHttpManager == null){
                     sHttpManager = new HttpManager();
                 }
             }
         }
         return sHttpManager;
    }

    /**
     *
     * @return 返回指定数据;
     */
    public Api getCategoryService(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        return api;
    }

}
