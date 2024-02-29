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
import com.spectric.erpsystem.Adapter.EmployeefetchAdater;
import com.spectric.erpsystem.Models.Employee.EmployeeFetchModel;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;

public class AdEmployeeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference mdatabase;
    List<EmployeeFetchModel> employeeFetchModelList;
    EmployeefetchAdater adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_employee, container, false);
        recyclerView = view.findViewById(R.id.ad_fetch_employee_recycler);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("All Data").child("employee");
        fetchData();
        return view;
    }

    private void fetchData() {
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeeFetchModelList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String companyPushID = userSnapshot.getKey(); // Get the PushID of the company

                    for (DataSnapshot employeeSnapshot : userSnapshot.getChildren()) {

                        // Assuming each child under "employee" node represents an employee
                        EmployeeFetchModel employeeFetchModel = employeeSnapshot.getValue(EmployeeFetchModel.class);
                        if (employeeFetchModel != null) {
                            employeeFetchModelList.add(employeeFetchModel);
                        }
                    }


                    // Initialize adapter here
                    adapter = new EmployeefetchAdater(getActivity(), employeeFetchModelList, companyPushID);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdEmployeeFragment", "Failed to fetch data: " + error.getMessage());
            }
        });
    }
}
