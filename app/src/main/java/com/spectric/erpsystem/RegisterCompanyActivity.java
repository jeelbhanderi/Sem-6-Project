package com.spectric.erpsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Company.CompanyRegisterModel;

public class RegisterCompanyActivity extends AppCompatActivity {
     FirebaseAuth mAuth;
    EditText etCompanyName,etMobile,etAddress,etEmail,etWebsite,etGstNo,etHscSac,etCompanyPan,etPassowrd;
    Button regbtn;
    String Email,Password,ComapnyName,Mobile,Address,GstNo,Website,CompanyPan,HscSac,Status,Role;
    TextView PageChangetxt;

    DatabaseReference mdatabasereference;
    CompanyRegisterModel companyRegisterModel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mdatabasereference = FirebaseDatabase.getInstance().getReference();


        etCompanyPan= findViewById(R.id.regetCompanyPan);
        etMobile = findViewById(R.id.regetmobile);
        etHscSac = findViewById(R.id.regetHScSac);
        etWebsite =findViewById(R.id.regetWebsite);
        etEmail = findViewById(R.id.regetEmail);
        etPassowrd = findViewById(R.id.regetpassword);
        etAddress  = findViewById(R.id.regetAddress);
        etGstNo = findViewById(R.id.regetgstno);
        etCompanyName = findViewById(R.id.regetCompanyName);
        regbtn = findViewById(R.id.regbutton);
        PageChangetxt = findViewById(R.id.PageChangetxt);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = etEmail.getText().toString();
                Password = etPassowrd.getText().toString();
                Address = etAddress.getText().toString();
                Mobile = etMobile.getText().toString();
                ComapnyName = etCompanyName.getText().toString();
                GstNo = etGstNo.getText().toString();
                Website = etWebsite.getText().toString();
                CompanyPan = etCompanyPan.getText().toString();
                HscSac = etHscSac.getText().toString();
                Status = "Active";
                Role  = "Company";

                mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String PushID = mAuth.getInstance().getCurrentUser().getUid();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterCompanyActivity.this, "email is sent to your email", Toast.LENGTH_SHORT).show();
                                    companyRegisterModel = new CompanyRegisterModel(ComapnyName,Mobile,Address,Email,Website,GstNo,HscSac,CompanyPan,Status,Role,Password);
                                    mdatabasereference.child("All Data").child("Company").child(PushID).setValue(companyRegisterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mdatabasereference.child("All Data").child("User").child(PushID).setValue(companyRegisterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent intent = new Intent(RegisterCompanyActivity.this,CompanyLoginActivity.class);
                                                    startActivity(intent);

                                                }
                                            });

                                        }
                                    });

                                }
                            }
                        });



                    }
                });
            }
        });
        PageChangetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCompanyActivity.this,CompanyLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}