package com.mel.retrofit_dagger_reactivex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btRetrofit)
    public void retrofit(){
        startActivity(new Intent(this,RetrofitActivity.class));
    }
    @OnClick(R.id.btRetrofitOrdenado)
    public void retrofitOrdenado(){
        startActivity(new Intent(this,RetrofitOrdenadoActivity.class));
    }

    @OnClick(R.id.btDagger)
    public void dagger(){
        startActivity(new Intent(this,DaggerActivity.class));
    }
    @OnClick(R.id.btDaggerOrdenado)
    public void daggerOrdenado(){
        startActivity(new Intent(this,DaggerOrdenadoActivity.class));
    }

    @OnClick(R.id.btRxBasico)
    public void rxBasico(){
        startActivity(new Intent(this,RXBaseActivity.class));
    }
}
