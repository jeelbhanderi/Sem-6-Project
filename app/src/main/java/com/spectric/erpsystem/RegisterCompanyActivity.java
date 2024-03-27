package com.spectric.erpsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Company.CompanyRegisterModel;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.jar.Attributes;

public class RegisterCompanyActivity extends AppCompatActivity {

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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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



        // Open gallery to select image
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
                if(validateInputs()) {
                    mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String PushID = mAuth.getInstance().getCurrentUser().getUid();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterCompanyActivity.this, "email is sent to your email", Toast.LENGTH_SHORT).show();
                                        companyRegisterModel = new CompanyRegisterModel(ComapnyName, Mobile, Address, Email, Website, GstNo, HscSac, CompanyPan, Status, Role, Password);
                                        mdatabasereference.child("All Data").child("Company").child(PushID).setValue(companyRegisterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                mdatabasereference.child("All Data").child("Users").child(PushID).setValue(companyRegisterModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Intent intent = new Intent(RegisterCompanyActivity.this, CompanyLoginActivity.class);
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

    private boolean validateInputs() {
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

        if (TextUtils.isEmpty(Email)
                || TextUtils.isEmpty(Password)
                || TextUtils.isEmpty(Address)
                || TextUtils.isEmpty(Mobile)
                || TextUtils.isEmpty(ComapnyName)
                || TextUtils.isEmpty(GstNo)
                || TextUtils.isEmpty(Website)
                || TextUtils.isEmpty(CompanyPan)
                || TextUtils.isEmpty(HscSac)) {
            Toast.makeText(RegisterCompanyActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(Email)) {
            Toast.makeText(RegisterCompanyActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidGST(GstNo)) {
            Toast.makeText(RegisterCompanyActivity.this, "Invalid GST format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPhoneNumber(Mobile)) {
            Toast.makeText(RegisterCompanyActivity.this, "Invalid Mobile", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Add more validation checks as needed
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidGST(String gst) {
        // GSTIN format: 2 characters for state code, 10 characters for PAN, 1 character for entity type,
        // 1 character for check sum digit
        String gstPattern = "^[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}Z[0-9]{1}$";
        return gst.matches(gstPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Phone number format: 10 digits
        String phonePattern = "^\\d{10}$";
        return phoneNumber.matches(phonePattern);
    }

    }



