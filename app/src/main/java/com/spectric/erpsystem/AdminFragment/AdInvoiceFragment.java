package com.spectric.erpsystem.AdminFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.InvoiceFetchAdapter;
import com.spectric.erpsystem.Models.Invoice.InvoiceFetchModel;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdInvoiceFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<InvoiceFetchModel> invoiceFetchModelList;
    InvoiceFetchAdapter adapter;

    public AdInvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_invoice, container, false);
        recyclerView = view.findViewById(R.id.ad_fetch_invoice_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("invoice");
        fetchData();
        return view;
    }

    private void fetchData() {
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceFetchModelList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String companyPushID = userSnapshot.getKey();
                    for (DataSnapshot invoiceSnapshot : userSnapshot.getChildren()) {
                        InvoiceFetchModel invoiceFetchModel = invoiceSnapshot.getValue(InvoiceFetchModel.class);
                        if (invoiceFetchModel != null) {
                            invoiceFetchModelList.add(invoiceFetchModel);
                        }
                    }


                    // Initialize adapter here
                    adapter = new InvoiceFetchAdapter(getActivity(), invoiceFetchModelList, companyPushID);
                    recyclerView.setHasFixedSize(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdInvoiceFragment", "Failed to fetch data: " + error.getMessage());
            }
        });
    }
}
