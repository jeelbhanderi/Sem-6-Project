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
import com.spectric.erpsystem.Models.Invoice.InvoiceAddModel;
import com.spectric.erpsystem.Models.Invoice.InvoiceFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class InvoiceFetchAdapter extends RecyclerView.Adapter<InvoiceFetchAdapter.ViewHolder> {
    Context context;
    InvoiceAddModel invoiceAddModel;
    String mCompanyPushId;

    public InvoiceFetchAdapter(Context context, List<InvoiceFetchModel> invoiceFetchModelList,String mCompanyPushId) {
        this.context = context;
        this.invoiceFetchModelList = invoiceFetchModelList;
        this.mCompanyPushId = mCompanyPushId;
    }

    List<InvoiceFetchModel> invoiceFetchModelList;

    @NonNull
    @Override
    public InvoiceFetchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View FetchData = LayoutInflater.from(context).inflate(R.layout.all_data_fetch_card, null);
        return new ViewHolder(FetchData);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceFetchAdapter.ViewHolder holder, int position) {
        holder.FirstDetail.setText(invoiceFetchModelList.get(position).getInvoiceNo());
        holder.SecondDetail.setText(invoiceFetchModelList.get(position).getAmount());
        int position1 = holder.getAbsoluteAdapterPosition();

        holder.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String InvoiceNo = invoiceFetchModelList.get(position1).getInvoiceNo();
                int position = holder.getAbsoluteAdapterPosition();

                EditInvoiceDialog(InvoiceNo, context, position);
            }
        });
        holder.DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invoiceNo = invoiceFetchModelList.get(position1).getInvoiceNo();
                deleteInvoice(invoiceNo);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                TextView InvoiceNo, Name, Email, Date, Address, Mobile, Amount, CGST;
                View Fetch = LayoutInflater.from(context).inflate(R.layout.invoice_fetch_data_card, null);
                InvoiceNo = Fetch.findViewById(R.id.txt_invoice_no1);
                Name = Fetch.findViewById(R.id.txt_invoice_clientname1);
                Email = Fetch.findViewById(R.id.txt_invoice_client_email);
                Date = Fetch.findViewById(R.id.txt_invoice_date);
                Address = Fetch.findViewById(R.id.txt_invoice_client_Address);
                Mobile = Fetch.findViewById(R.id.txt_invoice_client_mobile);
                Amount = Fetch.findViewById(R.id.txt_invoice_amount);
                CGST = Fetch.findViewById(R.id.txt_invoice_cgst);


                InvoiceNo.setText(invoiceFetchModelList.get(position).getInvoiceNo());
                Name.setText(invoiceFetchModelList.get(position).getClientName());
                Email.setText(invoiceFetchModelList.get(position).getClient_email());
                Date.setText(invoiceFetchModelList.get(position).getDate());
                Address.setText(invoiceFetchModelList.get(position).getClient_address());
                Mobile.setText(invoiceFetchModelList.get(position).getClient_phoneNumber());
                Amount.setText(invoiceFetchModelList.get(position).getAmount());
                CGST.setText(invoiceFetchModelList.get(position).getCgst());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(Fetch).create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceFetchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView FirstDetail, SecondDetail;
        Button DeleteBtn, EditBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FirstDetail = itemView.findViewById(R.id.txt_first_detail);
            SecondDetail = itemView.findViewById(R.id.txt_second_deltail);
            EditBtn = itemView.findViewById(R.id.fetch_EditButton);
            DeleteBtn = itemView.findViewById(R.id.fetch_DeleteButton);

        }
    }

    private void EditInvoiceDialog(String InvoiceNo, Context context, int Position) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        View AddView = LayoutInflater.from(context).inflate(R.layout.invoice_add_dialog, null);
        EditText etinvoiceNo, etclientName, etdate, etclient_address, etclient_phoneNumber, etamount, etcgst, etclient_email;
        String PushID = mAuth.getCurrentUser().getUid();

        etinvoiceNo = AddView.findViewById(R.id.edit_invoice_no);
        etclientName = AddView.findViewById(R.id.edit_invoice_clientname);
        etdate = AddView.findViewById(R.id.edit_invoice_date);
        etclient_address = AddView.findViewById(R.id.edit_invoice_client_Address);
        etclient_phoneNumber = AddView.findViewById(R.id.edit_invoice_client_mobile);
        etamount = AddView.findViewById(R.id.edit_invoice_amount);
        etcgst = AddView.findViewById(R.id.edit_invoice_cgst);
        etclient_email = AddView.findViewById(R.id.edit_invoice_client_email);
        etinvoiceNo.setEnabled(false);
        // Set initial values to EditText fields
        etinvoiceNo.setText(invoiceFetchModelList.get(Position).getInvoiceNo());
        etclientName.setText(invoiceFetchModelList.get(Position).getClientName());
        etdate.setText(invoiceFetchModelList.get(Position).getDate());
        etclient_address.setText(invoiceFetchModelList.get(Position).getClient_address());
        etclient_phoneNumber.setText(invoiceFetchModelList.get(Position).getClient_phoneNumber());
        etamount.setText(invoiceFetchModelList.get(Position).getAmount());
        etcgst.setText(invoiceFetchModelList.get(Position).getCgst());
        etclient_email.setText(invoiceFetchModelList.get(Position).getClient_email());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(AddView).setPositiveButton("Update",null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();            }
        });
        AlertDialog myDialog = builder.create();
        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button Positive = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference Editdatabase;
                        Editdatabase = FirebaseDatabase.getInstance().getReference();

                        // Retrieve updated values from EditText fields
                        String invoiceNo, clientName, date, client_address, client_phoneNumber, amount, cgst, client_email;
                        etinvoiceNo.setEnabled(true);
                        invoiceNo = etinvoiceNo.getText().toString();
                        clientName = etclientName.getText().toString();
                        date = etdate.getText().toString();
                        client_address = etclient_address.getText().toString();
                        client_phoneNumber = etclient_phoneNumber.getText().toString();
                        amount = etamount.getText().toString();
                        cgst = etcgst.getText().toString();
                        client_email = etclient_email.getText().toString();
                        if(clientName.isEmpty() || client_address.isEmpty() || amount.isEmpty() || cgst.isEmpty()){
                            Toast.makeText(context, "Please Fill All the field ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!isValidMobileNumber(client_phoneNumber)) {
                            etclient_phoneNumber.setError("Please enter a valid Indian mobile number");
                            Toast.makeText(context, "Invalid Mobile", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        if (!isValidEmail(client_email)) {
                            etclient_email.setError("Please enter a valid email address for project manager");
                            Toast.makeText(context, "Invalid Employee Email", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        if(!isValidDate(date)){
                            etdate.setError("Enter Valid Date");
                            return;
                        }
                        if(invoiceNo.isEmpty()){
                            etinvoiceNo.setError("Empty");
                            return;
                        }
                        if(!isValidNumeric(invoiceNo)){
                            etinvoiceNo.setError("Invalid");
                            return;
                        }


                        invoiceAddModel = new InvoiceAddModel(invoiceNo, clientName, date, client_address, client_phoneNumber, amount, cgst, client_email);
                        Editdatabase.child("All Data")

                                .child("invoice")
                                .child(mCompanyPushId)
                                .child(invoiceNo)
                                .setValue(invoiceAddModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
            }
        });
        myDialog.show();
    }

    private void deleteInvoice(String invoiceNo) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String pushID = mAuth.getCurrentUser().getUid();

        DatabaseReference deleteDatabase = FirebaseDatabase.getInstance().getReference();
        deleteDatabase.child("All Data")

                .child("invoice")
                .child(mCompanyPushId)
                .child(invoiceNo)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Invoice Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
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
        // Implement your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Function to validate Indian mobile number format
    private boolean isValidMobileNumber(String mobile) {
        // Indian mobile number should start with 7, 8, or 9 and have a total of 10 digits
        return Pattern.matches("[789]\\d{9}", mobile);
    }
    private boolean isValidNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
