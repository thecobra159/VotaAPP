package com.example.calebe.im_up_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath = null;

    private ListView listViewMyItems = null;
    private File photoFile = null;
    private  Uri photoURI = null;
    private ImagesAdapter imagesAdapter = null;
    private ArrayList<File> images_stored = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        this.images_stored = new ArrayList<>();

        if(Controller.getInstance().getAuthURL() == null)
        {
            Intent urlIntent = new Intent(this, InitialScreen.class);
            startActivity(urlIntent);
        }

        populateList();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void populateList()
    {
        String path = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        this.images_stored.clear();
        for (int i = 0; i < files.length; i++)
        {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath(),bmOptions);
            if(bitmap == null)
            {
                files[i].delete();
            }
            else
            {
                this.images_stored.add(files[i]);
            }
            bitmap = null;
        }
        listViewMyItems = (ListView) findViewById(R.id.listViewMyItems);
        this.imagesAdapter = new ImagesAdapter(this, this.images_stored);
        listViewMyItems.setAdapter(this.imagesAdapter);
        listViewMyItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("OnClickListView", ""+images_stored.get(position).getName());

                Controller.getInstance().setImageClicked(images_stored.get(position));
                Intent intent = new Intent(getApplicationContext(), ImageClicked.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    public void uploadImage(View view)
    {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle("Nome da Imagem").setCancelable(false);
        final EditText input = new EditText(MainActivity.this);

        input.setHint("Insira um nome para a Imagem");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( !input.getText().toString().isEmpty() )
                {
                    if (cameraIntent.resolveActivity(getPackageManager()) != null)
                    {
                        try
                        {
                            photoFile = FileUtils.createFile(MainActivity.this, input.getText().toString(), ".jpg");
                            mCurrentPhotoPath = photoFile.getAbsolutePath();
                            photoURI = FileProvider.getUriForFile(MainActivity.this, "com.example.calebe.im_up_app.fileprovider", photoFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            try
            {
                (new CallAPI(MainActivity.this)).execute(mCurrentPhotoPath);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }else if(resultCode == RESULT_CANCELED)
        {
            try
            {
                mCurrentPhotoPath = null;
//                this.photoFile = new File(mCurrentPhotoPath);
                this.photoFile.delete();
                this.photoFile = null;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == 0)
        {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.d("QR CODE", contents);
                (new DownloadTask( this, this.listViewMyItems, this.imagesAdapter, this.images_stored)).execute(contents);

            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            this.photoFile = new File(mCurrentPhotoPath);
            this.photoFile.delete();
            this.photoFile = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openBarCodeScanner()
    {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }
    }

    private void openZXing()
    {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(intent);
    }

    public void downloadImage(View view)
    {
        openBarCodeScanner();
//        openZXing(w);
    }

    public AlertDialog showDialog(Context ctx, String title, String msg, String btn1, String btn2)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setTitle(title).setCancelable(false);
        final EditText input = new EditText(MainActivity.this);

        input.setHint(msg);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        return alert;
    }
}

