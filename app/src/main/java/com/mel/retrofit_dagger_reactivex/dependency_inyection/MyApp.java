package com.mel.retrofit_dagger_reactivex.dependency_inyection;

import android.app.Application;

/**
 *
 */
public class MyApp extends Application {
    private RetrofitComponent retrofitComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //DaggerRetrofitComponent lo crea dagger automaticamente al compilar
        //El nombre es un estandar siempre hay que ponerlo de la misma forma
        //DaggerNameComponent
        retrofitComponent =DaggerRetrofitComponent
                .builder()
                .build();
    }

    public RetrofitComponent getRetrofitComponent(){
        return retrofitComponent;
    }
}
