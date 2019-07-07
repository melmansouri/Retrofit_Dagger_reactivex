package com.mel.retrofit_dagger_reactivex;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.retrofit_dagger_reactivex.adapter.PersonajeAdapter;
import com.mel.retrofit_dagger_reactivex.common.Constants;
import com.mel.retrofit_dagger_reactivex.dependency_inyection.MyApp;
import com.mel.retrofit_dagger_reactivex.model.Data;
import com.mel.retrofit_dagger_reactivex.model.Personaje;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Tenemos el codigo muy acomplado por el uso de new. Por ello comop regla general debemos evitar usar la palabra new en nuestras clases
 * como esto: okHttpClient = new OkHttpClient.Builder();
 * si queremos cambiar el objeto okHttpClient deberemos tocar en todas las activities que hace uso de retrofit.
 * Por ello segun el 5ª principio de SOLID, el principio de inyeccion de dependencia, se debe de depender de abtracciones y no de implementaciones
 * que es como lo estamos haciendo hasta la fecha. Asi que las inyeccion de dependiacias es uno de los metodos que nos ofrecen solucion a este principio
 * La definicion de este principio es que las clases no deben de crear los objetos si no hay que suministrarselos mediante dagger
 * Hacemos uso eficiente de la memoria mediante el uso de dagger y tambien hemos implementado el 5ª principio de SOLID
 * es decir que ahora dependemos de una abstraccion y no de la implementacion de la clase DaggerOrdenadoActivity
 */
public class RxRetrofitDaggerOrdenadoActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Personaje> personajes;
    private PersonajeAdapter adapter;
    @Inject
    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit_dagger_ordenado_pagination_swipe_refresh);
        ButterKnife.bind(this);
        setUpDagger();
        initRecyclerView();
        request();
    }

    private void setUpDagger() {
        ((MyApp)getApplication()).getRetrofitComponent().inject(this);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personajes=new ArrayList<>();
        adapter =new PersonajeAdapter(personajes);
        recyclerView.setAdapter(adapter);
    }

    private void request(){
        Call<Data> request=retrofitService.getPersonajes();
        request.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Data data=response.body();
                    adapter.setPersonajes(data.getResults());
                    nextRequest(data.getNext(),data.getResults());
                }else{
                    Toast.makeText(RxRetrofitDaggerOrdenadoActivity.this,"Algo ha pasado en el servidor.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(RxRetrofitDaggerOrdenadoActivity.this,"Oops algo ha salido mal!!!. Comprueba tu conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextRequest(String next, List<Personaje> results) {
        String endPoint=next.substring(Constants.URL_API.length());
        Call<Data> request=retrofitService.getPersonajes(endPoint);
        request.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Data data=response.body();
                    results.addAll(data.getResults());
                    Collections.sort(results, new Comparator<Personaje>() {
                        @Override
                        public int compare(Personaje o1, Personaje o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    adapter.setPersonajes(results);
                }else{
                    Toast.makeText(RxRetrofitDaggerOrdenadoActivity.this,"Algo ha pasado en el servidor.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(RxRetrofitDaggerOrdenadoActivity.this,"Oops algo ha salido mal!!!. Comprueba tu conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
