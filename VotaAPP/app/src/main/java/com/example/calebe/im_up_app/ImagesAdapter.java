package com.example.calebe.im_up_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ImagesAdapter extends ArrayAdapter<File> {
    private ArrayList<File> images;
    private Context context;

    public ImagesAdapter(Context context, ArrayList<File> data) {
        super(context, R.layout.local_items, data);
        this.images = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        File image = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_items, parent, false);
        }

//        Log.d("ImageView", ""+image.getAbsolutePath());
        ImageView listview_image = (ImageView) convertView.findViewById(R.id.listview_image);
        TextView image_name = (TextView) convertView.findViewById(R.id.image_name);
        TextView image_date = (TextView) convertView.findViewById(R.id.image_date);

        try
        {
            String DateName[] = image.getName().split("_-_");
            image_name.setText(DateName[0].replace("_", " "));

            SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmm", Locale.getDefault() );
            image_date.setText(dateFormat.parse( DateName[1] ).toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            image_name.setText(image.getName());
        }

        try
        {
            Glide.with(convertView).load(image).into(listview_image);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return convertView;
    }
}
