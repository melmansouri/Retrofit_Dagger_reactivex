package com.mel.retrofit_dagger_reactivex.retrofit;

import com.mel.retrofit_dagger_reactivex.common.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private RetrofitService retrofitService;
    private Retrofit retrofit;
    private RetrofitClient(){
        //Loggin-interceptor es opcional. Sirve para recoger log de las peticiones y debuggear estas.
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient=new OkHttpClient.Builder();
        okHttpClient.addInterceptor(httpLoggingInterceptor);

        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();

        retrofitService=retrofit.create(RetrofitService.class);
    }

    public static RetrofitClient getInstance(){
        if (instance==null){
            instance=new RetrofitClient();
        }
        return instance;
    }

    public RetrofitService getRetrofitService(){
        return retrofitService;
    }
}
