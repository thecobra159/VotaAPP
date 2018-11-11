package com.example.thecobra.votaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


class Controller {
    private static Controller ourInstance;
    private final String urlServerAuth = "http://kairos-dev.tk/api/auth/";
    private final String urlServerCouncilor = "http://kairos-dev.tk/api/candidatos/vereador";
    private final String urlServerMayor = "http://kairos-dev.tk/api/candidatos/prefeito";
    private final String urlServerVote = "http://kairos-dev.tk/api/votar";
    private String userAuth, userPass;
    private boolean  userVotou = false;
    private JSONObject votedCounciler, votedMayor;
    private ArrayList<JSONObject> councilers = new ArrayList<JSONObject>();
    private ArrayList<JSONObject> mayors = new ArrayList<JSONObject>();
    private JSONObject mayorsClicked = null;
    private JSONObject councilersClicked = null;

    private boolean readyToVote_mayor = false;
    private boolean readyToVote_councilers = false;
    private ProgressDialog progressDialog = null;
    private AlertDialog.Builder alertDialogBuilder = null;
    private AlertDialog alertDialog = null;

    private Boolean status = false;

    public static Controller getInstance()
    {
        if (ourInstance == null) { ourInstance = new Controller(); }
        return ourInstance;
    }

    public void clear()
    {
        this.ourInstance = null;
        this.votedCounciler = null;
        this.votedMayor = null;
        this.councilers.clear();
        this.mayors.clear();;
        this.mayorsClicked = null;
        this.councilersClicked = null;
        this.readyToVote_mayor = false;
        this.readyToVote_councilers = false;
        this.progressDialog = null;
        this.alertDialogBuilder = null;
        this.alertDialog = null;
        this.status = null;
    }

    public void createProgressDialog(Context context, String title, String message, boolean cancelable)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void destroyProgressDialog()
    {
        progressDialog.dismiss();
        progressDialog = null;
    }

    public AlertDialog createAlertDialog(final Context context, String title, String message, boolean cancelable)
    {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(cancelable);
        alertDialogBuilder.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.dismiss();
                        alertDialog = null;
                        alertDialogBuilder = null;
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }

    public void setUserVotou(boolean aux) { this.userVotou = aux; }

    public boolean getUserVotou() { return this.userVotou; }

    public JSONObject makeVote (String idCouncilor, String idMayor) throws JSONException {
        JSONObject jsonVote = new JSONObject();

        jsonVote.put("eleitor", userAuth);
        jsonVote.put("vereador", idCouncilor);
        jsonVote.put("prefeito", idMayor);

        return jsonVote;
    }
    public boolean isReadyToVote_mayor() {
        return readyToVote_mayor;
    }

    public void setReadyToVote_mayor(boolean readyToVote_mayor) {
        this.readyToVote_mayor = readyToVote_mayor;
    }

    public boolean isReadyToVote_councilers() {
        return readyToVote_councilers;
    }

    public void setReadyToVote_councilers(boolean readyToVote_councilers) {
        this.readyToVote_councilers = readyToVote_councilers;
    }

    public JSONObject getMayorsClicked() { return mayorsClicked; }

    public void setMayorsClicked(JSONObject mayorsClicked) { this.mayorsClicked = mayorsClicked; }

    public JSONObject getCouncilersClicked() { return councilersClicked; }

    public void setCouncilersClicked(JSONObject councilersClicked) { this.councilersClicked = councilersClicked; }

    public void setCouncilers(JSONArray aux)
    {
        for(int index = 0; index < aux.length(); index++)
        {
            try { councilers.add(aux.getJSONObject(index)); } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public void clearCouncilers()
    {
        this.councilers.clear();
    }

    public void clearMayors()
    {
        this.mayors.clear();
    }

    public void setMayors(JSONArray aux)
    {
        for(int index = 0; index < aux.length(); index++)
        {
            try { mayors.add(aux.getJSONObject(index)); } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    public ArrayList<JSONObject> getMayors() { return this.mayors; }

    public ArrayList<JSONObject> getCouncilers() { return this.councilers; }

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

    public String getMayorURL() { return urlServerMayor; }

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

    public void setVotedCounciler(JSONObject votedCounciler) { this.votedCounciler = votedCounciler; }

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
