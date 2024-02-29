package com.spectric.erpsystem.CompanyFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.ClientFetchAdapter;
import com.spectric.erpsystem.Models.Client.ClientAddModel;
import com.spectric.erpsystem.Models.Client.ClientFetchModel;
import com.spectric.erpsystem.Models.Incomemodel;
import com.spectric.erpsystem.Models.ProjectDetails;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ClientsFragment extends Fragment {
    Button NavAddClient;
    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<ClientFetchModel> clientFetchModelList;

    DatabaseReference mDatabaseAdd;
    FirebaseAuth mAuth;
    ClientAddModel clientAddModel;
    ClientFetchAdapter adapter;

    public ClientsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);
        NavAddClient = view.findViewById(R.id.nav_add_client);
        recyclerView = view.findViewById(R.id.fetch_client_recycler);
        mAuth = FirebaseAuth.getInstance();
        String PushID = mAuth.getCurrentUser().getUid();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("clients").child(PushID);

        //Fetch Data
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clientFetchModelList = new ArrayList<>(); // Initialize the list

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ClientFetchModel clientFetchModel = dataSnapshot.getValue(ClientFetchModel.class);
                    clientFetchModelList.add(clientFetchModel);
                }

                // Initialize adapter here
                adapter = new ClientFetchAdapter(getActivity(), clientFetchModelList, PushID);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                if (clientFetchModelList.isEmpty()) {
                    Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify adapter of dataset change
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        //AddData
        View Addview = LayoutInflater.from(getActivity()).inflate(R.layout.client_add_dialog, null);
        mDatabaseAdd = FirebaseDatabase.getInstance().getReference();

        NavAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(Addview)
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText etName, etEmail, etMobile, etProjectName, etProjectDescription,
                                        etTotalPrice, etAdvancePayment, etRemainingPayment, etLanguage,
                                        etSubmissionDate, etProjectManagerEmail;

                                etName = Addview.findViewById(R.id.addclientetName);
                                etEmail = Addview.findViewById(R.id.addclientetEmail);
                                etMobile = Addview.findViewById(R.id.addclientetMobile);
                                etProjectName = Addview.findViewById(R.id.addclientetProjectName);
                                etProjectDescription = Addview.findViewById(R.id.addclientetProjectDescription);
                                etTotalPrice = Addview.findViewById(R.id.addclientetTotalPrice);
                                etAdvancePayment = Addview.findViewById(R.id.addclientetAdvancePayment);
                                etRemainingPayment = Addview.findViewById(R.id.addclientetRemainingPayment);
                                etLanguage = Addview.findViewById(R.id.addclientetLanguage);
                                etSubmissionDate = Addview.findViewById(R.id.addclientetSubmissionDate);
                                etProjectManagerEmail = Addview.findViewById(R.id.addclientetProjectManagerEmail);

                                String name = etName.getText().toString();
                                String email = etEmail.getText().toString();
                                String mobile = etMobile.getText().toString();
                                String projectName = etProjectName.getText().toString();
                                String projectDescription = etProjectDescription.getText().toString();
                                String totalPrice = etTotalPrice.getText().toString();
                                String advancePayment = etAdvancePayment.getText().toString();
                                String remainingPayment = etRemainingPayment.getText().toString();
                                String language = etLanguage.getText().toString();
                                String submissionDate = etSubmissionDate.getText().toString();
                                String projectManagerEmail = etProjectManagerEmail.getText().toString();

                                if (isValidData(name, email, mobile, projectName, projectDescription, totalPrice, advancePayment, remainingPayment, language, submissionDate, projectManagerEmail)) {
                                    // Data is valid, add to database
                                    addClientToDatabase(name, email, mobile, projectName, projectDescription, totalPrice, advancePayment, remainingPayment, language, submissionDate, projectManagerEmail);
                                    alertDialog.dismiss();
                                } else {
                                    // Data is not valid, show error messages
                                    if (name.isEmpty()) {
                                        etName.setError("Empty Name");
                                    }
                                    if (!isValidEmail(email)) {
                                        etEmail.setError("Invalid Email");
                                    }

                                    if (!isValidMobileNumber(mobile)) {
                                        etMobile.setError("Invalid Mobile Number");
                                    }
                                    if (projectName.isEmpty()) {
                                        etProjectName.setError("Invalid Project Name");
                                    }
                                    if (projectDescription.isEmpty()) {
                                        etProjectName.setError("Invalid Project Description");
                                    }
                                    if (!isValidNumeric(advancePayment) || !isValidNumeric(remainingPayment)) {
                                        if(!isValidNumeric(advancePayment)) {
                                            etAdvancePayment.setError("Invalid Advance Amount");
                                        }
                                        else if(!isValidNumeric(remainingPayment)) {
                                            etRemainingPayment.setError("Invalid Remaining Amount");
                                        }
                                    }
                                    if (language.isEmpty()) {
                                        etLanguage.setError("Invalid Project field or Language");
                                    }
                                    if (isValidDate(submissionDate)) {
                                        etSubmissionDate.setError("Invalid Date");
                                    }
                                    if (!isValidEmail(projectManagerEmail)) {
                                        etProjectManagerEmail.setError("Invalid Email");
                                    }
                                    // Add similar checks for other fields...
                                }
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

        return view;
    }

    private boolean isValidData(String name, String email, String mobile, String projectName, String projectDescription, String totalPrice, String advancePayment, String remainingPayment, String language, String submissionDate, String projectManagerEmail) {
        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || projectName.isEmpty() || projectDescription.isEmpty() || totalPrice.isEmpty() || advancePayment.isEmpty() || remainingPayment.isEmpty() || language.isEmpty() || submissionDate.isEmpty() || projectManagerEmail.isEmpty()) {
            return false;
        }
        if (!isValidEmail(email) || !isValidMobileNumber(mobile)) {
            return false;
        }
        if (!isValidDate(submissionDate)) {
            return false;
        }
        if (!isValidNumeric(advancePayment) || !isValidNumeric(remainingPayment)) {
            return false;
        }
        double total = Double.parseDouble(totalPrice);
        double advance = Double.parseDouble(advancePayment);
        double remaining = Double.parseDouble(remainingPayment);
        return advance + remaining <= total;
    }

    private void addClientToDatabase(String name, String email, String mobile, String projectName, String projectDescription, String totalPrice, String advancePayment, String remainingPayment, String language, String submissionDate, String projectManagerEmail) {
        String PushID = mAuth.getCurrentUser().getUid();
        String id = email.replace(".", "dot");

        String Status = "pending";
        String Progress = "0";
        String OwnerType = "Client";

        ClientAddModel clientAddModel = new ClientAddModel(
                name, email, mobile, projectName, projectDescription,
                totalPrice, advancePayment, remainingPayment, language,
                submissionDate, projectManagerEmail, PushID);

        ProjectDetails projectModel = new ProjectDetails(projectName, email, projectDescription,
                totalPrice, advancePayment, remainingPayment, Status,
                submissionDate, Progress, language,
                OwnerType, projectManagerEmail);

        mDatabaseAdd.child("All Data")
                .child("clients")
                .child(PushID)
                .child(id)
                .setValue(clientAddModel);
        mDatabaseAdd.child("All Data")
                .child("Projects")
                .child(PushID)
                .child(id)
                .setValue(projectModel);

        Incomemodel incomeModel = new Incomemodel(
                "Client", email, totalPrice, getCurrentDate(), advancePayment, remainingPayment);

        mDatabaseAdd.child("All Data")
                .child("income")
                .child(PushID)
                .child(id)
                .setValue(incomeModel);
    }

    private boolean isValidNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        sdf.setLenient(false);

        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobileNumber(String mobile) {
        return Pattern.matches("[789]\\d{9}", mobile);
    }
}
