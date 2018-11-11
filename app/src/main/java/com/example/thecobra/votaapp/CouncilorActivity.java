package com.example.thecobra.votaapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

public class CouncilorActivity extends AppCompatActivity {
    private ListView listViewCouncilors;
    private ImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councilor);

        getSupportActionBar().setTitle("Escolha o Vereador");
        Controller.getInstance().clearCouncilers();
        AndroidNetworking.initialize(CouncilorActivity.this);
        Controller.getInstance().createProgressDialog(CouncilorActivity.this, "Aguarde...", "Carregando Candidatos", false);
        try {
            AndroidNetworking.get(Controller.getInstance().getCouncilorURL())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("HTTP", response.toString());
                            if (response != null) {
                                Controller.getInstance().setCouncilers(response);

                                listViewCouncilors = findViewById(R.id.listViewCouncilors);
                                adapter = new ImagesAdapter(getApplicationContext(), Controller.getInstance().getCouncilers());
                                listViewCouncilors.setAdapter(adapter);
                                listViewCouncilors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Controller.getInstance().setCouncilersClicked(adapter.getItem(position));
                                        Intent intent = new Intent(getApplicationContext(), CouncilorToVoteActivity.class);
                                        startActivity(intent);
//                                        finish();
                                    }
                                });
                                Controller.getInstance().destroyProgressDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void returnToActivity(View view) {
        finish();
    }
}
