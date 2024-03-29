package com.spectric.erpsystem.CompanyFragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spectric.erpsystem.Models.Company.CompanyFetchModel;
import com.spectric.erpsystem.Models.Company.CompanyRegisterModel;
import com.spectric.erpsystem.R;
import com.bumptech.glide.Glide;


public class ManageProfileFragment extends Fragment {

    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    private Button edit;
    ActivityResultLauncher<String> launcher;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    DatabaseReference databaseReference;
    ImageView UserDp;
    // Inside onCreateView() method, after initializing other variables


    private EditText editTextCompanyName, editTextMobile, editTextAddress, editTextEmail,
            editTextWebsite, editTextGstNo, editTextHscSac, editTextCompanyPan,
            editTextStatus, editTextRole, editTextPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_profile_frament, container, false);
        // Inside onCreateView() method, after mdatabase initialization
        storage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        UserDp = view.findViewById(R.id.user_dp);
        UserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                String PushID = mAuth.getInstance().getCurrentUser().getUid();

                storageRef = storage.getReference().child("images").child(PushID);

                storageRef.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(requireActivity(), "Images Upated", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.child("All Data").child("Images").child(PushID).setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(requireActivity(), "firebase Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireActivity(), "Images Not Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
        String PushID = mAuth.getInstance().getCurrentUser().getUid();




        mdatabase.child("All Data").child("Company").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CompanyFetchModel company = snapshot.getValue(CompanyFetchModel.class);
                    if (company != null) {
                        // Populate EditText fields with existing data
                        editTextCompanyName.setText(company.getComapnyName());
                        editTextMobile.setText(company.getMobile());
                        editTextAddress.setText(company.getAddress());
                        editTextEmail.setText(company.getEmail());
                        editTextWebsite.setText(company.getWebsite());
                        editTextGstNo.setText(company.getGstNo());
                        editTextHscSac.setText(company.getHscSac());
                        editTextCompanyPan.setText(company.getCompanyPan());
                        editTextPassword.setText(company.getPassword());
                        // Disable Email and Password EditText fields
                        editTextEmail.setEnabled(false);
                        editTextPassword.setEnabled(false);
                        // Inside onDataChange() method, after populating other fields
                        String userId = mAuth.getCurrentUser().getUid();
                        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child("images").child(userId);
                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Load and display the profile image using Glide
                                Glide.with(requireContext()).load(uri).into(UserDp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to retrieve profile image
                                // For example, load a default placeholder image
                                Glide.with(requireContext()).load(R.drawable.default_profile).into(UserDp);
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

        // Find EditText views
        editTextCompanyName = view.findViewById(R.id.editTextCompanyName);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextWebsite = view.findViewById(R.id.editTextWebsite);
        editTextGstNo = view.findViewById(R.id.editTextGstNo);
        editTextHscSac = view.findViewById(R.id.editTextHscSac);
        editTextCompanyPan = view.findViewById(R.id.editTextCompanyPan);

        editTextPassword = view.findViewById(R.id.editTextPassword);

        editTextCompanyName = view.findViewById(R.id.editTextCompanyName);
        editTextMobile = view.findViewById(R.id.editTextMobile);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextWebsite = view.findViewById(R.id.editTextWebsite);
        editTextGstNo = view.findViewById(R.id.editTextGstNo);
        editTextHscSac = view.findViewById(R.id.editTextHscSac);
        editTextCompanyPan = view.findViewById(R.id.editTextCompanyPan);

        editTextPassword = view.findViewById(R.id.editTextPassword);

        // Set initial values if needed
        // editTextCompanyName.setText("Initial Company Name");
        // editTextMobile.setText("Initial Mobile");

        edit = view.findViewById(R.id.com_Edit_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to perform edit operation
                performEditOperation();
            }
        });

        return view;
    }

    private void performEditOperation() {
        // Retrieve values from EditText fields
        String companyName = editTextCompanyName.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String address = editTextAddress.getText().toString();
        String email = editTextEmail.getText().toString();
        String website = editTextWebsite.getText().toString();
        String gstNo = editTextGstNo.getText().toString();
        String hscSac = editTextHscSac.getText().toString();
        String companyPan = editTextCompanyPan.getText().toString();
        String status = "Active";
        String role = "Company";
        String password = editTextPassword.getText().toString();
        Log.d("EditOperation", "Company Name: " + companyName);
        Log.d("EditOperation", "Mobile: " + mobile);
        if (TextUtils.isEmpty(companyName)
                || TextUtils.isEmpty(mobile)
                || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(website)
                || TextUtils.isEmpty(gstNo)
                || TextUtils.isEmpty(hscSac)
                || TextUtils.isEmpty(companyPan)
                || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }



        if (!isValidGST(gstNo)) {
            Toast.makeText(getActivity(), "Invalid GST format", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPhoneNumber(mobile)){
            Toast.makeText(getActivity(),"Invalid Mobile",Toast.LENGTH_SHORT).show();
            return;
        }
        CompanyRegisterModel registerModel = new CompanyRegisterModel(companyName,mobile,address,email,website,gstNo,hscSac,companyPan,status,role,password);
        String userId = mAuth.getCurrentUser().getUid();
        mdatabase.child("All Data").child("Company").child(userId).setValue(registerModel);
        mdatabase.child("your_node_in_database").child(userId).child("Mobile").setValue(mobile);
        // ... (Set other values)

        // You can also update the UI or show a success message to the user
        Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
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
