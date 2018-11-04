package com.example.thecobra.votaapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetPostTask extends AsyncTask<String, Void, Boolean> {

    private final Activity context;
    private int what;
    private HashMap<String, String> post;
    private Boolean result = false;

    public GetPostTask(Activity context, int what) {
        this.context = context;
        this.what = what;
        this.post = new HashMap<>();

        AndroidNetworking.initialize(context);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        result = false;
        HttpHandler httpHandler = new HttpHandler();
        try {
            switch (what) {
                case Constants.LOGIN:
                    LoginJson(httpHandler);
                    break;
                case Constants.MAYOR:
                    CandidateJson(httpHandler);
                    break;
                case Constants.COUNCILOR:
                    CandidateJson(httpHandler);
                    break;
                case Constants.VOTE:
                    VoteJson(httpHandler);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void VoteJson(HttpHandler httpHandler) {

    }

    private void LoginJson(HttpHandler httpHandler) throws JSONException {
        JSONObject loginJson = new JSONObject();

        loginJson.put("username", Controller.getInstance().getUserAuth());
        loginJson.put("password", Controller.getInstance().getUserPass());

        AndroidNetworking.post(Controller.getInstance().getAuthURL())
                .addJSONObjectBody(loginJson)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseJson = response.getString("auth");
                            if (responseJson.equals("true")) {
                                result = true;
                                Controller.getInstance().alertMessage(context, "Login feito com sucesso!");
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                context.finish();
                            } else {
                                result = false;
                                Controller.getInstance().alertMessage(context, "Login inválido!");
                            }
                            Controller.getInstance().setStatus(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void CandidateJson(HttpHandler httpHandler) {
        String jsonStr;
        if (what == Constants.COUNCILOR)
            jsonStr = httpHandler.getCandidate(Controller.getInstance().getCouncilorURL());
        else
            jsonStr = httpHandler.getCandidate(Controller.getInstance().getMayorURL());
        if (jsonStr != null) {
            AndroidNetworking.get(jsonStr)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String name = response.getString("nome");
                                String party = response.getString("partido");
                                String photo = response.getString("foto");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }
}
