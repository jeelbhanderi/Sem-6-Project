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
import com.spectric.erpsystem.Models.Client.ClientAddModel;
import com.spectric.erpsystem.Models.Client.ClientEditModel;
import com.spectric.erpsystem.Models.Client.ClientFetchModel;
import com.spectric.erpsystem.Models.Incomemodel;
import com.spectric.erpsystem.Models.ProjectDetails;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ClientFetchAdapter extends RecyclerView.Adapter<ClientFetchAdapter.ViewHolder> {
    private Context mctx;
    private ClientEditModel clientEditModel;
    private FirebaseAuth mAuth;
    private List<ClientFetchModel> clientList;
    private String mCompanyPushID; // New member variable to hold the company's PushID
    private DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

    public ClientFetchAdapter(Context mctx, List<ClientFetchModel> clientList, String companyPushID) {
        this.mctx = mctx;
        this.clientList = clientList;
        this.mAuth = FirebaseAuth.getInstance();
        this.mCompanyPushID = companyPushID; // Assign the company's PushID
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_data_fetch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientFetchModel client = clientList.get(position);

        holder.FirtsDetail.setText(client.getName());
        holder.SecondDetail.setText(client.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"WrongViewCast", "MissingInflatedId"})
            @Override
            public void onClick(View v) {
               int postio1 = holder.getAdapterPosition();
                ClientFetchModel client = clientList.get(postio1);
                View clientView = LayoutInflater.from(mctx).inflate(R.layout.client_data_fetch,null);
                ((TextView) clientView.findViewById(R.id.txt_client_Name1)).setText("Name: " + client.getName());
                ((TextView) clientView.findViewById(R.id.txt_client_mobile1)).setText("Mobile: " + client.getMobile());
                ((TextView) clientView.findViewById(R.id.txt_client_email)).setText("Email: " + client.getEmail());
//                ((TextView) clientView.findViewById(R.id.txt_client_address)).setText("Address: " + client.getAddress());

                // Set data for the remaining fields
                ((TextView) clientView.findViewById(R.id.txt_client_ProjectName)).setText("Project Name: " + client.getProjectName());
                ((TextView) clientView.findViewById(R.id.txt_client_ProjectDescription)).setText("Project Description: " + client.getProjectDescription());
                ((TextView) clientView.findViewById(R.id.txt_client_TotalPrice)).setText("Total Price: " + client.getTotalPrice());

                ((TextView) clientView.findViewById(R.id.txt_client_AdvancePayment)).setText("Advance Payment: " + client.getAdvancePayment());
                ((TextView) clientView.findViewById(R.id.txt_client_RemainingPayment)).setText("Remaining Payment: " + client.getRemainingPayment());
                ((TextView) clientView.findViewById(R.id.txt_client_Language)).setText("Language: " + client.getLanguage());
                ((TextView) clientView.findViewById(R.id.txt_client_SubmissionDate)).setText("Submission Date: " + client.getSubmissionDate());
                ((TextView) clientView.findViewById(R.id.txt_client_ProjectManagerEmail)).setText("Project Manager Email: " + client.getProjectManagerEmail());
                ((TextView) clientView.findViewById(R.id.txt_client_PushId)).setText("PushId: " + client.getPushId());

                AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
                builder.setView(clientView).create().show();


            }
        });

        // Edit Button code
        holder.NavClientEdit.setOnClickListener(v -> showEditClientDialog(client,mCompanyPushID));

        // Delete Button Code
        holder.NavClientDelete.setOnClickListener(v -> showDeleteConfirmationDialog(client.getEmail()));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView FirtsDetail,SecondDetail;
        Button NavClientEdit, NavClientDelete;
        LinearLayout additionalFieldsLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FirtsDetail = itemView.findViewById(R.id.txt_first_detail);
            SecondDetail = itemView.findViewById(R.id.txt_second_deltail);
            NavClientEdit = itemView.findViewById(R.id.fetch_EditButton);
            NavClientDelete = itemView.findViewById(R.id.fetch_DeleteButton);
        }
    }

    private void showEditClientDialog(ClientFetchModel client, String mCompanyPushID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
        View editClientView = LayoutInflater.from(builder.getContext()).inflate(R.layout.client_add_dialog, null);
        builder.setView(editClientView);
        builder.setPositiveButton("Save", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
//        View editClientView = LayoutInflater.from(mctx).inflate(R.layout.client_add_dialog, null);

        EditText etName, etEmail, etMobile, etProjectName, etProjectDescription,
                etTotalPrice, etAdvancePayment, etRemainingPayment, etLanguage,
                etSubmissionDate, etProjectManagerEmail;

        etName = editClientView.findViewById(R.id.addclientetName);
        etEmail = editClientView.findViewById(R.id.addclientetEmail);
        etMobile = editClientView.findViewById(R.id.addclientetMobile);
        etProjectName = editClientView.findViewById(R.id.addclientetProjectName);
        etProjectDescription = editClientView.findViewById(R.id.addclientetProjectDescription);
        etTotalPrice = editClientView.findViewById(R.id.addclientetTotalPrice);
        etAdvancePayment = editClientView.findViewById(R.id.addclientetAdvancePayment);
        etRemainingPayment = editClientView.findViewById(R.id.addclientetRemainingPayment);
        etLanguage = editClientView.findViewById(R.id.addclientetLanguage);
        etSubmissionDate = editClientView.findViewById(R.id.addclientetSubmissionDate);
        etProjectManagerEmail = editClientView.findViewById(R.id.addclientetProjectManagerEmail);

        etName.setText(client.getName());
        etEmail.setText(client.getEmail());
        etEmail.setEnabled(false);  // Disable email field
        etMobile.setText(client.getMobile());
        etProjectName.setText(client.getProjectName());
        etProjectDescription.setText(client.getProjectDescription());
        etTotalPrice.setText(client.getTotalPrice());
        etAdvancePayment.setText(client.getAdvancePayment());
        etRemainingPayment.setText(client.getRemainingPayment());
        etLanguage.setText(client.getLanguage());
        etSubmissionDate.setText(client.getSubmissionDate());
        etProjectManagerEmail.setText(client.getProjectManagerEmail());

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = client.getEmail().replace(".", "dot");

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
                            addClientToDatabase(name, email, mobile, projectName, projectDescription, totalPrice, advancePayment, remainingPayment, language, submissionDate, projectManagerEmail);
                            dialog.dismiss(); // Dismiss the dialog after saving
                        } else {   // Data is not valid, show error messages
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
                                etProjectDescription.setError("Invalid Project Description");
                            }
                            if (!isValidNumeric(advancePayment) || !isValidNumeric(remainingPayment)) {
                                if(!isValidNumeric(advancePayment)) {
                                    etAdvancePayment.setError("Invalid Advance Amount");
                                }
                                else if(!isValidNumeric(remainingPayment)) {
                                    etRemainingPayment.setError("Invalid Remaining Amount");
                                }
                            }
                            if (totalPrice.isEmpty()) {
                                etTotalPrice.setError("Invalid Project field or amount");
                            }
                            if (language.isEmpty()) {
                                etLanguage.setError("Invalid Project field or Language");
                            }
                            if (!isValidDate(submissionDate)) {
                                etSubmissionDate.setError("Invalid Date");
                            }
                            if (!isValidEmail(projectManagerEmail)) {
                                etProjectManagerEmail.setError("Invalid Email");
                            }
                        }
                    }
                });

            }
        });
        alertDialog.show();
