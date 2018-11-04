package com.example.thecobra.votaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class CouncilorActivity extends AppCompatActivity {

    private ListView listViewCouncilors;
    private ArrayAdapter adapter;
    private List<Candidate> listCouncilor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councilor);

        listViewCouncilors = findViewById(R.id.listViewCouncilors);
    }
}
