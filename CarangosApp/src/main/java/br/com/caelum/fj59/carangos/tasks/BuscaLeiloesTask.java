package br.com.caelum.fj59.carangos.tasks;

import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.webservice.WebClient;

/**
 * Created by wanderson on 22/07/16.
 */
public class BuscaLeiloesTask extends TimerTask {
    private Calendar horarioUlimaBusca;
    private CustomHandler handle;


    public BuscaLeiloesTask(CustomHandler handler, Calendar horarioUlimaBusca){
        this.handle = handler;
        this.horarioUlimaBusca = horarioUlimaBusca;
    }

    @Override
    public void run() {
        MyLog.i("Efetuando novo busca!");
        WebClient webClient;
        webClient = new WebClient("leilao/leilaoid54635"+
                                new SimpleDateFormat("ddMMyy-HHmmss")
                                .format(horarioUlimaBusca.getTime()),new CarangosApplication());

        String json = webClient.get();

        MyLog.i("Lances recebidos: "+ json);

        Message message = handle.obtainMessage();
        message.obj = json;
        handle.sendMessage(message);

        horarioUlimaBusca = Calendar.getInstance();

    }

    public void executa(){
        Timer timer = new Timer();
        timer.schedule(this,0,30*1000);
    }
}
