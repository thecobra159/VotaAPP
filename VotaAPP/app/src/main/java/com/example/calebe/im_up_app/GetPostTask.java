package com.example.calebe.im_up_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetPostTask extends AsyncTask<String, Void, Boolean> {
    private final Activity context;
    private int what;
    private HashMap<String, String> post;

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
        Boolean result = false;
        HttpHandler httpHandler = new HttpHandler();
        switch (what) {
            case Constants.LOGIN:
                try {
                    LoginJson(result, httpHandler);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void LoginJson(Boolean result, HttpHandler httpHandler) throws JSONException {
        JSONObject loginJson = new JSONObject();

        loginJson.put("username", Controller.getInstance().getUserAuth());
        loginJson.put("password", Controller.getInstance().getUserPass());

        AndroidNetworking.post(Controller.getInstance().getAuthURL())
                .addJSONObjectBody(loginJson)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
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

                    String name = c.getString("nome");
                    String party = c.getString("partido");
                    String photo = c.getString("foto");
                    String id = c.getString("id");

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
