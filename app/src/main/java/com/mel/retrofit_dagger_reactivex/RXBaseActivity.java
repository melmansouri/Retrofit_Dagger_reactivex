package com.mel.retrofit_dagger_reactivex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * La programacion reactiva: programacion asincrona basada en eventos. Se basa en un flujo de datos asincronos
 * por lo tanto pueden ser observados y cuando se emitan esos datos se llevara a cabo una operacion con ellos
 * Los flujos de datos pueden ser llamadas a ws, cambios de variables, eventos de click, de una bd , etc
 * Se compone de los observables que son una fuente de datos que emite datos, los observers que reciben esos datos que son emitidos por el observable
 * Un observable puede ser observado por varios observers. Luego estan los suscriptores que permiten vincular un observable con un observer
 * Los operator modifican los datos que son emitidos por los observables antes de que el observador los reciba
 * Por ejemplo cuando ordenamos en el onResponse de Retrofit en RetrofitOrdenadoActivity eso ya actua en hilo principal y esa operacion en este caso no tarda mucho
 * pero puede dar el caso que si, por ello habria que crear hilos para evitarlo para manejar esa operacion. Mediante RXJava evitamos el crearnos esos hilos
 * y el tener que manejarlos y permite que todos estos tipos de acciones que queremos antes de que se muestren en pantalla se hagan en hilos en background
 * dejando libre el hilo principal
 * Otro de los componentes es el schedule que decide el hilo en el que el observable debe emitir los datos y tambien el hilo en el que el observador recibe esos datos
 * Tambien esta la posibilidad de eliminar la relacion que hay entre el observable y el observer por ejemplo cuando se destruye la activity y asi evitar
 * problemas de memory leak. Para ello tenemos el objeto Disposeable
 */
public class RXBaseActivity extends AppCompatActivity {

    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbase);
        crearObservable()
                .subscribeOn(Schedulers.io())//Donde queremos ejecutar el observable
                .observeOn(AndroidSchedulers.mainThread())//Donde queremos ejecutar el observer que en este caso seria el hilo principal
                .subscribe(crearObserver());
    }

    /**
     * La fuente de datos
     * @return
     */
    private Observable crearObservable(){
        /**
         * Hay otras muchas formas de crear el observable
         */
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    //Un observable puede emitir 3 tipos de datos
                    //1
                    emitter.onNext("Primer dato del observable");
                    emitter.onNext("Segundo dato del observable");
                    emitter.onNext(tareaLargaDuracion());
                    //2
                    emitter.onComplete();//Ha dejado ya de emitir datos
                }catch (Exception e){
                    e.printStackTrace();
                    //3
                    emitter.onError(e);
                }
            }
        });
    }

    private Observer crearObserver(){
        return new Observer<String>(){

            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(String s) {
                Log.d("TAG1","OnNext: "+s+" Hilo: "+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d("TAG1","OnComplete Hilo: "+Thread.currentThread().getName());
            }
        };
    }
    /**
     * Esto esta mal hacerlo ya que bloqueara la aplicacion al correr en el hilo principal
     * Se puede solucionar usando hilos o asynctask. O mejor con con RXJava que puede resultar mas sencillo
     * @return
     */
    private String tareaLargaDuracion(){
        try{
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("TAG1","Observable: Hil: "+Thread.currentThread().getName());
        return "Tarea Larga Finalizada";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
