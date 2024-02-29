package com.spectric.erpsystem.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Intenrs.InternsFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InternsFetchAdapter extends RecyclerView.Adapter<InternsFetchAdapter.Viewholder> {
    private Context context;
    private List<InternsFetchModel> internsFetchModelList;
    private DatabaseReference mdatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mCompanyPushID;

    public InternsFetchAdapter(Context context, List<InternsFetchModel> internsFetchModelList,String mCompanyPushID) {
        this.context = context;
        this.internsFetchModelList = internsFetchModelList;
        this.mdatabase = FirebaseDatabase.getInstance().getReference();
        this.mCompanyPushID = mCompanyPushID;
    }

    @NonNull
    @Override
    public InternsFetchAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_data_fetch_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternsFetchAdapter.Viewholder holder, int position) {
        InternsFetchModel intern = internsFetchModelList.get(position);

        holder.FirstDetail.setText(intern.getFullName());
        holder.SeconDetail.setText(intern.getEmailAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                View internView = LayoutInflater.from(context).inflate(R.layout.interns_fetch_data_card,null);
                ((TextView) internView.findViewById(R.id.txt_intern_full_name)).setText("Name: " + intern.getFullName());
                ((TextView) internView.findViewById(R.id.txt_intern_field)).setText("Field: " + intern.getField());
                ((TextView) internView.findViewById(R.id.txt_intern_fees)).setText("Fees: " + intern.getFees());
                ((TextView) internView.findViewById(R.id.txt_intern_employee_email)).setText("Employee Email Id: " + intern.getEmployeeEmail());
                ((TextView) internView.findViewById(R.id.txt_intern_start_date)).setText("Joining Date: " + intern.getJoiningDate());
                ((TextView) internView.findViewById(R.id.txt_intern_gender)).setText("Gender: " + intern.getGender());
                ((TextView) internView.findViewById(R.id.txt_intern_duration)).setText("Duration: " + intern.getDuration());

// Set data for the remaining fields
// Add more lines as needed
                ((TextView) internView.findViewById(R.id.txt_intern_email)).setText("Email: " + intern.getEmailAddress());
                ((TextView) internView.findViewById(R.id.txt_intern_start_date)).setText("Internship Start Date: " + intern.getJoiningDate());
                ((TextView) internView.findViewById(R.id.txt_intern_end_date)).setText("Internship End Date: " + intern.getEndDate());
                ((TextView) internView.findViewById(R.id.txt_intern_supervisor_name)).setText("Supervisor Name: " + intern.getEmployeeEmail());

// Remove unnecessary fields
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(internView).create().show();

            }
        });
        holder.Editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditIntern(intern);
            }
        });
        holder.DeletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email address of the current intern
                String emailAddress = holder.SeconDetail.getText().toString();
                deleteIntern(emailAddress);
            }
        });

    }

    @Override
    public int getItemCount() {
        return internsFetchModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView FirstDetail,SeconDetail;
        Button Editbtn, DeletBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            FirstDetail= itemView.findViewById(R.id.txt_first_detail);
            SeconDetail = itemView.findViewById(R.id.txt_second_deltail);

            Editbtn = itemView.findViewById(R.id.fetch_EditButton);
            DeletBtn = itemView.findViewById(R.id.fetch_DeleteButton);
        }
    }
    // Method to delete the intern from the database
    private void deleteIntern(String emailAddress) {
        String id = emailAddress.replace(".", "dot");
        String pushID = mAuth.getCurrentUser().getUid();

        // Construct the database reference for the intern
        DatabaseReference internRef = mdatabase.child("All Data").child("inter_ship").child(mCompanyPushID).child(id);

        // Remove the intern data from the database
        internRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Intern deleted successfully
                            Toast.makeText(context, "Intern deleted successfully", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged(); // Notify the adapter that data has changed
                        } else {
                            // Failed to delete intern
                            Toast.makeText(context, "Failed to delete intern", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(context, "Failed to delete intern", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper method to edit intern details
    public void EditIntern(InternsFetchModel intern) {
        View editView = LayoutInflater.from(context).inflate(R.layout.interns_add_dialog, null);

        EditText etFullName, etField, etContactNo, etEmailAddress, etFees, etEmployeeEmail, etJoiningDate, etEndDate, etDuration;
        RadioGroup rgGender;

        etFullName = editView.findViewById(R.id.add_intern_full_name);
        etField = editView.findViewById(R.id.add_intern_field);
        etContactNo = editView.findViewById(R.id.add_intern_contact_number);
        etEmailAddress = editView.findViewById(R.id.add_intern_email);
        etFees = editView.findViewById(R.id.add_intern_fees);
        etEmployeeEmail = editView.findViewById(R.id.add_intern_employee_email);
        etJoiningDate = editView.findViewById(R.id.add_intern_start_date);
        etEndDate = editView.findViewById(R.id.add_intern_end_date);
        etDuration = editView.findViewById(R.id.add_intern_duration);
        rgGender = editView.findViewById(R.id.add_intern_gender);

        // Populate the dialog with existing data
        etFullName.setText(intern.getFullName());
        etField.setText(intern.getField());
        etContactNo.setText(intern.getContactNo());
        etEmailAddress.setText(intern.getEmailAddress());
        etEmailAddress.setEnabled(false);
        etFees.setText(intern.getFees());
        etEmployeeEmail.setText(intern.getEmployeeEmail());
        etJoiningDate.setText(intern.getJoiningDate());
        etEndDate.setText(intern.getEndDate());
        etDuration.setText(intern.getDuration());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editView)
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Your logic to save changes goes here
                        String fullName, field, contactNo, fees, employeeEmail, joiningDate, endDate, duration, id;

                        fullName = etFullName.getText().toString();
                        field = etField.getText().toString();
                        contactNo = etContactNo.getText().toString();
                        fees = etFees.getText().toString();
                        employeeEmail = etEmployeeEmail.getText().toString();
                        joiningDate = etJoiningDate.getText().toString();
                        endDate = etEndDate.getText().toString();

                        // Validate end date format
                        if (!isValidDate(endDate)) {
                            Toast.makeText(context, "Invalid end date format. Please enter in dd-MM-yyyy format.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Validate end date is after start date and duration is at least 1 month
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date startDate = sdf.parse(joiningDate);
                            Date endDt = sdf.parse(endDate);

                            if (endDt.before(startDate)) {
                                Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Calendar startCalendar = new GregorianCalendar();
                            startCalendar.setTime(startDate);
                            Calendar endCalendar = new GregorianCalendar();
                            endCalendar.setTime(endDt);

                            int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                            int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                            int diffMonths = months + years * 12;

                            if (diffMonths < 1) {
                                Toast.makeText(context, "Duration must be at least 1 month", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Calculate duration in months
                            duration = String.valueOf(diffMonths);

                            id = etEmailAddress.getText().toString().replace(".", "dot");

                            DatabaseReference mdatabaseEdit = FirebaseDatabase.getInstance().getReference();
                            String pushID = mAuth.getCurrentUser().getUid();

                            DatabaseReference internRef = mdatabaseEdit.child("All Data")
                                    .child("inter_ship")
                                    .child(mCompanyPushID)
                                    .child(id);

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("fullName", fullName);
                            updateData.put("field", field);
                            updateData.put("contactNo", contactNo);
                            updateData.put("fees", fees);
                            updateData.put("employeeEmail", employeeEmail);
                            updateData.put("joiningDate", joiningDate);
                            updateData.put("endDate", endDate);
                            updateData.put("duration", duration);

                            internRef.updateChildren(updateData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(context, "Intern updated successfully", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged(); // Notify the adapter that data has changed
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Failed to update intern", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Cancel
                    }
                })
                .create()
                .show();
    }
    // Method to validate date format
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }



}
