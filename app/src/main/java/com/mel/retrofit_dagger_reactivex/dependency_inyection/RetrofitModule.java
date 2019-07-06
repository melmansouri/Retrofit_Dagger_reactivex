package com.mel.retrofit_dagger_reactivex.dependency_inyection;

import com.mel.retrofit_dagger_reactivex.common.Constants;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Se encarga de proveer a nuestra actividad las dependancias necesarias para que funcionen nuestras clases
 * Con esto dagger sabe como va a crear los objetos que van a ser requeridos mas adelante
 */
@Module
public class RetrofitModule {

    @Singleton//Para crear una unica instancia
    @Provides //Para que nos pueda proveer ese objeto en la clase que queramos
    GsonConverterFactory provideGsonConverterFactory(){
        return GsonConverterFactory.create();
    }
    @Singleton//Para crear una unica instancia
    @Provides //Para que nos pueda proveer ese objeto en la clase que queramos
    HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
    @Singleton//Para crear una unica instancia
    @Provides //Para que nos pueda proveer ese objeto en la clase que queramos
    //El parametro de entrada lo va a coger siguiendo el grafo, es decir del metodo
    //que hay justo arriba. Es importante seguir el orden
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,GsonConverterFactory gsonConverterFactory){
        return new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    RetrofitService provideRetrofitService(Retrofit retrofit){
        return retrofit.create(RetrofitService.class);
    }

}
