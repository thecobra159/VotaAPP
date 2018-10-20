package com.example.thecobra.votaapp;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

class Controller {
    private static Controller ourInstance;
    private String urlServer = "http://kairos-dev.tk/api/auth/";
    private File imageClicked = null;

    public static Controller getInstance() {
        if(ourInstance == null)
            ourInstance = new Controller();
        return ourInstance;
    }

    public String getUrlServer() {
        return urlServer;
    }

    public void setUrlServer(String URL){
        this.urlServer = URL;
    }

    public void alertMessage(Context view, String msg){
        Toast.makeText(view, msg, Toast.LENGTH_SHORT).show();
    }
}
