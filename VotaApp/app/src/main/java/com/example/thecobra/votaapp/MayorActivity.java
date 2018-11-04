package com.example.thecobra.votaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MayorActivity extends AppCompatActivity {

    private ListView listViewMayors;
    private ArrayAdapter adapter;
    private List<Candidate> listMayor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor);

        listViewMayors = findViewById(R.id.listViewMayors);
    }
}
