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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    Button btn;
    EditText FirstName, LastName, Email, Password;
    ProgressBar prog;

    private FirebaseAuth firebaseAuth;
    ArrayList<String> KIDlist = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        prog = (ProgressBar) findViewById(R.id.prog);
        btn = (Button) findViewById(R.id.btn);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));
        FirstName = (EditText) findViewById(R.id.FirstName);
        LastName = (EditText) findViewById(R.id.LastName);
        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);

    }

    public void CreateAccount(View view) {
        String firstName = FirstName.getText().toString().trim();
        if(firstName.isEmpty()){
            FirstName.setError("Please enter your first name!");
            FirstName.requestFocus();
            return;
        }
        String lastName = LastName.getText().toString().trim();
        if(lastName.isEmpty()){
            LastName.setError("Please enter your last name!");
            LastName.requestFocus();
            return;
        }
        String email = Email.getText().toString().trim();
        if(email.isEmpty()){
            Email.setError("Please enter your email!");
            Email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please provide a valid email!");
            Email.requestFocus();
            return;
        }
        String password = Password.getText().toString().trim();
        if(password.isEmpty()){
            Password.setError("Please enter your password!");
            Password.requestFocus();
            return;
        }
        if(password.length() < 6){
            Password.setError("Password must contain at least 6 characters");
            Password.requestFocus();
            return;
        }

        prog.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserInfo user = new UserInfo(firstName, lastName, KIDlist , FirebaseAuth.getInstance().getCurrentUser().getUid());

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                prog.setVisibility(View.INVISIBLE);

                                                // redirect
                                                Intent intent = new Intent(Register.this, Login.class);
                                                intent.putExtra("Email", email);
                                                intent.putExtra("Password", password);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(Register.this, "Failed to register, try again", Toast.LENGTH_LONG).show();
                                                prog.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this, "Failed to register, try again", Toast.LENGTH_LONG).show();
                            prog.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    public void redirect(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }
}