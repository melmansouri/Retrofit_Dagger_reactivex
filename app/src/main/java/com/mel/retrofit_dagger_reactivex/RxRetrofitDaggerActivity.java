package com.mel.retrofit_dagger_reactivex;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.retrofit_dagger_reactivex.adapter.PersonajeAdapter;
import com.mel.retrofit_dagger_reactivex.dependency_inyection.MyApp;
import com.mel.retrofit_dagger_reactivex.model.Data;
import com.mel.retrofit_dagger_reactivex.model.Personaje;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
 */
public class RxRetrofitDaggerActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Personaje> personajes;
    private PersonajeAdapter adapter;
    private Disposable disposable;

    @Inject
    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        setUpDagger();
        initRecyclerView();
        request();
    }

    private void setUpDagger() {
        //Le estoy diciendo que inyecte la activity para inyectar los
        //componentes que se necesitan como RetrofitService
        //Si no se hace esto retrofitService provocara un nullpointerException
        ((MyApp) getApplication()).getRetrofitComponent().inject(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personajes = new ArrayList<>();
        adapter = new PersonajeAdapter(personajes);
        recyclerView.setAdapter(adapter);
    }

    private void request() {
        Observable<Data> request = retrofitService.getPersonajesObservable();
        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Data data) {
                        adapter.setPersonajes(data.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RxRetrofitDaggerActivity.this, "Oops algo ha salido mal!!!. Comprueba tu conexión", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG","OnComplete: ");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
