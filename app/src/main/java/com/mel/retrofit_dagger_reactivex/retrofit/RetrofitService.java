package com.mel.retrofit_dagger_reactivex.retrofit;

import com.mel.retrofit_dagger_reactivex.model.Data;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitService {
    @GET("people")
    Call<Data> getPersonajes();
    @GET()
    Call<Data> getPersonajes(@Url String url);

    /**
     * Retrofit ya esta bien hecho y ya te ejecuta las peticiciones en segundo plano
     * Pero con RXjava tenemos el control de en que hilo queremos ejecutar la peticion
     * y todo tipo de tareas que queremos que se ejecuten en ese hilo una vez recibido el dato
     * y justo antes de emitirlo al observer para que lo muestre en pantalla
     *
     * @return
     */
    @GET("people")
    Observable<Data> getPersonajesObservable();
    @GET()
    Observable<Data> getPersonajesObservable(@Url String url);
}
