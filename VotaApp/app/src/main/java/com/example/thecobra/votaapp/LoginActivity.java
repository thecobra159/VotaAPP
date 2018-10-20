package com.example.thecobra.votaapp;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * A login screen that offers login via auth/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText textAuth, textPass;
    private String auth, pass;
    private Button btnLogin;
    private ProgressBar loginBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textAuth = findViewById(R.id.editTextAuth);
        textPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loginBar = findViewById(R.id.loginProgress);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = textAuth.getText().toString();
                pass = textPass.getText().toString();

                if (isAuthValid(auth) && isPasswordValid(pass)) {
                    Controller.getInstance().alertMessage(getApplication(), "Realizando busca");
                } else
                    Controller.getInstance().alertMessage(getApplication(), "Auth deve conter 12 chars\nPass deve ser maior que 4 chars");
            }
        });
    }

    private boolean isAuthValid(String email) {
        return email.trim().isEmpty() || email.length() < 12 ? false : true;
    }

    private boolean isPasswordValid(String password) {
        return password.trim().isEmpty() || password.length() < 4 ? false : true;
    }
}

