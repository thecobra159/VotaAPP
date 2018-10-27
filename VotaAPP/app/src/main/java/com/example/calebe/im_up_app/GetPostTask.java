package com.example.calebe.im_up_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetPostTask extends AsyncTask<String, Void, Boolean> {
    private final ProgressBar progressBar;
    private final Activity context;
    private int what;
    private HashMap<String, String> post;

    public GetPostTask(ProgressBar progressBar, Activity context, int what) {
        this.progressBar = progressBar;
        this.context = context;
        this.what = what;
        this.post = new HashMap<>();
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Boolean result = false;
        HttpHandler httpHandler = new HttpHandler();
        switch(what){
            case Constants.LOGIN:
                LoginJson(result, httpHandler);
                break;
            case Constants.MAYOR:
                CandidateJson(result, httpHandler);
                break;
            case Constants.COUNCILOR:
                CandidateJson(result, httpHandler);
                break;
            case Constants.VOTE:
                VoteJson(result, httpHandler);
                break;
        }
        return result;
    }

    private void VoteJson(Boolean result, HttpHandler httpHandler) {

    }

    private void LoginJson(Boolean result, HttpHandler httpHandler) {
        String jsonStr = httpHandler.loginPost(Controller.getInstance().getAuthURL());
        try {
            JsonArray list = new JsonArray(jsonStr); //WHY GOD WHY
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void CandidateJson(Boolean result, HttpHandler httpHandler) {
        String jsonStr;
        if (what == Constants.COUNCILOR)
            jsonStr = httpHandler.getCandidate(Controller.getInstance().getCouncilorURL());
        else
            jsonStr = httpHandler.getCandidate(Controller.getInstance().getMayorURL());
        if (jsonStr != null) {
            try {
                JSONArray list = new JSONArray(jsonStr);

                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String name     = c.getString("nome");
                    String party    = c.getString("partido");
                    String photo    = c.getString("foto");
                    String id       = c.getString("id");

                    post.put("nome", name);
                    post.put("partido", party);
                    post.put("foto", photo);
                    post.put("id", id);
                }
                result = true;
                context.finish();
            } catch (JSONException e) {
                e.printStackTrace();
                result = false;
            }
        }
    }
}
