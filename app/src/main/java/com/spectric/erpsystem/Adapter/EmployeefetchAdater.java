package com.spectric.erpsystem.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Employee.EmployeeAddModel;
import com.spectric.erpsystem.Models.Employee.EmployeeFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class EmployeefetchAdater extends RecyclerView.Adapter<EmployeefetchAdater.ViewHolder> {

    private Context context;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    private String mCompanyPushID; // Assign the company's PushID


    public EmployeefetchAdater(Context context, List<EmployeeFetchModel> employeeFetchModelList, String companyPushID) {
        this.context = context;
        this.employeeFetchModelList = employeeFetchModelList;
        this.mAuth = FirebaseAuth.getInstance();
        this.mCompanyPushID = companyPushID; // Assign the company's PushID

    }

    private List<EmployeeFetchModel> employeeFetchModelList;

    @NonNull
    @Override
    public EmployeefetchAdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_data_fetch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeefetchAdater.ViewHolder holder, int position) {
        EmployeeFetchModel employee = employeeFetchModelList.get(position);

        holder.FirstDetail.setText(employee.getName());
        holder.SecondDetail.setText(employee.getEmail());

        // Delete Button Click
        holder.NavEmployeeDelete.setOnClickListener(v -> showDeleteConfirmationDialog(employee.getEmail()));

        // Edit Button Click
        holder.NavEmployeeEdit.setOnClickListener(v -> showEditDialog(employee, holder));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                int postio1 = holder.getAdapterPosition();
                EmployeeFetchModel client = employeeFetchModelList.get(postio1);
                TextView etClientName, etClientMobile, etClientEmail, etClientAddress, etClientStatus, etemployeeRole, etSalary, etJoiningDate;
                View ClientView = LayoutInflater.from(context).inflate(R.layout.employee_fetch_data_card, null);
                etClientName = ClientView.findViewById(R.id.txt_employee_Name1);
                etClientMobile = ClientView.findViewById(R.id.txt_employee_mobile1);
                etClientEmail = ClientView.findViewById(R.id.txt_employee_email);
                etClientAddress = ClientView.findViewById(R.id.txt_employee_address);
                etClientStatus = ClientView.findViewById(R.id.txt_employee_Status);
                etemployeeRole = ClientView.findViewById(R.id.txt_employee_Role);
                etSalary = ClientView.findViewById(R.id.txt_employee_salary);
                etJoiningDate = ClientView.findViewById(R.id.txt_employee_joining_date);
                etClientName.setText("Name " + client.getName());
                etClientMobile.setText("Mobile " + client.getMobile());
                etClientEmail.setText("Email " + client.getEmail());
                etClientAddress.setText("Address " + client.getAddress());
                etClientStatus.setText("Status " + client.getStatus());
                etemployeeRole.setText("Role " + client.getRole());
                etSalary.setText("Salary " + client.getSalary());
                etJoiningDate.setText("JoiningDate " + client.getJoiningdate());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(ClientView).create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeFetchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView FirstDetail, SecondDetail, Mobile;
        Button NavEmployeeEdit, NavEmployeeDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FirstDetail = itemView.findViewById(R.id.txt_first_detail);
            SecondDetail = itemView.findViewById(R.id.txt_second_deltail);
            NavEmployeeEdit = itemView.findViewById(R.id.fetch_EditButton);
            NavEmployeeDelete = itemView.findViewById(R.id.fetch_DeleteButton);
        }
    }

    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void showDeleteConfirmationDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEmployee(email))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteEmployee(String email) {
        String PushID = mAuth.getInstance().getCurrentUser().getUid();

        String id = email.replace(".", "dot");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("employee").child(mCompanyPushID).child(id);
        mdatabase.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notifyDataSetChanged();
            } else {
                Log.e("DeleteEmployee", "Failed to delete employee: " + task.getException());
            }
        });
    }

    private void showEditDialog(EmployeeFetchModel employee, EmployeefetchAdater.ViewHolder holder) {
        EditText etName, etEmail, etAdderess, etMobile, etsalary, etjoiningdate, etPassowrd;
        int postio1 = holder.getAdapterPosition();
        EmployeeFetchModel client = employeeFetchModelList.get(postio1);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View AddView = LayoutInflater.from(builder.getContext()).inflate(R.layout.employee_add_dialog, null);
        builder.setView(AddView)
                .setPositiveButton("Edit", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mdatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        etName = AddView.findViewById(R.id.add_employee_etName);
        etAdderess = AddView.findViewById(R.id.add_employee_etAddress);
        etEmail = AddView.findViewById(R.id.add_employee_etEmail);
        etMobile = AddView.findViewById(R.id.add_employee_etMobile);
        etsalary = AddView.findViewById(R.id.add_employee_etsalary);
        etjoiningdate = AddView.findViewById(R.id.add_employee_etJoiningdate);
        etName.setText(employee.getName());
        etAdderess.setText(employee.getAddress());
        etEmail.setText(employee.getEmail());
        etMobile.setText(employee.getMobile());
        etsalary.setText(employee.getSalary());
        etjoiningdate.setText(employee.getJoiningdate());
        etEmail.setEnabled(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Name, email, address, mobile, salary, joiningdate, password;
                        EmployeeAddModel employeeAddModel;
                        Name = etName.getText().toString();
                        address = etAdderess.getText().toString();
                        etEmail.setText(client.getEmail());
                        etEmail.setEnabled(false);
                        email = etEmail.getText().toString();
                        mobile = etMobile.getText().toString();
                        salary = etsalary.getText().toString();
                        joiningdate = etjoiningdate.getText().toString();
                        String Status = "Active";
                        String Role = "Employee";
                        String id = email.replace(".", "dot");
                        mAuth = FirebaseAuth.getInstance();
                        String PushID = mAuth.getCurrentUser().getUid();
                        if (validateEmployee(Name, address, email, mobile, joiningdate, salary)) {
                            // Employee data is valid, proceed to add to Firebase
                            String pushID = mAuth.getCurrentUser().getUid();
                            String Id = email.replace(".", "dot");
                            employeeAddModel = new EmployeeAddModel(Name, email, address, mobile, salary, joiningdate, "Active", "Employee", pushID);
                            mdatabase.child("All Data").child("employee").child(mCompanyPushID).child(Id).setValue(employeeAddModel);
                            dialog.dismiss();
                            // Clear input fields
                        }
                        else {
                            if (Name.isEmpty()) {
                                etName.setError("Please enter a name");
                            }
                            if (address.isEmpty()) {
                                etAdderess.setError("Please enter an address");
                            }
                            if (!isValidEmail(email)) {
                                etEmail.setError("Please enter a valid email address");
                            }
                            if (!isValidMobileNumber(mobile)) {
                                etMobile.setError("Please enter a valid mobile number");
                            }
                            if (!isValidDate(joiningdate)) {
                                etjoiningdate.setError("Please enter a valid date");

                            }
                            if (salary.isEmpty()) {
                                etsalary.setError("Please enter a salary");

                            }

                        }

                    }
                });
            }
        });
        alertDialog.show();

    }

    private boolean validateEmployee(String name, String address, String email, String mobile, String joiningDate, String salary) {
        if (name.isEmpty()) {
            return false;
        }
        if (address.isEmpty()) {
            return false;
        }
        if (!isValidEmail(email)) {
            return false;
        }
        if (!isValidMobileNumber(mobile)) {
            return false;
        }
        if (!isValidDate(joiningDate)) {
            return false;
        }
        if (salary.isEmpty()) {
            return false;
        }

        // If all validations pass, return true
        return true;
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

}
