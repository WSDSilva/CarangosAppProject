package br.com.caelum.fj59.carangos.gcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.fj59.carangos.R;
import br.com.caelum.fj59.carangos.activity.LeilaoActivity;
import br.com.caelum.fj59.carangos.activity.MainActivity;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.webservice.WebClient;

/**
 * Created by wanderson on 19/07/16.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i("Chegou a mesagem do GCM!");

/*        String regId = intent.getExtras().getString("registration_id");
        MyLog.i("VALOR DO regId " + regId);

        if (regId != null && !regId.equals("")){
            String email = InformacoesDoUsuario.getEmail(context);
            MyLog.i("VALOR DO regId " + regId);
            String url = "device/register/"+email+"/"+regId;
            WebClient client = new WebClient(url);
            client.post();
        }*/

        if (appEstaRodando(context)){
            Toast.makeText(context,"Novo leilão começou",Toast.LENGTH_LONG);
        }else {


            String mensagem = (String) intent.getExtras().getSerializable("message");

            MyLog.i("Mensagem om conteúdo: " + mensagem);

            Intent irParaLeilao = new Intent(context, LeilaoActivity.class);

            PendingIntent acaoPendente = PendingIntent.getActivity(context, 0, irParaLeilao,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            irParaLeilao.putExtra("idDaNotificacao", Constantes.ID_NOTIFICACAO);

            Notification notificacao = new Notification.Builder(context)
                    .setContentTitle("Um novo leilão começou !!")
                    .setContentText("Leilão: " + mensagem)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(acaoPendente) //<-- IMPORTANTE!
                    .build();

            NotificationManager manager = (NotificationManager)
                    context.getSystemService(context.NOTIFICATION_SERVICE);

            manager.notify(Constantes.ID_NOTIFICACAO, notificacao);
        }
    }

    private boolean appEstaRodando(Context context) {

        ActivityManager am = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if(!tasks.isEmpty()){
            ComponentName topActivity = tasks.get(0).topActivity;
            if(!topActivity.getPackageName()
                    .equals(context.getPackageName())){
                return false;
            }
        }

        return true;
    }
}
