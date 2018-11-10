package com.example.thecobra.votaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView imgCouncilor, imgMayor;
    private Button btnCouncilor, btnMayor, btnVote;
    private TextView mayorParty, mayorName, councilorName, councilorParty;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCouncilor = findViewById(R.id.photoCouncilor);
        btnCouncilor = findViewById(R.id.btnCouncilor);
        imgMayor = findViewById(R.id.photoMayor);
        btnMayor = findViewById(R.id.btnMayor);
        btnVote = findViewById(R.id.btnVote);
        imgCouncilor = findViewById(R.id.photoCouncilor);
        imgMayor = findViewById(R.id.photoMayor);
        mayorParty = findViewById(R.id.mayorParty);
        mayorName = findViewById(R.id.mayorName);
        councilorName = findViewById(R.id.councilorName);
        councilorParty = findViewById(R.id.councilorParty);

        AndroidNetworking.initialize(this);

        if( Controller.getInstance().getUserVotou())
        {
            btnCouncilor.setEnabled(false);
            btnMayor.setEnabled(false);
            btnVote.setEnabled(false);
        }

        btnCouncilor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CouncilorActivity.class);
                startActivity(intent);
            }
        });

        btnMayor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MayorActivity.class);
                startActivity(intent);
            }
        });

        btnVote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Controller.getInstance().isReadyToVote_mayor() && Controller.getInstance().isReadyToVote_councilers())
                {
                    // POST
                    String id_vereador = null, id_prefeito = null;
                    try
                    {
                        id_vereador = Controller.getInstance().getCouncilersClicked().getString("_id");
                        id_prefeito = Controller.getInstance().getMayorsClicked().getString("_id");
                        JSONObject meu_voto = Controller.getInstance().makeVote(id_prefeito, id_vereador);
                        Log.d("Vereador", id_vereador);
                        Log.d("Prefeito", id_prefeito);
                        Log.d("JSON", meu_voto.toString());
                        AndroidNetworking.post(Controller.getInstance().getVoteURL())
                                .addJSONObjectBody(meu_voto)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {
                                        Controller.getInstance().alertMessage(getApplication(), response.toString());
                                        btnVote.setEnabled(false);
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Controller.getInstance().alertMessage(getApplication(), "Você precisa escolher seus candidatos!");
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(Controller.getInstance().isReadyToVote_councilers())
        {
            try
            {
                Glide.with(this).load(Controller.getInstance().getCouncilersClicked().getString("foto")).into(imgCouncilor);
                councilorName.setText(Controller.getInstance().getCouncilersClicked().getString("nome"));
                councilorParty.setText(Controller.getInstance().getCouncilersClicked().getString("partido"));
                btnCouncilor.setEnabled(false);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        if (Controller.getInstance().isReadyToVote_mayor())
        {
            try
            {
                Glide.with(this).load(Controller.getInstance().getMayorsClicked().getString("foto")).into(imgMayor);
                mayorParty.setText(Controller.getInstance().getMayorsClicked().getString("partido"));
                mayorName.setText(Controller.getInstance().getMayorsClicked().getString("nome"));
                btnMayor.setEnabled(false);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
