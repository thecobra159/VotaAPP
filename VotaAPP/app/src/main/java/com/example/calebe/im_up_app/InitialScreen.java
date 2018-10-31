package com.example.calebe.im_up_app;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class InitialScreen extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText editTextAuth, editTextPass;
    private String auth, pass;

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Aguarde ...");
        progressDialog.setMessage("Conectando ao Servidor ...");
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_screen);
        editTextAuth = findViewById(R.id.editTextAuth);
        editTextPass = findViewById(R.id.editTextPass);
    }

    public void connectServer(View view) {
        showProgressDialog();
        auth = editTextAuth.getText().toString();
        pass = editTextPass.getText().toString();
        if (isAuthValid(auth) && isPasswordValid(pass)) {
            Controller.getInstance().setUserAuth(auth);
            Controller.getInstance().setUserPass(pass);
            Controller.getInstance().alertMessage(getApplication(), "Realizando busca");
            if (executeCommand()) {
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        finish();
                    }
                };
            } else {
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        editTextAuth.setBackgroundColor(Color.parseColor("#ff0000"));
                        editTextPass.setBackgroundColor(Color.parseColor("#ff0000"));
                    }
                };
            }
            new GetPostTask(this, Constants.LOGIN).execute(auth, pass);
        } else {
            Controller.getInstance().alertMessage(getApplication(), "Auth deve conter 12 chars\nPass deve ser maior que 4 chars");
        }
    }

    private boolean executeCommand() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + Controller.getInstance().getAuthURL());
            int mExitValue = mIpAddrProcess.waitFor();
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAuthValid(String auth) {
        return auth.trim().isEmpty() || auth.length() != 12 ? false : true;
    }

    private boolean isPasswordValid(String password) {
        return password.trim().isEmpty() || password.length() < 4 ? false : true;
    }
}
