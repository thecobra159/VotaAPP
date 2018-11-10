package com.example.thecobra.votaapp;

import android.content.Intent;
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

public class MayorActivity extends AppCompatActivity
{

    private ListView listViewMayors;
    private ImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor);
        AndroidNetworking.initialize(MayorActivity.this);
        Controller.getInstance().createProgressDialog(MayorActivity.this, "Aguarde...", "Carregando Candidatos", false);
        try
        {
            AndroidNetworking.get(Controller.getInstance().getMayorURL())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener()
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            Log.d("HTTP", response.toString());
                            if( response != null)
                            {
                                Controller.getInstance().setMayors(response);
                                listViewMayors = findViewById(R.id.listViewMayors);
                                adapter = new ImagesAdapter(getApplicationContext(), Controller.getInstance().getMayors());
                                listViewMayors.setAdapter(adapter);
                                listViewMayors.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        Controller.getInstance().setMayorsClicked(adapter.getItem(position));
                                        Intent intent = new Intent(getApplicationContext(), MayorToVoteActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                Controller.getInstance().destroyProgressDialog();
                            }
                        }
                        @Override
                        public void onError(ANError anError)
                        {

                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
//        if(Controller.getInstance().isReadyToVote_mayor())
//        {
//            finish();
//        }
    }
}
