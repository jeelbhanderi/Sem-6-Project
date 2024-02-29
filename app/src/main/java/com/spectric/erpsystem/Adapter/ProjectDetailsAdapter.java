package com.spectric.erpsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.ProjectDetails;
import com.spectric.erpsystem.Models.ProjectDetsilsFetch;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectDetailsAdapter extends RecyclerView.Adapter<ProjectDetailsAdapter.ViewHolder> {

    private List<ProjectDetsilsFetch> projectDetailsList;
    private Context context;
    private DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String mCompanyPushId;

    public ProjectDetailsAdapter(List<ProjectDetsilsFetch> projectDetailsList, Context context,String mCompanyPushId) {
        this.projectDetailsList = projectDetailsList;
        this.context = context;
        this.mCompanyPushId = mCompanyPushId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item_project_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectDetsilsFetch projectDetails = projectDetailsList.get(position);
        holder.textProjectName.setText("Project Name: " + projectDetails.getProjectName());
//        holder.textDescription.setText("Description: " + projectDetails.getDescription());

        // Set progress on AnyChartView
        setAnyChartProgress(holder.anyChartProgress, projectDetails.getProgress());
    }

    @Override
    public int getItemCount() {
        return projectDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textClientName, textClientEmail, textProjectName, textDescription;
        AnyChartView anyChartProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textProjectName = itemView.findViewById(R.id.txtProjectName);
            anyChartProgress = itemView.findViewById(R.id.anyChartProgress);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Handle long click
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ProjectDetsilsFetch projectDetails = projectDetailsList.get(position);
                        editProject( projectDetails);
                        return true; // Consume the long click event
                    }
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ProjectDetsilsFetch projectDetails = projectDetailsList.get(position);
                        showProjectDetailsDialog(context,projectDetails);
                    }
                }
            });
        }
    }

    private void setAnyChartProgress(AnyChartView anyChartView, String progress) {
        Pie pie = AnyChart.pie();

        List<DataEntry> data = generateData(progress);

        pie.data(data);

        // Set pie chart size explicitly
        pie.labels().position("outside");
        pie.radius("50%"); // Adjust as needed

        anyChartView.setChart(pie);
    }



    private List<DataEntry> generateData(String progress) {
        // Convert progress string to integers
        int progressValue = Integer.parseInt(progress);

        // Calculate remaining progress
        int remainingProgress = 100 - progressValue;

        // Prepare data for the pie chart
        List<DataEntry> data = new java.util.ArrayList<>();
        data.add(new ValueDataEntry("Progress", progressValue));
        data.add(new ValueDataEntry("Remaining", remainingProgress));

        return data;
    }
    private void showProjectDetailsDialog(Context context, ProjectDetsilsFetch projectDetails) {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the layout for the dialog
        View view = LayoutInflater.from(context).inflate(R.layout.show_projects_detail, null);
        builder.setView(view);

        // Initialize TextViews in the dialog
        TextView dialogProjectName = view.findViewById(R.id.Show_Project_Name);
        TextView dialogClientEmail = view.findViewById(R.id.Show_Project_client_email);
        TextView dialogType = view.findViewById(R.id.type);
        TextView dialogProjectDescription = view.findViewById(R.id.Show_Project_Description);
        TextView dialogTotalPrice = view.findViewById(R.id.Show_Project_total_price);
        TextView dialogAdvancePayment = view.findViewById(R.id.Show_Project_Advance_payment);
        TextView dialogProjectProgress = view.findViewById(R.id.Show_Project_Progress);
        TextView dialogProjectStatus = view.findViewById(R.id.Show_Project_Status);
        TextView dialogSubmissionDate = view.findViewById(R.id.Show_Project_submition_Date);

        // Get data from the selected projectDetails
        String projectName = projectDetails.getProjectName();
        String clientEmail = projectDetails.getOwnerEmail();
        String projectType = projectDetails.getOwnerType(); // Assuming you have a method for this in your ProjectDetails class
        String projectDescription = projectDetails.getProjectDescription();
        String totalPrice = projectDetails.getTotalPrice();
        String advancePayment = projectDetails.getAdvancePayment();
        String projectProgress = projectDetails.getProgress();
        String projectStatus = projectDetails.getStatus();
        String submissionDate = projectDetails.getSubmitionDate();

        // Set data to the TextViews in the dialog
        dialogProjectName.setText("Project Name: " + projectName);
        dialogClientEmail.setText("Email Id: " + clientEmail);

        // Set the selected radio button based on the project type


        dialogProjectDescription.setText("Project Description: " + projectDescription);
        dialogTotalPrice.setText("Total Price: " + totalPrice);
        dialogAdvancePayment.setText("Advance Payment: " + advancePayment);
        dialogProjectProgress.setText("Project Progress: " + projectProgress);
        dialogProjectStatus.setText("Project Status: " + projectStatus);
        dialogSubmissionDate.setText("Submission Date: " + submissionDate);
        dialogType.setText("Type :"+projectType);
        // Create and show the AlertDialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle OK button click if needed
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProject(projectDetails); // Call the editProject method with the projectDetails
            }
        });

        builder.create().show();
    }
    private void editProject(ProjectDetsilsFetch projectDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View editView = LayoutInflater.from(context).inflate(R.layout.project_add_dialog, null);

        // Find views in the editView for pre-filling existing project details
        EditText etProjectName = editView.findViewById(R.id.Add_Project_Name);
        RadioGroup etType = editView.findViewById(R.id.type);
        EditText etDescription = editView.findViewById(R.id.Add_Project_Description);
        EditText etTotalPrice = editView.findViewById(R.id.Add_Project_total_price);
        EditText etAdvancePayment = editView.findViewById(R.id.Add_Project_AdvancePayMent);
        EditText etRemainingPayment = editView.findViewById(R.id.Add_Project_RemainingPayment);
        EditText etField = editView.findViewById(R.id.Add_Project_Field);
        EditText etHandlerEmail = editView.findViewById(R.id.Add_Project_HandlerEmail);
        EditText etSubmissionDate = editView.findViewById(R.id.Add_Project_submition_Date);
        EditText etOwnerEmail = editView.findViewById(R.id.Add_Project_owner_email);
        SeekBar seekBar = editView.findViewById(R.id.progressbar); // Find the SeekBar
        etOwnerEmail.setEnabled(false);
        // Disable editing of project name
        etProjectName.setFocusable(false);
        etProjectName.setEnabled(false);

        // Pre-fill the views with existing project details
        etProjectName.setText(projectDetails.getProjectName());
        etDescription.setText(projectDetails.getProjectDescription());
        etTotalPrice.setText(projectDetails.getTotalPrice());
        etAdvancePayment.setText(projectDetails.getAdvancePayment());
        etRemainingPayment.setText(projectDetails.getRemainingPayment());
        etField.setText(projectDetails.getField());
        etHandlerEmail.setText(projectDetails.getHandlerEMail());
        etSubmissionDate.setText(projectDetails.getSubmitionDate());
        etOwnerEmail.setText(projectDetails.getOwnerEmail());
        seekBar.setProgress(Integer.parseInt(projectDetails.getProgress()));
        // Set the selected radio button based on the project type
        if (projectDetails.getOwnerType().equals("Client")) {
            etType.check(R.id.radioClient);
        } else if (projectDetails.getOwnerType().equals("Intern")) {
            etType.check(R.id.radioIntern);
        }

        // Set the maximum value of the SeekBar to 100
        seekBar.setMax(100);
        // Set the current progress of the SeekBar to the project's progress
        seekBar.setProgress(Integer.parseInt(projectDetails.getProgress()));

        // Set up positive button as "Edit" for editing a project
        builder.setView(editView).setPositiveButton("Edit",null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog when canceled
            }
        });

        AlertDialog myDialog = builder.create();
        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button PostiveBtn = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                PostiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updatedType = getSelectedType(etType);
                        String updatedDescription = etDescription.getText().toString();
                        String updatedTotalPrice = etTotalPrice.getText().toString();
                        String updatedAdvancePayment = etAdvancePayment.getText().toString();
                        String updatedRemainingPayment = etRemainingPayment.getText().toString();
                        String updatedField = etField.getText().toString();
                        String updatedHandlerEmail = etHandlerEmail.getText().toString();
                        String updatedSubmissionDate = etSubmissionDate.getText().toString();
                        String updatedOwnerEmail = etOwnerEmail.getText().toString();
                        // Get the progress from the SeekBar
                        String updatedProgress = String.valueOf(seekBar.getProgress());
                        String id = updatedOwnerEmail.replace(".","dot");
                        // Update the project in the database with the updated details
                        String projectName = etProjectName.getText().toString();


                        if (updatedType != null) {
                            if (updatedOwnerEmail.isEmpty() || projectName.isEmpty() || updatedDescription.isEmpty() || updatedTotalPrice.isEmpty() || updatedAdvancePayment.isEmpty() || updatedRemainingPayment.isEmpty() || updatedField.isEmpty() || updatedSubmissionDate.isEmpty() || updatedHandlerEmail.isEmpty()) {
                                Toast.makeText(context, "Please Fill All the Field", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!isValidEmail(updatedOwnerEmail)){
                                etOwnerEmail.setError("Invalid Email");
                                return;
                            }
                            if(!isValidEmail(updatedHandlerEmail)){
                                etHandlerEmail.setError("Invalid Email");
                                return;
                            }
                            if (!isValidDate(updatedSubmissionDate)) {
                                etSubmissionDate.setError("Invalid DAte");
                                return;
                            }
                            if (updatedAdvancePayment.isEmpty() || updatedRemainingPayment.isEmpty() || updatedTotalPrice.isEmpty()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                                return; // Exit the method
                            }

                            // Convert the string values to double for comparison
                            double advancePayment = Double.parseDouble(updatedAdvancePayment);
                            double remainingPayment = Double.parseDouble(updatedRemainingPayment);
                            double totalPrice = Double.parseDouble(updatedTotalPrice);

                            if (advancePayment + remainingPayment != totalPrice) {
                                Toast.makeText(context, "Advance payment and remaining payment cannot exceed total price", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(isValidData(updatedOwnerEmail,projectName,updatedDescription,updatedTotalPrice,updatedAdvancePayment,updatedRemainingPayment,updatedField,updatedSubmissionDate,updatedHandlerEmail)){
                                ProjectDetails projectDetails1 = new ProjectDetails(projectName, updatedOwnerEmail, updatedDescription, updatedTotalPrice, updatedAdvancePayment, updatedRemainingPayment, "active", updatedSubmissionDate, updatedProgress, updatedField, updatedType, updatedHandlerEmail);
                                mdatabase.child("All Data").child("Projects").child(mCompanyPushId).child(id).setValue(projectDetails1);

                                dialog.dismiss();
                            }

                        } else {
                            Toast.makeText(context, "Please select a role (Client or Intern)", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
        });
        myDialog.show();
    }


    private String getSelectedType(RadioGroup typeGroup) {
        // Get the ID of the selected radio button in the RadioGroup
        int selectedRadioButtonId = typeGroup.getCheckedRadioButtonId();

        // Find the selected radio button
        RadioButton selectedRadioButton = typeGroup.findViewById(selectedRadioButtonId);

        // Return the text of the selected radio button
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText().toString();
        } else {
            return ""; // Return empty string if no radio button is selected
        }
    }
//Delete Method For Company
public void deleteProject(ProjectDetsilsFetch projectDetails) {
    String email = projectDetails.getOwnerEmail();
    String id = email.replace(".","dot");
    DatabaseReference projectRef = mdatabase.child("All Data").child("Projects").child(mCompanyPushId).child(id);
    projectRef.removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Project deleted successfully
                    // Optionally, you can notify the adapter that an item has been removed
                    int position = projectDetailsList.indexOf(projectDetails);
                    if (position != -1) {
                        projectDetailsList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Failed to delete project
                    Log.e("ProjectDetailsAdapter", "Failed to delete project", e);
                    // Optionally, show an error message to the user
                    // You can use Toast or Snackbar to display the error message
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
