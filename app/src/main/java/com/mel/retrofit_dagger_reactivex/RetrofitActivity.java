package com.mel.retrofit_dagger_reactivex;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mel.retrofit_dagger_reactivex.adapter.PersonajeAdapter;
import com.mel.retrofit_dagger_reactivex.model.Data;
import com.mel.retrofit_dagger_reactivex.model.Personaje;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitClient;
import com.mel.retrofit_dagger_reactivex.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RetrofitClient retrofitClient;
    private RetrofitService retrofitService;
    private List<Personaje> personajes;
    private PersonajeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        initRetrofit();
        initRecyclerView();
        request();
    }

    private void initRetrofit(){
        retrofitClient =RetrofitClient.getInstance();
        retrofitService=retrofitClient.getRetrofitService();
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
                    Toast.makeText(RetrofitActivity.this,"Algo ha pasado en el servidor.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(RetrofitActivity.this,"Oops algo ha salido mal!!!. Comprueba tu conexión",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
