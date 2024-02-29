package com.spectric.erpsystem.AdminFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.spectric.erpsystem.Adapter.ReadDataAdapter;
import com.spectric.erpsystem.Models.Company.CompanyFetchModel;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;

public class FetchAdminCompanyFragment extends Fragment {


    public FetchAdminCompanyFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;

    ReadDataAdapter adapter;
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;
    List<CompanyFetchModel> registermodel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fetch_admin_company, container, false);

        recyclerView = view.findViewById(R.id.companyfetchrecyler);
        registermodel = new ArrayList<>();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("Company");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CompanyFetchModel companyRegisterModel = dataSnapshot.getValue(CompanyFetchModel.class);
                        String mCompanyPushId = dataSnapshot.getKey();

                        if (companyRegisterModel != null) {
                            registermodel.add(companyRegisterModel);
                        }


                    adapter = new ReadDataAdapter(getActivity(), registermodel,mCompanyPushId);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
}            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}