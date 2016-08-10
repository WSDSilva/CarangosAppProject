package br.com.caelum.fj59.carangos.infra;

import android.app.Application;

import br.com.caelum.fj59.carangos.R;

public class MyServer {
    private String uri;

    public MyServer(Application app) {
        this.uri = app.getResources().getString(R.string.server_uri);
    }


    public String uriFor(String value) {

        return String.format(uri, value);
    }
}
