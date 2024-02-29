package com.spectric.erpsystem.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.Company.CompanyFetchModel;
import com.spectric.erpsystem.Models.Company.CompanyRegisterModel;
import com.spectric.erpsystem.R;

import java.util.List;

public class ReadDataAdapter extends RecyclerView.Adapter<ReadDataAdapter.ViewHolder> {

    private Context mctx;
    private List<CompanyFetchModel> companyRegisterModelList;
    String mCompanyPushId;


    public ReadDataAdapter(Context mctx, List<CompanyFetchModel> companyRegisterModelList, String mCompanyPushId) {
        this.mctx = mctx;
        this.companyRegisterModelList = companyRegisterModelList;
        this.mCompanyPushId = mCompanyPushId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_data_fetch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyFetchModel company = companyRegisterModelList.get(position);
        holder.FirstDetail.setText(company.getComapnyName());
        holder.SecondDeatil.setText(company.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postio1 = holder.getAdapterPosition();
                CompanyFetchModel company = companyRegisterModelList.get(postio1);
                View view = LayoutInflater.from(mctx).inflate(R.layout.datacard,null);
                ((TextView) view.findViewById(R.id.txtcompanyname)).setText("Name: " + company.getComapnyName());
                ((TextView) view.findViewById(R.id.txtemail)).setText("Mobile: " + company.getEmail());
                ((TextView) view.findViewById(R.id.txtpassword)).setText("Email: " + company.getPassword());
//                ((TextView) clientView.findViewById(R.id.txt_client_address)).setText("Address: " + client.getAddress());
                ((TextView) view.findViewById(R.id.txtaddress)).setText("Status: " + company.getAddress());

                // Set data for the remaining fields
                ((TextView) view.findViewById(R.id.txtmobile)).setText("" + company.getMobile());
                ((TextView) view.findViewById(R.id.txtgstno)).setText("Project Description: " + company.getGstNo());


                AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
                builder.setView(view).create().show();

//                holder.cname.setText(company.getComapnyName());
//                holder.cphone.setText(company.getMobile());
//                holder.cemail.setText(company.getEmail());
//                // Password is not displayed
//                holder.cpassword.setVisibility(View.GONE);
//                holder.cgst.setText(company.getGstNo());
//                holder.caddress.setText(company.getAddress());
            }
        });
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            EditComapny(holder);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Position = holder.getAdapterPosition();
                deleteCompany(Position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyRegisterModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView FirstDetail,SecondDeatil,cname, cemail, cpassword, caddress, cphone, cgst;
        Button Edit,Delete;
        View view = LayoutInflater.from(mctx).inflate(R.layout.datacard,null);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Edit= itemView.findViewById(R.id.fetch_EditButton);
            Delete = itemView.findViewById(R.id.fetch_DeleteButton);
            FirstDetail = itemView.findViewById(R.id.txt_first_detail);
            SecondDeatil = itemView.findViewById(R.id.txt_second_deltail);
            cname = view.findViewById(R.id.txtcompanyname);
            cemail = view.findViewById(R.id.txtemail);
            cpassword = view.findViewById(R.id.txtpassword);
            caddress = view.findViewById(R.id.txtaddress);
            cphone = view.findViewById(R.id.txtmobile);
            cgst = view.findViewById(R.id.txtgstno);
        }
    }

    public void EditComapny(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        CompanyFetchModel company = companyRegisterModelList.get(position);
        View view = LayoutInflater.from(mctx).inflate(R.layout.companay_edit_card, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
        builder.setView(view);

        // Retrieve EditText fields
        EditText etCompanyName = view.findViewById(R.id.regetCompanyName);
        EditText etMobile = view.findViewById(R.id.regetmobile);
        EditText etAddress = view.findViewById(R.id.regetAddress);
        EditText etEmail = view.findViewById(R.id.regetEmail);
        EditText etWebsite = view.findViewById(R.id.regetWebsite);
        EditText etGstNo = view.findViewById(R.id.regetgstno);
        EditText etHscSac = view.findViewById(R.id.regetHScSac);
        EditText etCompanyPan = view.findViewById(R.id.regetCompanyPan);
        EditText etPassword = view.findViewById(R.id.regetpassword);

        // Populate EditText fields with existing company data
        etCompanyName.setText(company.getComapnyName());
        etMobile.setText(company.getMobile());
        etAddress.setText(company.getAddress());
        etEmail.setText(company.getEmail());
        etWebsite.setText(company.getWebsite());
        etGstNo.setText(company.getGstNo());
        etHscSac.setText(company.getHscSac());
        etCompanyPan.setText(company.getCompanyPan());
        etPassword.setText(company.getPassword());

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the company data in the Firebase Realtime Database
                DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference()
                        .child("All Data").child("Company").child(mCompanyPushId);

                // Get updated values from EditText fields
                String updatedCompanyName = etCompanyName.getText().toString();
                String updatedMobile = etMobile.getText().toString();
                String updatedAddress = etAddress.getText().toString();
                String updatedEmail = etEmail.getText().toString();
                String updatedWebsite = etWebsite.getText().toString();
                String updatedGstNo = etGstNo.getText().toString();
                String updatedHscSac = etHscSac.getText().toString();
                String updatedCompanyPan = etCompanyPan.getText().toString();
                String updatedPassword = etPassword.getText().toString();
                if (TextUtils.isEmpty(updatedCompanyName)
                        || TextUtils.isEmpty(updatedMobile)
                        || TextUtils.isEmpty(updatedAddress)
                        || TextUtils.isEmpty(updatedEmail)
                        || TextUtils.isEmpty(updatedWebsite)
                        || TextUtils.isEmpty(updatedGstNo)
                        || TextUtils.isEmpty(updatedHscSac)
                        || TextUtils.isEmpty(updatedCompanyPan)
                        || TextUtils.isEmpty(updatedPassword)) {
                    Toast.makeText(mctx, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(updatedEmail)) {
                    Toast.makeText(mctx, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhoneNumber(updatedMobile)) {
                    Toast.makeText(mctx, "Invalid mobile number format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidGST(updatedGstNo)) {
                    Toast.makeText(mctx, "Invalid GST format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update company data
                CompanyRegisterModel updatedCompany = new CompanyRegisterModel(updatedCompanyName, updatedMobile,
                        updatedAddress, updatedEmail, updatedWebsite, updatedGstNo, updatedHscSac,
                        updatedCompanyPan, "Active", "Company", updatedPassword);

                companyRef.setValue(updatedCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mctx, "Edited", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mctx, "Not Edited", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    // Delete method
        public void deleteCompany(int position) {
            CompanyFetchModel company = companyRegisterModelList.get(position);
            // Display a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(mctx);
            builder.setTitle("Delete Company");
            builder.setMessage("Are you sure you want to delete this company?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Delete the company from the Firebase Realtime Database
                    DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference().child("All Data").child("Company").child(mCompanyPushId);
                    companyRef.removeValue();
                    // Remove the company from the RecyclerView
                    companyRegisterModelList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, companyRegisterModelList.size());
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
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



