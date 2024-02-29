package com.spectric.erpsystem.AdminFragment;

import android.os.Bundle;
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
import com.spectric.erpsystem.Adapter.InternsFetchAdapter;
import com.spectric.erpsystem.Models.Intenrs.InternsFetchModel;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdInternsFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<InternsFetchModel> internFetchModelList;
    InternsFetchAdapter adapter;

    public AdInternsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_interns, container, false);
        recyclerView = view.findViewById(R.id.ad_fetch_interns_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("inter_ship");
        fetchData();
        return view;
    }
    private void fetchData() {
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                internFetchModelList = new ArrayList<>();

                for (DataSnapshot internSnapshot : snapshot.getChildren()) {
                    String companyPushID = internSnapshot.getKey(); // Get the PushID of the company

                    for (DataSnapshot data : internSnapshot.getChildren()) {
                        InternsFetchModel internFetchModel = data.getValue(InternsFetchModel.class);
                        if (internFetchModel != null) {
                            internFetchModelList.add(internFetchModel);
                        }
                    }


                    // Initialize adapter here
                    adapter = new InternsFetchAdapter(getActivity(), internFetchModelList,companyPushID);
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
    }

}
