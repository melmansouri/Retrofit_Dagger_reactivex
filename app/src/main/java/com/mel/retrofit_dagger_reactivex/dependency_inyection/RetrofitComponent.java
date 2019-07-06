package com.mel.retrofit_dagger_reactivex.dependency_inyection;

import com.mel.retrofit_dagger_reactivex.DaggerActivity;
import com.mel.retrofit_dagger_reactivex.DaggerOrdenadoActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Puente entre modulos creados y la parte del cogido que va a necesitar que le inyectemos los
 * objetos que necesita
 */
@Singleton //Una unica instancia
@Component(modules = RetrofitModule.class)
public interface RetrofitComponent {
    void inject(DaggerActivity daggerActivity);
    void inject(DaggerOrdenadoActivity daggerOrdenadoActivity);
}
