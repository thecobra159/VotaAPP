package com.example.thecobra.votaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
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
    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpWindowAnimations();

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

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Você já votou!");
            alertDialogBuilder.setMessage("Você já participou do pleito eleitoral! Obrigado pelo seu voto! ");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            Controller.getInstance().clear();
                            finish();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
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
                if( (Controller.getInstance().getMayorsClicked() != null) && (Controller.getInstance().getCouncilersClicked() != null) )
                {
                    // POST
                    String id_vereador = null, id_prefeito = null;
                    Controller.getInstance().createProgressDialog(MainActivity.this, "Validando Voto...", "Enviando o seu voto para o servidor!", false);
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
                                        Controller.getInstance().destroyProgressDialog();
                                        try
                                        {
                                            if(response.getString("status").equals("true"))
                                            {
//                                                AlertDialog dialog = Controller.getInstance().createAlertDialog(MainActivity.this, "Voto Confirmado com Sucesso", null, false);

                                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(MainActivity.this);
                                                alertDialogBuilder1.setTitle("Voto Confirmado com Sucesso");
                                                alertDialogBuilder1.setMessage("Você será redirecionado para tela de login! Obrigado pelo seu voto!");
                                                alertDialogBuilder1.setCancelable(false);
                                                alertDialogBuilder1.setNeutralButton(android.R.string.ok,
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                alertDialog1.dismiss();
                                                                Controller.getInstance().setMayorsClicked(null);
                                                                Controller.getInstance().setCouncilersClicked(null);
                                                                Controller.getInstance().setReadyToVote_mayor(false);
                                                                Controller.getInstance().setReadyToVote_councilers(false);
                                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                startActivity(intent);
                                                                Controller.getInstance().clear();
                                                                finish();
                                                            }
                                                        });

                                                alertDialog1 = alertDialogBuilder1.create();
                                                alertDialog1.show();

                                                btnCouncilor.setEnabled(false);
                                                btnMayor.setEnabled(false);
                                                btnVote.setEnabled(false);
                                            }
                                            else
                                            {
                                                Controller.getInstance().createAlertDialog(MainActivity.this, "ERRO!", "Houve um problema ao confirmar o seu voto, tente mais tarde!", false);
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
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
                    Controller.getInstance().createAlertDialog(MainActivity.this, "Complete sua Escolha!", "Você precisa escolher seus candidatos!", false);
//                    Controller.getInstance().alertMessage(getApplication(), "Você precisa escolher seus candidatos!");
                }
            }
        });
    }

    private void setUpWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
            getWindow().setExitTransition(slide);
        }
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
//                btnCouncilor.setEnabled(false);
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
//                btnMayor.setEnabled(false);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void exitAPP(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Controller.getInstance().clear();
        finish();
    }
}
