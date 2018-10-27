package com.example.calebe.im_up_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownloadTask extends AsyncTask<String, String, String> {

    private String fileName;
    private String folder;
    private Context context;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialog = null;
    private ListView listView = null;
    private ImagesAdapter imagesAdapter = null;
    private ArrayList<File> images_stored = null;
    private String response;

    public DownloadTask(Context context, ListView listView, ImagesAdapter imagesAdapter, ArrayList<File> images_stored)
    {

        this.context = context;
        this.listView = listView;
        this.progressDialog = new ProgressDialog(this.context);
        this.imagesAdapter = imagesAdapter;
        this.images_stored = images_stored;
        dialog = new AlertDialog.Builder(context);

    }

    private void populateList()
    {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
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
        this.imagesAdapter = new ImagesAdapter(context, this.images_stored);
        this.listView.setAdapter(this.imagesAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d("OnClickListView", ""+images_stored.get(position).getName());

                //Controller.getInstance().setImageClicked(images_stored.get(position));
                Intent intent = new Intent(context, ImageClicked.class);
                context.startActivity(intent);
            }
        });
    }
    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        this.progressDialog.setTitle("DOWNLOAD");
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();

        dialog.setTitle("Erro !");
        dialog.setMessage("Falha ao realizar o Download da Imagem");
        dialog.setCancelable(true);
        dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {

        int count;
        try
        {
            Log.d("URL", f_url[0]);
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

            //Extract file name from URL
            fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

            //Append timestamp to file name
            fileName =  fileName;

            //External directory path to save file
            folder = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();

            File directory = new File(folder);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(folder +"/"+ "Downloaded: "+fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                long progress = ((total * 100) / lengthOfFile);
                publishProgress("" + (int) progress);
                progressDialog.setProgress((int)progress);
                if(progress == 100)
                {
//                    progressDialog.hide();
                }
                Log.d("Download", "Progress: " + (int) ((total * 100) / lengthOfFile));

                output.write(data, 0, count);
            }

            output.flush();

            output.close();
            input.close();
            response = "OK";
            return "Downloaded at: " + folder + fileName;

        } catch (Exception e) {
            response = "Error";
            Log.e("Error: ", e.getMessage());
        }

        return "Something went wrong";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress)
    {

    }

    @Override
    protected void onPostExecute(String message)
    {
        if(response.contains("Error"))
        {
            dialog.show();
        }
        progressDialog.hide();
        populateList();
    }
}
