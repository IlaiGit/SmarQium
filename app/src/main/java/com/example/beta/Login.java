package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    Button login, Register, ForgotPassword;
    EditText email, password;
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        Intent intent = getIntent();
        String LogMail = intent.getStringExtra("LogMail");

        if(LogMail != null){
            email.setText(LogMail);
        }
        else{
            String Email = intent.getStringExtra("Email");
            String Password = intent.getStringExtra("Password");

            email.setText(Email);
            password.setText(Password);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * connecting xml elements to java, setting color of custom buttons
         */

        progressBar = (ProgressBar) findViewById(R.id.Recover_prog);
        login = findViewById(R.id.Login);
        login.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        Register = findViewById(R.id.Register);
        Register.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        ForgotPassword = findViewById(R.id.ForgotPassword);
        ForgotPassword.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void SignIn(View view) {
        String EMAIL = email.getText().toString().trim();
        String PASSWORD = password.getText().toString().trim();

        if(EMAIL.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
            email.setError(("Please enter valid email address!"));
            email.requestFocus();
            return;
        }
        if(PASSWORD.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(PASSWORD.length() < 6){
            password.setError("Password must contain at least 6 characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        // redirect
                        startActivity(new Intent(Login.this, AquariumPicker.class));
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Verify account through email", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
                else{
                    Toast.makeText(Login.this, "Failed to log in, check credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void Register(View view) {
        startActivity(new Intent(this, Register.class));
    }

    public void forgot (View view) {
        Intent intent = new Intent(this, com.example.beta.ForgotPassword.class);
        intent.putExtra("RecMail", email.getText().toString().trim());
        startActivity(intent);
    }
}