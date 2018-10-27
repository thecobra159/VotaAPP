package com.example.calebe.im_up_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.io.File;

public class CallAPI extends AsyncTask<String, Void, Void> {

    private ProgressDialog progressDialog = null;
    private AlertDialog.Builder dialog = null;

    String response = new String();

    private Context context = null;

    public CallAPI(Context context)
    {
        this.context = context;

        this.progressDialog = new ProgressDialog(context);

        this.dialog = new AlertDialog.Builder(context);

        AndroidNetworking.initialize(context);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        this.progressDialog.setTitle("UPLOAD");
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);

        this.progressDialog.show();

        this.dialog.setTitle("Erro !");
        this.dialog.setMessage("Falha ao realizar o UPLOAD da Imagem");
        this.dialog.setCancelable(true);
        this.dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
    }

    @Override
    protected Void doInBackground(String... params) {

        try
        {
            uploadImage(params[0]);
        }
        catch (Exception e)
        {
            response = "Error";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        try
        {
            if(response.contains("Error"))
            {
                this.progressDialog.hide();
                this.dialog.show();
            }
            else if(response.contains("OK")){

                this.progressDialog.hide();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void uploadImage(String file)
    {
        File image = new File(file.toString());
        Log.d("File Upload", image.getName());
        try
        {
            AndroidNetworking.upload(Controller.getInstance().getAuthURL()+"/images/"+image.getName() )
                    .addMultipartFile("image", image)
                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // do anything with progress
                            Log.d("UPLOAD", ""+bytesUploaded+" de "+totalBytes);
                            long progress = ((bytesUploaded*100)/totalBytes);
                            progressDialog.setProgress((int)progress);
                            if(progress == 100)
                            {
                                response = "OK";
                                progressDialog.hide();
                                return ;
                            }
                        }
                    }).getAsOkHttpResponse(new OkHttpResponseListener() {
                @Override
                public void onResponse(okhttp3.Response response1) {
                    Log.d("ServerResponse", ""+response1.toString());
                }

                @Override
                public void onError(ANError anError) {
                    Log.d("ServerANERROR", ""+anError.toString());

                }
            });
        }catch (Exception e)
        {
            response = "Error";
            e.printStackTrace();
        }

    }
}

