package com.example.thecobra.votaapp;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;


class Controller {
    private static Controller ourInstance;
    private final String urlServerAuth = "http://kairos-dev.tk/api/auth/";
    private final String urlServerCouncilor = "http://kairos-dev.tk/api/candidatos/:vereador";
    private final String urlServerMayor = "http://kairos-dev.tk/api/candidatos/:prefeito";
    private final String urlServerVote = "http://kairos-dev.tk/api/candidatos/votar/";
    private String userAuth, userPass;
    private JSONObject votedCounciler, votedMayor;
    private Boolean status = false;

    public static Controller getInstance() {
        if (ourInstance == null) {
            ourInstance = new Controller();
        }
        return ourInstance;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getVoteURL() {
        return urlServerVote;
    }

    public String getCouncilorURL() {
        return urlServerCouncilor;
    }

    public String getMayorURL() {
        return urlServerMayor;
    }

    public String getAuthURL() {
        return urlServerAuth;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public JSONObject getVotedCounciler() {
        return votedCounciler;
    }

    public void setVotedCounciler(JSONObject votedCounciler) {
        this.votedCounciler = votedCounciler;
    }

    public JSONObject getVotedMayor() {
        return votedMayor;
    }

    public void setVotedMayor(JSONObject votedMayor) {
        this.votedMayor = votedMayor;
    }

    public void alertMessage(Context view, String msg) {
        Toast.makeText(view, msg, Toast.LENGTH_SHORT).show();
    }
}
