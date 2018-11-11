package com.example.thecobra.votaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * A login screen that offers login via auth/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText textAuth, textPass;
    private String auth, pass;
    private Button btnLogin;

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
        setContentView(R.layout.activity_login);
        setupWindowAnimations();

        textAuth = findViewById(R.id.editTextAuth);
        textPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showProgressDialog();
                auth = textAuth.getText().toString();
                pass = textPass.getText().toString();
                if (isAuthValid(auth) && isPasswordValid(pass)) {
                    Controller.getInstance().setUserAuth(auth);
                    Controller.getInstance().setUserPass(pass);
                    // TODO armazenar o resultado do AUTH na controller, verificar posterior mente e matar essa activity.
//                    Controller.getInstance().alertMessage(getApplication(), "Realizando busca");
                    if (executeCommand()) {
                        Controller.getInstance().alertMessage(getApplication(), "Servidor fora do AR! Tente novamente mais tarde!");
                    } else {
                        new GetPostTask(LoginActivity.this, Constants.LOGIN, progressDialog).execute(auth, pass);
                    }
                } else {
                    Controller.getInstance().alertMessage(getApplication(), "Auth deve conter 12 chars\nPass deve ser maior que 4 chars");
                    textAuth.setBackgroundColor(Color.parseColor("#ff0000"));
                    textPass.setBackgroundColor(Color.parseColor("#ff0000"));
                }
            }
        });
    }

    private void setupWindowAnimations() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
            getWindow().setEnterTransition(fade);
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

