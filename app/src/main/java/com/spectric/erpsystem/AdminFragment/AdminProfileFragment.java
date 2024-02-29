package com.spectric.erpsystem.AdminFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.R;

public class AdminProfileFragment extends Fragment {
    private EditText etName, etEmail, etMobile, etAddress;
    private Button btnEditProfile;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        etName = view.findViewById(R.id.ProfileetName);
        etEmail = view.findViewById(R.id.ProfileetEmail);
        etMobile = view.findViewById(R.id.profileetMobile);
        etAddress = view.findViewById(R.id.Profile_etAddress);
        btnEditProfile = view.findViewById(R.id.admin_edit_Profile_btn);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("All Data").child("User").child(currentUser.getUid());


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("comapnyName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String mobile = dataSnapshot.child("mobile").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);

                    // Populate EditText fields with user data
                    etName.setText(name);
                    etEmail.setText(email);
                    etEmail.setEnabled(false);
                    etMobile.setText(mobile);
                    etAddress.setText(address);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = etName.getText().toString();
                String newEmail = etEmail.getText().toString();
                String newMobile = etMobile.getText().toString();
                String newAddress = etAddress.getText().toString();

                DatabaseReference currentUserRef = userRef;
                currentUserRef.child("comapnyName").setValue(companyName);
                currentUserRef.child("email").setValue(newEmail);
                currentUserRef.child("mobile").setValue(newMobile);
                currentUserRef.child("address").setValue(newAddress);
            }
        });





        return view;
    }
}