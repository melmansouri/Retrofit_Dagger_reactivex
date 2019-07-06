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
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitClient;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Tenemos el codigo muy acomplado por el uso de new. Por ello comop regla general debemos evitar usar la palabra new en nuestras clases
 * como esto: okHttpClient = new OkHttpClient.Builder();
 * si queremos cambiar el objeto okHttpClient deberemos tocar en todas las activities que hace uso de retrofit.
 * Por ello segun el 5ª principio de SOLID, el principio de inyeccion de dependencia, se debe de depender de abtracciones y no de implementaciones
 * que es como lo estamos haciendo hasta la fecha. Asi que las inyeccion de dependiacias es uno de los metodos que nos ofrecen solucion a este principio
 * La definicion de este principio es que las clases no deben de crear los objetos si no hay que suministrarselos mediante dagger
 *
 */
public class DaggerActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Personaje> personajes;
    private PersonajeAdapter adapter;

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
                }else{
                    Toast.makeText(DaggerActivity.this,"Algo ha pasado en el servidor.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(DaggerActivity.this,"Oops algo ha salido mal!!!. Comprueba tu conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
