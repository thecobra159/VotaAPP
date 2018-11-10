package com.example.thecobra.votaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;

public class ImagesAdapter extends ArrayAdapter<JSONObject> {
    private ArrayList<JSONObject> images;
    private Context context;

    public ImagesAdapter(Context context, ArrayList<JSONObject> data) {
        super(context, R.layout.list_cell, data);
        this.images = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        JSONObject candidate = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell, parent, false);
        }

        ImageView listview_image = (ImageView) convertView.findViewById(R.id.imgCandidate);
        TextView candidate_name = (TextView) convertView.findViewById(R.id.txtNameCadidate);
        TextView candidate_party = (TextView) convertView.findViewById(R.id.txtPartyCandidate);

        try
        {
            Glide.with(convertView).load(candidate.getString("foto")).into(listview_image);
            candidate_name.setText(candidate.getString("nome"));
            candidate_party.setText(candidate.getString("partido"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }
}