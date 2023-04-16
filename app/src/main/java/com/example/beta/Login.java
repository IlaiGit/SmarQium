package com.example.beta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author		Ilai Shimoni <ilaigithub@gmail.com>
 * @version	    2.2
 * @since		6/11/22
 *  this class makes sure an account is registered before continuing and shows his data accordingly
 */

public class Login extends AppCompatActivity {

    EditText email, password;
    ProgressBar progress_horizontal;
    private Boolean saveLogin;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress_horizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }
    /**
     * this function occurs when "Login" Button is pressed
     * the function checks credentials, if account is verified and if details are correct/account exists and acts accordingly
     */
    public void SignIn(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

        String EMAIL = email.getText().toString().trim(); //end spaces not included
        String PASSWORD = password.getText().toString().trim(); //end spaces not included

        //check validity
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

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("email", EMAIL);
            loginPrefsEditor.putString("password", PASSWORD);
            loginPrefsEditor.commit();
        }
        else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        progress_horizontal.setVisibility(View.VISIBLE);
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
                        // email exists but not verified yet, user needs to verify through email
                        Toast.makeText(Login.this, "Verify account through email", Toast.LENGTH_LONG).show();
                        progress_horizontal.setVisibility(View.INVISIBLE);
                    }

                }
                else{
                    Toast.makeText(Login.this, "Failed to log in, check credentials", Toast.LENGTH_LONG).show();
                    progress_horizontal.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * this function occurs when "Forgot Password" Button is pressed
     * the function redirects user to recovery activity and saves written email if exists
     */
    public void forgot (View view) {
        Intent intent = new Intent(this, com.example.beta.ForgotPassword.class);
        intent.putExtra("RecMail", email.getText().toString().trim());
        startActivity(intent);
    }

    /**
     * this function occurs when "Register" Button is pressed
     * the function redirects user to registry activity and saves credentials
     */
    public void Register(View view) {
        Intent intent = new Intent(this, com.example.beta.Register.class);
        intent.putExtra("RegistryMail", email.getText().toString().trim());
        intent.putExtra("RegistryPassword", password.getText().toString().trim());
        startActivity(intent);
    }

}