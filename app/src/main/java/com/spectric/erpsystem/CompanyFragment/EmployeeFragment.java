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
import com.spectric.erpsystem.Adapter.EmployeefetchAdater;
import com.spectric.erpsystem.Models.Client.ClientFetchModel;
import com.spectric.erpsystem.Models.Employee.EmployeeAddModel;
import com.spectric.erpsystem.Models.Employee.EmployeeFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class EmployeeFragment extends Fragment {

    Button NavAddEmploye;
    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<ClientFetchModel> clientFetchModelList;

    EditText etName,etEmail,etAdderess,etMobile,etsalary,etjoiningdate;

    String Name,email,address,mobile,salary,joiningdate,password;
    DatabaseReference mDatabaseAdd;
    FirebaseAuth mAuth;
    EmployeeAddModel employeeAddModel;
    EmployeefetchAdater adapter;
    List<EmployeeFetchModel> employeeFetchModelList;

    public EmployeeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        View AddView = inflater.inflate(R.layout.employee_add_dialog, container, false);
        employeeFetchModelList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.fetch_employee_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        etName = AddView.findViewById(R.id.add_employee_etName);
        etAdderess = AddView.findViewById(R.id.add_employee_etAddress);
        etEmail = AddView.findViewById(R.id.add_employee_etEmail);
        etMobile = AddView.findViewById(R.id.add_employee_etMobile);
        etsalary = AddView.findViewById(R.id.add_employee_etsalary);
        etjoiningdate = AddView.findViewById(R.id.add_employee_etJoiningdate);

        NavAddEmploye = view.findViewById(R.id.nav_add_employee);
        //Add
        NavAddEmploye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(AddView)
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
                                Name = etName.getText().toString();
                                address = etAdderess.getText().toString();
                                email = etEmail.getText().toString();
                                mobile = etMobile.getText().toString();
                                salary = etsalary.getText().toString();
                                joiningdate = etjoiningdate.getText().toString();
                                String Status = "Active";
                                String Role = "Employee";
                                String id = email.replace(".","dot");
                                mAuth = FirebaseAuth.getInstance();
                                String PushID = mAuth.getCurrentUser().getUid();

                                if (validateEmployee(Name, address, email, mobile, joiningdate, salary)) {
                                    // Employee data is valid, proceed to add to Firebase
                                    String pushID = mAuth.getCurrentUser().getUid();
                                    String Id = email.replace(".", "dot");
                                    employeeAddModel = new EmployeeAddModel(Name, email, address, mobile, salary, joiningdate, "Active", "Employee", pushID);
                                    mdatabase.child("All Data").child("employee").child(pushID).child(Id).setValue(employeeAddModel);
                                    // Clear input fields
                                    clearInputFields();
                                    dialog.dismiss();

                                }
                            }
                        });
                    }
                });

                alertDialog.show();

            }
        });
        String PushID = mAuth.getInstance().getCurrentUser().getUid();
        //Fetch Data
        mdatabase.child("All Data").child("employee").child(PushID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeFetchModelList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        // Assuming each child under "employee" node represents an employee
                        EmployeeFetchModel employeeFetchModel = userSnapshot.getValue(EmployeeFetchModel.class);
                        if (employeeFetchModel != null) {
                            employeeFetchModelList.add(employeeFetchModel);
                        }
                    adapter = new EmployeefetchAdater(getActivity(), employeeFetchModelList, PushID);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Function to validate Indian mobile number format
    private boolean isValidMobileNumber(String mobile) {
        // Indian mobile number should start with 7, 8, or 9 and have a total of 10 digits
        return Pattern.matches("[789]\\d{9}", mobile);
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
    private boolean validateEmployee(String name, String address, String email, String mobile, String joiningDate, String salary) {
        if (name.isEmpty()) {
            etName.setError("Please enter a name");
            return false;
        }
        if (address.isEmpty()) {
            etAdderess.setError("Please enter an address");
            return false;
        }
        if (!isValidEmail(email)) {
            etEmail.setError("Please enter a valid email address");
            return false;
        }
        if (!isValidMobileNumber(mobile)) {
            etMobile.setError("Please enter a valid mobile number");
            return false;
        }
        if (!isValidDate(joiningDate)) {
            etjoiningdate.setError("Please enter a valid date");
            return false;
        }
        if (salary.isEmpty()) {
            etsalary.setError("Please enter a salary");
            return false;
        }

        // If all validations pass, return true
        return true;
    }
    private void clearInputFields() {
        etName.setText("");
        etAdderess.setText("");
        etEmail.setText("");
        etMobile.setText("");
        etsalary.setText("");
        etjoiningdate.setText("");
    }
}