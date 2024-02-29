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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Adapter.ExpenseAdapter;
import com.spectric.erpsystem.Adapter.IncomeAdapter;
import com.spectric.erpsystem.Models.ExpensesFetchModel;
import com.spectric.erpsystem.Models.FetchIncomeModel;
import com.spectric.erpsystem.R;

import java.util.ArrayList;
import java.util.List;
public class AdBudgetFragment extends Fragment {
    DatabaseReference incomeRef;
    FirebaseAuth mAuth;
    private RecyclerView recyclerView,expensesrecycle;
    private IncomeAdapter incomeAdapter;
    private List<FetchIncomeModel> incomeList;
    private ExpenseAdapter expenseAdapter;
    private List<ExpensesFetchModel> expenseList;

    public AdBudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_budget, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.adrecycleIncome);
        incomeList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        expensesrecycle = view.findViewById(R.id.recycleExpemses);
        expenseList = new ArrayList<>();
        expensesrecycle.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (currentUser != null) {
            incomeRef = FirebaseDatabase.getInstance().getReference()
                    .child("All Data").child("income");

            incomeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        incomeList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String PushId = snapshot.getKey();
                            for (DataSnapshot income : snapshot.getChildren()) {
                                FetchIncomeModel incomeModel = income.getValue(FetchIncomeModel.class);
                                if (incomeModel != null) {
                                    incomeList.add(incomeModel);
                                }
                            }
                            incomeAdapter = new IncomeAdapter(requireContext(), incomeList, PushId);
                            recyclerView.setAdapter(incomeAdapter);
                        }
                        incomeAdapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(getActivity(), "Data not found of income", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch income data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference()
                    .child("All Data").child("Expenses");

            expenseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        expenseList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String PushId = snapshot.getKey();
                            for (DataSnapshot expense : snapshot.getChildren()) {
                                ExpensesFetchModel expenseModel = expense.getValue(ExpensesFetchModel.class);
                                if (expenseModel != null) {
                                    expenseList.add(expenseModel);
                                }
                            }

                            expenseAdapter = new ExpenseAdapter(requireContext(), expenseList, PushId);
                            expensesrecycle.setAdapter(expenseAdapter);
                            expenseAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "data not found of expenses", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch expense data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}
