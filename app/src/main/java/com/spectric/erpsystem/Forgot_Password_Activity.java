package com.spectric.erpsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {
private Button Forgetbtn;
private EditText etEmail;
private String Email;
private FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth =FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.recoveryEmail);
        Forgetbtn = findViewById(R.id.ForgetBtn);
        Forgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        Email = etEmail.getText().toString();
        if(Email.isEmpty()){
            etEmail.setError("is required");
        }
        else{
            forgotpass();
        }
    }

    private void forgotpass() {
        auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot_Password_Activity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Forgot_Password_Activity.this,CompanyLoginActivity.class);
                            startActivity(intent);
                        }
                    }, 3000);
                    finish();
                }
                else{
                    Toast.makeText(Forgot_Password_Activity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}