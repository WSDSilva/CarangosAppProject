package br.com.caelum.fj59.carangos.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.com.caelum.fj59.carangos.activity.MainActivity;
import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.converter.PublicacaoConverter;
import br.com.caelum.fj59.carangos.evento.EventoPublicacoesRecebidas;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.modelo.Publicacao;
import br.com.caelum.fj59.carangos.webservice.Pagina;
import br.com.caelum.fj59.carangos.webservice.WebClient;

/**
 * Created by erich on 7/16/13.
 */
public class BuscaMaisPublicacoesTask extends AsyncTask<Pagina, Void, List<Publicacao>> {

    private Exception erro;
//    private MainActivity activity;
//    private BuscaMaisPublicacoesDelegate delegate;
    CarangosApplication application;

//    public BuscaMaisPublicacoesTask(MainActivity activity) {
//        this.activity = activity;
//    }


    /*public BuscaMaisPublicacoesTask(BuscaMaisPublicacoesDelegate delegate) {
        this.delegate = delegate;
        this.delegate.getCarangosApplication().registra(this);
    }*/

    public BuscaMaisPublicacoesTask(CarangosApplication application) {
        this.application = application;
        application.registra(this);
    }

    @Override
    protected List<Publicacao> doInBackground(Pagina... paginas) {
        try {
            Pagina paginaParaBuscar = paginas.length > 1? paginas[0] : new Pagina();

            String jsonDeResposta = new WebClient("post/list?" + paginaParaBuscar, this.application).get();

            List<Publicacao> publicacoesRecebidas = new PublicacaoConverter().converte(jsonDeResposta);

            return publicacoesRecebidas;
        } catch (Exception e) {
            this.erro = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Publicacao> retorno) {
        if (retorno!=null) {
            EventoPublicacoesRecebidas.notifica(this.application,(Serializable) retorno,true);
//           delegate.lidaComRetorno(retorno);
        } else {
            EventoPublicacoesRecebidas.notifica(this.application,erro, false);
//            delegate.lidaComErro(erro);
        }
//        delegate.getCarangosApplication().desregistra(this);
        this.application.desregistra(this);
    }
}
