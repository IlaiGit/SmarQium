package com.example.beta;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author		Ilai Shimoni <ilaigithub@gmail.com>
 * @version	    2.2
 * @since		6/11/22
 *  this class allows a user who forgot his password to recover it by authentication through email provided during registration
 */

public class ForgotPassword extends AppCompatActivity {
    Button RecoveryMail;
    EditText recovery;
    ProgressBar Recover_prog;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String RecMail = intent.getStringExtra("RecMail");
        recovery.setText(RecMail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        RecoveryMail = (Button) findViewById(R.id.RecoveryMail);
        RecoveryMail.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2E3545")));

        recovery = (EditText) findViewById(R.id.recovery);
        Recover_prog = (ProgressBar) findViewById(R.id.ProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    public void recover(View view) {
        String email = recovery.getText().toString().trim();
        if(email.isEmpty()){
            recovery.setError("Email is required!");
            recovery.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            recovery.setError("Please provide valid email!");
            recovery.requestFocus();
            return;
        }
        Recover_prog.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel("EmailRecovery", "Email recovery", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ForgotPassword.this, "EmailRecovery");
                    builder.setContentTitle("Smarqium");
                    builder.setContentText("Please check your email and change your password ! ");
                    builder.setSmallIcon(R.drawable.ic_noti_icon);
                    builder.setAutoCancel(true);

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, builder.build());
                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                    intent.putExtra("LogMail", recovery.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Something wrong happened", Toast.LENGTH_SHORT).show();
                }
                Recover_prog.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void redirect(View view) {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        intent.putExtra("LogMail", recovery.getText().toString());
        startActivity(intent);
    }
}