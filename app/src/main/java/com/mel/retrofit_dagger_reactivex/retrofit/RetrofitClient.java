package com.mel.retrofit_dagger_reactivex.retrofit;

import com.mel.retrofit_dagger_reactivex.common.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Con el singleton nos ahorramos el tener que instanciar retrofit en todas las
 * activities en las que queramos hacer peticiones al ws y asi evitar desperdiciar memoria ya que puede que esas activities no
 * se cierren y esas instancias permanezcan en memoria.
 * Hay una mejor opcion a usar un singleton y es usar la inyeccion de dependencia como ahora que sera con dagger2.
 * La razon esta en que es muy complicado de testear debido a que guarda siempre la misma referencia y durante el test no podemos cambiar
 * cuando lo necesitamos. Por supuesto siempre hay trucos pero dagger2 nos soluciona este problema
 */
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
