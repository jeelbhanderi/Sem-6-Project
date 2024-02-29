package com.spectric.erpsystem.AdminFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.spectric.erpsystem.Adapter.ClientFetchAdapter;
import com.spectric.erpsystem.Models.Client.ClientAddModel;
import com.spectric.erpsystem.Models.Client.ClientFetchModel;
import com.spectric.erpsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdClientFragment extends Fragment {
    Button NavAddClient;
    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<ClientFetchModel> clientFetchModelList;

    DatabaseReference mDatabaseAdd;
    FirebaseAuth mAuth;
    ClientAddModel clientAddModel;
    ClientFetchAdapter adapter;
    public  AdClientFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_client, container, false); NavAddClient = view.findViewById(R.id.nav_add_client);
        recyclerView = view.findViewById(R.id.ad_fetch_client_recycler);
        mAuth= FirebaseAuth.getInstance();
//        String PushID = mAuth.getInstance().getCurrentUser().getUid();

        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("clients");

        // Fetch Data for all clients
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clientFetchModelList = new ArrayList<>();

                for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                    String companyPushID = companySnapshot.getKey(); // Get the PushID of the company
                    for (DataSnapshot clientSnapshot : companySnapshot.getChildren()) {
                        ClientFetchModel clientFetchModel = clientSnapshot.getValue(ClientFetchModel.class);
                        if (clientFetchModel != null) {
                            clientFetchModelList.add(clientFetchModel);
                        }
                    }
                    // Initialize adapter here with the company's PushID
                    adapter = new ClientFetchAdapter(getActivity(), clientFetchModelList, companyPushID);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //AddData
        View Addview = LayoutInflater.from(getActivity()).inflate(R.layout.client_add_dialog, null);
        mDatabaseAdd = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        return view;
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }







}