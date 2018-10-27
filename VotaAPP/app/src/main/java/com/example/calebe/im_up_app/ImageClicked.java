package com.example.calebe.im_up_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageClicked extends AppCompatActivity
{
    private File image = null;
    private TextView imageName = null;
    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_clicked);
        this.imageName = findViewById(R.id.imageName);
        this.imageView = findViewById(R.id.imageView);

        //this.image = Controller.getInstance().getImageClicked();
        try
        {
            String DateName[] = this.image.getName().split("_-_");
            this.imageName.setText(DateName[0].replace("_", " "));

//            Log.d("FileName", "DateName lenght: "+DateName.length+"  DateName[0]: "+DateName[0]);
//            String Name[] = DateName[1].split("\\.");
//            Log.d("FileName", "Name lenght: "+Name.length+" Name[0]: "+Name[0]);

//            this.imageName.setText(Name[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.imageName.setText(this.image.getName());
        }

        Glide.with(this).load(this.image).into(this.imageView);
    }

    public void returnToMain(View view)
    {
        //Controller.getInstance().setImageClicked(null);
        finish();
    }

    public void doImageUpload(View view)
    {
        //(new CallAPI(ImageClicked.this)).execute(Controller.getInstance().getImageClicked().getAbsolutePath());
    }
}
