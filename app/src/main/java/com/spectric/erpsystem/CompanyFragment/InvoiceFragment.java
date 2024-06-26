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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.InvoiceFetchAdapter;
import com.spectric.erpsystem.Models.Invoice.InvoiceAddModel;
import com.spectric.erpsystem.Models.Invoice.InvoiceFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class InvoiceFragment extends Fragment {
    RecyclerView recyclerView;
    Button Addinvoice;
    DatabaseReference mdatabase;
    List<InvoiceFetchModel> invoiceFetchModelList;
    FirebaseAuth mAuth;

    InvoiceFetchModel fetchModel;

    InvoiceAddModel invoiceAddModel;
    InvoiceFetchAdapter adapter;
    public InvoiceFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_invoice, container, false);

        //Linking And Declare Area
        recyclerView = view.findViewById(R.id.fetch_invoice_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        Addinvoice = view.findViewById(R.id.nav_add_invoice);
        //ReadData

        invoiceFetchModelList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        String PushID = mAuth.getInstance().getCurrentUser().getUid();
        invoiceFetchModelList = new ArrayList<>();
        InvoiceFetchAdapter adapter = new InvoiceFetchAdapter(getContext(),invoiceFetchModelList,PushID);
        mdatabase.child("All Data").child("invoice").child(PushID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        fetchModel = dataSnapshot.getValue(InvoiceFetchModel.class);
                        invoiceFetchModelList.add(fetchModel);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);

                }
                else{
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Add Data
        Addinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddInvoiceDialog();
            }
        });

        return view;
    }
    @SuppressLint("MissingInflatedId")
    private void AddInvoiceDialog(){
        View AddView = LayoutInflater.from(getContext()).inflate(R.layout.invoice_add_dialog,null);
        EditText etinvoiceNo, etclientName, etdate, etclient_address, etclient_phoneNumber, etamount, etcgst,etclient_email;
        String PushID = mAuth.getInstance().getCurrentUser().getUid();
        etinvoiceNo = AddView.findViewById(R.id.edit_invoice_no);
        etclientName = AddView.findViewById(R.id.edit_invoice_clientname);
        etdate = AddView.findViewById(R.id.edit_invoice_date);
        etclient_address = AddView.findViewById(R.id.edit_invoice_client_Address);
        etclient_phoneNumber = AddView.findViewById(R.id.edit_invoice_client_mobile);
        etamount = AddView.findViewById(R.id.edit_invoice_amount);
        etcgst = AddView.findViewById(R.id.edit_invoice_cgst);
        etclient_email = AddView.findViewById(R.id.edit_invoice_client_email);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(AddView).setPositiveButton("Add",null).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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

                        String invoiceNo, clientName, date, client_address, client_phoneNumber, amount, cgst,client_email;
                        invoiceNo = etinvoiceNo.getText().toString();
                        clientName = etclientName.getText().toString();
                        date = etdate.getText().toString();
                        client_address = etclient_address.getText().toString();
                        client_phoneNumber = etclient_phoneNumber.getText().toString();
                        amount = etamount.getText().toString();
                        cgst = etcgst.getText().toString();
                        client_email = etclient_email.getText().toString();
                        if(clientName.isEmpty() || client_address.isEmpty() || amount.isEmpty() || cgst.isEmpty()){
                            Toast.makeText(getActivity(), "Please Fill All the field ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!isValidMobileNumber(client_phoneNumber)) {
                            etclient_phoneNumber.setError("Please enter a valid Indian mobile number");
                            Toast.makeText(getActivity(), "Invalid Mobile", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        if (!isValidEmail(client_email)) {
                            etclient_email.setError("Please enter a valid email address for project manager");
                            Toast.makeText(getActivity(), "Invalid Employee Email", Toast.LENGTH_SHORT).show();

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
                        invoiceAddModel = new InvoiceAddModel(invoiceNo,clientName,date,client_address,client_phoneNumber,amount,cgst,client_email);
                        mdatabase.child("All Data")

                                .child("invoice")
                                .child(PushID)
                                .child(invoiceNo)
                                .setValue(invoiceAddModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Data Added", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Something wrong try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        myDialog.dismiss();
                    }
                });
            }
        });
        myDialog.show();
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