//        builder.show();

    }


    // Function to format date to "dd-MM-yyyy" format

    // Function to validate payment amount



    private void showDeleteConfirmationDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure All Data Related to This Client Will be deleted Income,Projects")
                .setPositiveButton("Yes", (dialog, which) -> deleteClient(email,mCompanyPushID))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void deleteClient(String email,String mCompanyPushID) {
        String PushID = mAuth.getInstance().getCurrentUser().getUid();
        String id = email.replace(".", "dot");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("clients").child(mCompanyPushID).child(id);
        mdatabase.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteClient", "Client deleted successfully.");
                    updateData(clientList);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Add a log statement to check if there's an error during deletion
                    Log.e("DeleteClient", "Error deleting client: " + e.getMessage());
                });
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("Projects").child(mCompanyPushID).child(id);
        mdatabase.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteClient", "Client deleted successfully.");
                    updateData(clientList);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Add a log statement to check if there's an error during deletion
                    Log.e("DeleteClient", "Error deleting client: " + e.getMessage());
                });
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("income").child(mCompanyPushID).child(id);// Add a log statement to check if the method is called

        Log.d("DeleteClient", "Deleting client with email: " + email);
        mdatabase.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteClient", "Client deleted successfully.");
                    updateData(clientList);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Add a log statement to check if there's an error during deletion
                    Log.e("DeleteClient", "Error deleting client: " + e.getMessage());
                });
    }



    public void updateData(List<ClientFetchModel> newData) {
        clientList.clear();
        clientList.addAll(newData);
        notifyDataSetChanged();
    }
    // Function to validate date format
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

        mdatabase.child("All Data")
                .child("clients")
                .child(PushID)
                .child(id)
                .setValue(clientAddModel);
        mdatabase.child("All Data")
                .child("Projects")
                .child(PushID)
                .child(id)
                .setValue(projectModel);

        Incomemodel incomeModel = new Incomemodel(
                "Client", email, totalPrice, getCurrentDate(), advancePayment, remainingPayment);

        mdatabase.child("All Data")
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
