package com.spectric.erpsystem.AdminFragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spectric.erpsystem.R;

import javax.xml.transform.Result;

public class AdminProfileFragment extends Fragment {
    private EditText etName, etEmail, etMobile, etAddress;
    private Button btnEditProfile;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    ActivityResultLauncher<String> launcher;
     FirebaseStorage storage;
     StorageReference storageRef;

    DatabaseReference databaseReference;
    ImageView UserDp;
    FirebaseAuth mAuth;
    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        mAuth = FirebaseAuth.getInstance();

        etName = view.findViewById(R.id.ProfileetName);
        etEmail = view.findViewById(R.id.ProfileetEmail);
        etMobile = view.findViewById(R.id.profileetMobile);
        etAddress = view.findViewById(R.id.Profile_etAddress);
        btnEditProfile = view.findViewById(R.id.admin_edit_Profile_btn);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        UserDp = view.findViewById(R.id.AdminUserDp);
        UserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (isAdded()) { // Check if fragment is added to activity
                    String PushID = mAuth.getInstance().getCurrentUser().getUid();
                    UserDp.setImageURI(result);

                    storageRef = storage.getReference().child("images").child(PushID);

                    storageRef.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (isAdded()) { // Check if fragment is added to activity
                                Toast.makeText(requireActivity(), "Images Upated", Toast.LENGTH_SHORT).show();
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        if (isAdded()) { // Check if fragment is added to activity
                                            databaseReference.child("All Data").child("Images").child(PushID).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if (isAdded()) { // Check if fragment is added to activity
                                                        Toast.makeText(requireActivity(), "firebase Success", Toast.LENGTH_SHORT).show();
                                                        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child("images").child(PushID);
                                                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                if (isAdded()) { // Check if fragment is added to activity
                                                                    // Load and display the profile image using Glide
                                                                    Glide.with(requireContext()).load(uri).into(UserDp);
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                if (isAdded()) { // Check if fragment is added to activity
                                                                    // Handle failure to retrieve profile image
                                                                    // For example, load a default placeholder image
                                                                    Glide.with(requireContext()).load(R.drawable.default_profile).into(UserDp);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (isAdded()) { // Check if fragment is added to activity
                                Toast.makeText(requireActivity(), "Images Not Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        userRef = FirebaseDatabase.getInstance().getReference().child("All Data").child("Users").child(currentUser.getUid());


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
            // Inside onCreateView() method, after mdatabase initialization


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