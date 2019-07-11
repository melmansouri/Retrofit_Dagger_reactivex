package com.mel.retrofit_dagger_reactivex;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
    private boolean isLoadingNewData = false;
    private Disposable disposable;
    private String lastPersonajeUrlNext;

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
        ((MyApp) getApplication()).getRetrofitComponent().inject(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personajes = new ArrayList<>();
        adapter = new PersonajeAdapter(personajes);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("listaY", "Estado scroll: " + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d("listaY",dy+"");
                if ((dy > 0 && ((PersonajeAdapter) recyclerView.getAdapter()).isEndScroll()) && !isLoadingNewData) {
                    Log.d("listaY", "END");
                    nextRequest(lastPersonajeUrlNext);
                }
            }
        });
    }

    private void request() {
        retrofitService.getPersonajesObservable().subscribeOn(Schedulers.io())
                /*.flatMap(new Function<Data, ObservableSource<Data>>() {

                    @Override
                    public ObservableSource<Data> apply(Data data) throws Exception {
                        personajes = data.getResults();
                        //SE ejecuta en el hilo secundario
                        return retrofitService.getPersonajesObservable(data.getNext()).subscribeOn(Schedulers.io());
                    }
                }).map(new Function<Data, Data>() {
            @Override
            public Data apply(Data data) throws Exception {
                personajes.addAll(data.getResults());
                Collections.sort(personajes,new Comparator<Personaje>() {
                    @Override
                    public int compare(Personaje o1, Personaje o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                data.setResults(personajes);
                return data;
            }
        })*/.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Data>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(Data r) {
                lastPersonajeUrlNext=r.getNext();
                personajes = r.getResults();
                adapter.setPersonajes(personajes);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void nextRequest(String next) {
        if (TextUtils.isEmpty(next)){
            return;
        }
        String endPoint = next.substring(Constants.URL_API.length());
        retrofitService.getPersonajesObservable(endPoint)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Data>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(Data data) {
                lastPersonajeUrlNext=data.getNext();
                personajes.addAll(data.getResults());
                adapter.setPersonajes(personajes);
                isLoadingNewData=false;
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
