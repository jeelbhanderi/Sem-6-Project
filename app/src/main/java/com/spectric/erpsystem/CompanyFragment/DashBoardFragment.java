package com.spectric.erpsystem.CompanyFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.ProjectDetailsAdapter;
import com.spectric.erpsystem.Models.ProjectDetails;
import com.spectric.erpsystem.Models.ProjectDetsilsFetch;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashBoardFragment extends Fragment {

    private RecyclerView recyclerViewClientProgress, recyclerViewInternProgress;
    private CardView cardViewTotalClient, cardViewTotalIntern;
    private DatabaseReference mdatabase, InternData;
    private ProjectDetsilsFetch clientprojectDetails;
    private List<ProjectDetsilsFetch> clientProjectList;
    private List<ProjectDetsilsFetch> internProjectList;
    private Button AddProject;
    private FirebaseAuth mAuth;
    TextView textViewincome,texttotalintern;

    private Context mContext; // Store the context here


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context; // Store the context when the fragment is attached
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        mAuth = FirebaseAuth.getInstance();
        textViewincome =view.findViewById(R.id.total_income);
        texttotalintern= view.findViewById(R.id.total_expenses);
        // Initialize UI elements
        recyclerViewClientProgress = view.findViewById(R.id.recyclerViewClientProgress);
        recyclerViewInternProgress = view.findViewById(R.id.recyclerViewInternProgress);
        cardViewTotalClient = view.findViewById(R.id.cardViewTotalClient);
        cardViewTotalIntern = view.findViewById(R.id.cardViewTotalIntern);
        AddProject = view.findViewById(R.id.addProject);

        AddProject.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                showAddProjectDialog();
            }
        });

        fetchDataFromFirebase();
        countTotalClients();
        countTotalInterns();
        return view;
    }

    private void showAddProjectDialog() {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.project_add_dialog, null);
        EditText etHandlerEMail = view1.findViewById(R.id.Add_Project_HandlerEmail);
        EditText etclientEmail = view1.findViewById(R.id.Add_Project_owner_email);
        EditText etdescription = view1.findViewById(R.id.Add_Project_Description);
        EditText etprojectName = view1.findViewById(R.id.Add_Project_Name);
        RadioGroup etrole = view1.findViewById(R.id.type);
        EditText etprice = view1.findViewById(R.id.Add_Project_total_price);
        EditText etadvancePayment = view1.findViewById(R.id.Add_Project_AdvancePayMent);
        EditText etRemainingPayment = view1.findViewById(R.id.Add_Project_RemainingPayment);
        EditText etsubmissionDate = view1.findViewById(R.id.Add_Project_submition_Date);
        EditText etfield = view1.findViewById(R.id.Add_Project_Field);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view1).setPositiveButton("Add",null).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog myDialog = builder.create();
        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button PositiveBtn = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                PositiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ProjectName = etprojectName.getText().toString();
                        String ownerEmail = etclientEmail.getText().toString();
                        String ProjectDescription = etdescription.getText().toString();
                        String TotalPrice = etprice.getText().toString();
                        String AdvancePayment = etadvancePayment.getText().toString();
                        String RemainingPayment = etRemainingPayment.getText().toString();
                        String Status = "pending";
                        String SubmitionDate = etsubmissionDate.getText().toString();
                        String Progress = "0";
                        String field = etfield.getText().toString();
                        String OwnerType = getSelectedRadioButtonText(etrole);
                        String HandlerEmail = etHandlerEMail.getText().toString();

                        if (OwnerType != null) {
                            if (ownerEmail.isEmpty() || ProjectName.isEmpty() || ProjectDescription.isEmpty() || TotalPrice.isEmpty() || AdvancePayment.isEmpty() || RemainingPayment.isEmpty() || field.isEmpty() || SubmitionDate.isEmpty() || HandlerEmail.isEmpty()) {
                                Toast.makeText(mContext, "Please Fill All the Field", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!isValidEmail(ownerEmail)){
                                etclientEmail.setError("Invalid Email");
                                return;
                            }
                            if(!isValidEmail(HandlerEmail)){
                                etHandlerEMail.setError("Invalid Email");
                                return;
                            }
                            if (!isValidDate(SubmitionDate)) {
                                etsubmissionDate.setError("Invalid DAte");
                                return;
                            }
                            if (AdvancePayment.isEmpty() || RemainingPayment.isEmpty() || TotalPrice.isEmpty()) {
                                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                                return; // Exit the method
                            }

                            // Convert the string values to double for comparison
                            double advancePayment = Double.parseDouble(AdvancePayment);
                            double remainingPayment = Double.parseDouble(RemainingPayment);
                            double totalPrice = Double.parseDouble(TotalPrice);

                            // Check if the sum of advance payment and remaining payment exceeds the total price
                            if (advancePayment + remainingPayment != totalPrice) {
                                // Show error message
                                Toast.makeText(getActivity(), "Addition of Advance payment and remaining payment Should be equal to total price", Toast.LENGTH_SHORT).show();
                                return; // Exit the method
                            }

                            if(isValidData(ownerEmail,ProjectName,ProjectDescription,TotalPrice,AdvancePayment,RemainingPayment,field,SubmitionDate,HandlerEmail)){
                                ProjectDetails projectDetails = new ProjectDetails(ProjectName, ownerEmail, ProjectDescription, TotalPrice, AdvancePayment, RemainingPayment, Status, SubmitionDate, Progress, field, OwnerType, HandlerEmail);
                                saveProjectToDatabase(projectDetails);
                                dialog.dismiss();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Please select a role (Client or Intern)", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        });
        myDialog.show();

    }

    private String getSelectedRadioButtonText(RadioGroup radioGroup) {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = radioGroup.findViewById(selectedRadioButtonId);
            if (selectedRadioButton != null) {
                return selectedRadioButton.getText().toString();
            }
        }
        return null;
    }

    private void saveProjectToDatabase(ProjectDetails projectDetails) {
        String email = projectDetails.getOwnerEmail();
        String id = email.replace(".","dot");

        String pushId = mAuth.getCurrentUser().getUid();

        // Save the project under the appropriate owner type node
        mdatabase.child("All Data").child("Projects").child(pushId).child(id).setValue(projectDetails);
    }


    private void fetchDataFromFirebase() {
        // Initialize the lists
        clientProjectList = new ArrayList<>();
        internProjectList = new ArrayList<>();

        // Initialize mdatabase
        mdatabase = FirebaseDatabase.getInstance().getReference();

        String PushId = mAuth.getCurrentUser().getUid();

        // Fetch client projects
        // Inside fetchDataFromFirebase()

// Fetch intern projects
        InternData = FirebaseDatabase.getInstance().getReference();
        InternData.child("All Data").child("Projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                internProjectList.clear(); // Clear existing data
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : datasnapshot.getChildren()) {
                        ProjectDetsilsFetch projectDetails = dataSnapshot.getValue(ProjectDetsilsFetch.class);
                        if ("Intern".equals(projectDetails.getOwnerType())) {
                            internProjectList.add(projectDetails);
                        }
                    }
                }
                setRecyclerViewAdapter(recyclerViewInternProgress, internProjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

// Fetch client projects
        mdatabase.child("All Data").child("Projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clientProjectList.clear(); // Clear existing data
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot : datasnapshot.getChildren()) {
                        ProjectDetsilsFetch projectDetails = dataSnapshot.getValue(ProjectDetsilsFetch.class);
                        if ("Client".equals(projectDetails.getOwnerType())) {
                            clientProjectList.add(projectDetails);
                        }
                    }
                }
                setRecyclerViewAdapter(recyclerViewClientProgress, clientProjectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void setRecyclerViewAdapter(RecyclerView recyclerView, List<ProjectDetsilsFetch> projectDetailsList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false); // Use stored context here
        recyclerView.setLayoutManager(layoutManager);
        String companyPushID = mAuth.getCurrentUser().getUid(); // Get the PushID of the company

        ProjectDetailsAdapter adapter = new ProjectDetailsAdapter(projectDetailsList, mContext, companyPushID); // Use stored context here
        recyclerView.setAdapter(adapter);
    }

    private void countTotalClients() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String PushId = mAuth.getCurrentUser().getUid();

        databaseReference.child("All Data").child("Projects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalClients = 0;

                // Iterate through each project
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapshot : projectSnapshot.getChildren()) {
                        // Get the client project details
                        ProjectDetsilsFetch projectDetails = childSnapshot.getValue(ProjectDetsilsFetch.class);
                        if (projectDetails != null && "Client".equals(projectDetails.getOwnerType())) {
                            // Increment the count if the project belongs to a client
                            totalClients++;
                        }
                    }
                }
                Log.d("TotalClients", "Total number of clients: " + totalClients);
                textViewincome.setText(String.valueOf(totalClients));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("CountTotalClients", "Failed to count total clients: " + databaseError.getMessage());
            }
        });
    }

    private void countTotalInterns() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String PushId = mAuth.getCurrentUser().getUid();

        databaseReference.child("All Data").child("Projects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalInterns = 0;

                // Iterate through each project
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapshot : projectSnapshot.getChildren()) {
                        // Get the intern project details
                        ProjectDetsilsFetch projectDetails = childSnapshot.getValue(ProjectDetsilsFetch.class);
                        if (projectDetails != null && "Intern".equals(projectDetails.getOwnerType())) {
                            // Increment the count if the project belongs to an intern
                            totalInterns++;
                        }
                    }
                }

                // Update UI or perform any action with the total intern count
                Log.d("TotalInterns", "Total number of interns: " + totalInterns);
                // For example, you can set the count to a TextView
                texttotalintern.setText(String.valueOf(totalInterns));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("CountTotalInterns", "Failed to count total interns: " + databaseError.getMessage());
            }
        });
    }
    private boolean isValidData(String email, String projectName, String projectDescription, String totalPrice, String advancePayment, String remainingPayment, String language, String submissionDate, String projectManagerEmail) {
        if (email.isEmpty() || projectName.isEmpty() || projectDescription.isEmpty() || totalPrice.isEmpty() || advancePayment.isEmpty() || remainingPayment.isEmpty() || language.isEmpty() || submissionDate.isEmpty() || projectManagerEmail.isEmpty()) {
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
    private boolean isValidNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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




}
