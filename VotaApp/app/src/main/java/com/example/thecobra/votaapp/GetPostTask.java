package com.example.thecobra.votaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetPostTask extends AsyncTask<String, Void, Integer> {

    private final ProgressBar progressBar;
    private final Context context;
    private ListView listView;
    private ArrayList<HashMap<String, String>> postList;

    public GetPostTask(ProgressBar progressBar, Context context, ListView listView) {
        this.progressBar = progressBar;
        this.context = context;
        this.listView = listView;
        this.postList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Integer result) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int result = -1;
        String url = strings[0];
        HttpHandler httpHandler = new HttpHandler();
        String jsonStr = httpHandler.makeServiceCall(url);
        if (jsonStr != null){
            try {
                JSONArray list = new JSONArray(jsonStr);

                for (int i = 0; i < list.length(); i++) {
                    JSONObject c = list.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");
                    String body = c.getString("body");

                    HashMap<String, String> post = new HashMap<>();
                    post.put("id", id);
                    post.put("name", name);
                    post.put("email", email);
                    post.put("body", body);

                    postList.add(post);
                }
                result = 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
