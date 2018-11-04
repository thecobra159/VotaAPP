package com.example.thecobra.votaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgCouncilor, imgMayor;
    private Button btnCouncilor, btnMayor, btnVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCouncilor = findViewById(R.id.photoCouncilor);
        btnCouncilor = findViewById(R.id.btnCouncilor);
        imgMayor = findViewById(R.id.photoMayor);
        btnMayor = findViewById(R.id.btnMayor);
        btnVote = findViewById(R.id.btnVote);

    }
}
