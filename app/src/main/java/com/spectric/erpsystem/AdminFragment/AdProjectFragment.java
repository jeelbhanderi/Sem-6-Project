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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.ProjectDetailsAdapter;
import com.spectric.erpsystem.Models.ProjectDetsilsFetch;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdProjectFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<ProjectDetsilsFetch> projectDetailsList;
    ProjectDetailsAdapter adapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String PushId = mAuth.getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_project, container, false);
        recyclerView = view.findViewById(R.id.ad_fetch_project_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("Projects");
        fetchData();
        return view;
    }

    private void fetchData() {
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectDetailsList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String companyPushID = dataSnapshot.getKey(); // Get the PushID of the company

                    for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                        // Check if the child node contains a ProjectDetsilsFetch object
                        if (projectSnapshot.getValue(ProjectDetsilsFetch.class) != null) {
                            ProjectDetsilsFetch projectDetails = projectSnapshot.getValue(ProjectDetsilsFetch.class);
                            projectDetailsList.add(projectDetails);
                        }
                    }


                    // Initialize adapter here
                    adapter = new ProjectDetailsAdapter(projectDetailsList, getActivity(),companyPushID);
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
