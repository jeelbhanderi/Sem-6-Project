package com.spectric.erpsystem.AdminFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Company.CompanyRegisterModel;
import com.spectric.erpsystem.R;

public class AddComapnyFragment extends Fragment {
EditText etCompanyName,etMobile,etAddress,etEmail,etWebsite,etGstNo,etHscSac,etCompanyPan,etPassowrd;
Button Addbtn;
String ComapnyName,Mobile,Address,Email,Website,Gstno,HscSac,Companypan,Status,Role,Password;
CompanyRegisterModel companyRegisterModel;
DatabaseReference mdatabasereference;
FirebaseAuth mAuth;


    public AddComapnyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_comapny, container, false);

        mdatabasereference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        etCompanyName = view.findViewById(R.id.AddetCompanyname);
        etMobile = view.findViewById(R.id.Addetmobile);
        etAddress = view.findViewById(R.id.AddetAddress);
        etEmail = view.findViewById(R.id.AddetEmail);
        etWebsite = view.findViewById(R.id.AddetWebsite);
        etGstNo = view.findViewById(R.id.AddetGstNo);
        etHscSac = view.findViewById(R.id.AddetHscSac);
        etCompanyPan = view.findViewById(R.id.AddetCompanyPan);
        etPassowrd = view.findViewById(R.id.AddetPassword);
        Addbtn = view.findViewById(R.id.AddComapanyButton);
        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComapnyName = etCompanyName.getText().toString();
                Mobile = etMobile.getText().toString();
                Address = etAddress.getText().toString();
                Email = etEmail.getText().toString();
                Website = etWebsite.getText().toString();
                Gstno= etGstNo.getText().toString();
                HscSac = etHscSac.getText().toString();
                Companypan = etCompanyPan.getText().toString();
                Status = "Active";
                Role = "Company";
                Password = etPassowrd.getText().toString();
                mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String PushID = mAuth.getInstance().getCurrentUser().getUid();
                        companyRegisterModel = new CompanyRegisterModel(ComapnyName,Mobile,Address,Email,Website,Gstno,HscSac,Companypan,Status,Role,Password);
                        mdatabasereference.child("Company").child(PushID).setValue(companyRegisterModel);
                        etCompanyName.setText("");
                        etMobile.setText("");
                        etAddress.setText("");
                        etEmail.setText("");
                        etWebsite.setText("");
                        etGstNo.setText("");
                        etHscSac.setText("");
                        etCompanyPan.setText("");
                        etPassowrd.setText("");


                    }
                });

            }
        });

        return view;
    }
}