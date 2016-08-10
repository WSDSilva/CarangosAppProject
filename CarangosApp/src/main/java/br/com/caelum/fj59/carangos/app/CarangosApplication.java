package br.com.caelum.fj59.carangos.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.modelo.Publicacao;
import br.com.caelum.fj59.carangos.tasks.RegistraAparelhoTask;

/**
 * Created by wanderson on 06/07/16.
 */
public class CarangosApplication extends Application{

    private List<AsyncTask<?,?,?>> tasks = new ArrayList<AsyncTask<?, ?, ?>>();
    private List<Publicacao> publicacoes = new ArrayList<Publicacao>();

    private static final String REGISTRADO = "registradoNoGcm";
    private static final String ID_DO_REGISTRO = "idDoRegistro";
    private SharedPreferences preferences;

    @Override
    public void onTerminate() {
        super.onTerminate();
        for(AsyncTask task : tasks){
            task.cancel(true);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("configs", Activity.MODE_PRIVATE);

        registraNoGcm();
    }

    private void registraNoGcm() {
        if(!usuarioRegistrado()){
            new RegistraAparelhoTask(this).execute();
        }else{
            MyLog.i("Aparelho já cadastrado! Seu id é: "+
                    preferences.getString(ID_DO_REGISTRO, null));
        }
    }

    private boolean usuarioRegistrado() {

        return preferences.getBoolean(REGISTRADO,false);
    }

    public void registra(AsyncTask<?,?,?> task){
        tasks.add(task);
    }


    public void desregistra(AsyncTask<?,?,?> task){
        tasks.remove(task);
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void lidaComRespostaDoRegistroNoServidor(String result) {

        if(result != null){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(REGISTRADO, true);
            editor.putString(ID_DO_REGISTRO,result);
            editor.commit();
        }
    }
}
