package com.mel.retrofit_dagger_reactivex.retrofit;

import com.mel.retrofit_dagger_reactivex.model.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitService {
    @GET("people")
    Call<Data> getPersonajes();
    @GET()
    Call<Data> getPersonajes(@Url String url);

    @GET("people")
    Call<Data> getPersonajesObservable();
    @GET()
    Call<Data> getPersonajesObservable(@Url String url);
}
