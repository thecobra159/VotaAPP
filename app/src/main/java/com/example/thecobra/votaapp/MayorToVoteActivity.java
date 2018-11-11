package com.example.thecobra.votaapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

public class MayorToVoteActivity extends AppCompatActivity {
    private ImageView candidateImage = null;
    private TextView candidateName = null;
    private TextView candidateParty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_clicked);

        getSupportActionBar().setTitle("Escolha o Prefeito");
        this.candidateImage = findViewById(R.id.imageView);
        this.candidateName = findViewById(R.id.candidateName);
        this.candidateParty = findViewById(R.id.candidateParty);
        try {
            Glide.with(this).load(Controller.getInstance().getMayorsClicked().getString("foto")).into(this.candidateImage);
            this.candidateName.setText(Controller.getInstance().getMayorsClicked().getString("nome"));
            this.candidateParty.setText(Controller.getInstance().getMayorsClicked().getString("partido"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selectCandidateToVote(View view) {
        Controller.getInstance().setReadyToVote_mayor(true);
        finish();
    }
}
