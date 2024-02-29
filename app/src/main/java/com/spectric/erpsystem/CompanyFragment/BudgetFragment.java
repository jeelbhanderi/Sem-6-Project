package com.spectric.erpsystem.CompanyFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.spectric.erpsystem.Models.ExpenseModel;
import com.spectric.erpsystem.Models.ExpensesFetchModel;
import com.spectric.erpsystem.Models.FetchIncomeModel;
import com.spectric.erpsystem.Models.Incomemodel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BudgetFragment extends Fragment {

    private EditText editTextCustomerId;
    private EditText editTextCustomerType;
    private EditText editTextTotalAmount;
    private EditText editTextAdvancePayment;
    private EditText editTextRemainingPayment;
    private EditText editTextCurrentDate;
    DatabaseReference incomedatbase;
    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private IncomeAdapter incomeAdapter;
    private List<FetchIncomeModel> incomeList;

    private DatabaseReference incomeRef;
    private ExpenseAdapter expenseAdapter;
    private List<ExpensesFetchModel> expenseList;

    private DatabaseReference expenseRef;
    public BudgetFragment() {
        // Required empty public constructor
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        mAuth = FirebaseAuth.getInstance();
        String PushID = mAuth.getUid();


        recyclerView = view.findViewById(R.id.recycleIncome);
        incomeList = new ArrayList<>();
        incomeAdapter = new IncomeAdapter(requireContext(), incomeList,PushID);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,true));
        recyclerView.setAdapter(incomeAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            incomeRef = FirebaseDatabase.getInstance().getReference()
                    .child("All Data").child("income").child(currentUser.getUid());

            incomeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    incomeList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FetchIncomeModel income = snapshot.getValue(FetchIncomeModel.class);
                        incomeList.add(income);
                    }
                    incomeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch income data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
//        String PushID = mAuth.getUid();

        RecyclerView expenserecyclerView = view.findViewById(R.id.recycleExpemses);
        expenseList = new ArrayList<>();
        ExpenseAdapter expenseAdapter = new ExpenseAdapter(requireContext(), expenseList,PushID);
        expenserecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,true));
        expenserecyclerView.setAdapter(expenseAdapter);
        mAuth = FirebaseAuth.getInstance();
        if (currentUser != null) {
            expenseRef = FirebaseDatabase.getInstance().getReference()
                    .child("All Data").child("Expenses").child(PushID);

            expenseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    expenseList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ExpensesFetchModel expense = snapshot.getValue(ExpensesFetchModel.class);
                        expenseList.add(expense);
                    }
                    expenseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch expense data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }
        Button addButton = view.findViewById(R.id.budget_add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }

        });
        calculateTotalIncome();
        calculateTotalExpenses();

        return view;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.inflate(R.menu.budget_menu_option);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_add_income) {
                    openIncomeForm();
                    return true;
                } else if (itemId == R.id.action_add_expense) {
                    openExpenseForm();
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }

    private void openIncomeForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.income_add_card, null);
        builder.setView(view);

        editTextCustomerId = view.findViewById(R.id.edit_text_customer_id);
        editTextCustomerType = view.findViewById(R.id.edit_text_customer_type);
        editTextTotalAmount = view.findViewById(R.id.edit_text_total_amount);
        editTextAdvancePayment = view.findViewById(R.id.edit_text_advance_payment);
        editTextRemainingPayment = view.findViewById(R.id.edit_text_remaining_payment);
        editTextCurrentDate = view.findViewById(R.id.edit_text_current_date);// Initialize views

        builder.setTitle("Add Income");
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        incomedatbase = FirebaseDatabase.getInstance().getReference();
                        String Id = editTextCustomerId.getText().toString().replace(".","dot");
                        String customerId = editTextCustomerId.getText().toString();
                        String customerType = editTextCustomerType.getText().toString().trim();
                        String totalAmountStr = editTextTotalAmount.getText().toString().trim();
                        String advancePaymentStr = editTextAdvancePayment.getText().toString().trim();
                        String remainingPaymentStr = editTextRemainingPayment.getText().toString().trim();
                        String currentDate = editTextCurrentDate.getText().toString().trim();

                        if (customerId.isEmpty() || customerType.isEmpty() || totalAmountStr.isEmpty() ||
                                advancePaymentStr.isEmpty() || remainingPaymentStr.isEmpty() || currentDate.isEmpty()) {
                            editTextCustomerType.setError("Invalid type");
                            editTextTotalAmount.setError("Invalid Amount");
                            editTextCustomerId.setError("Invalid Email");
                            editTextCurrentDate.setError("Invallid Date");
                            editTextAdvancePayment.setError("Invalid Amount");
                            editTextRemainingPayment.setError("Invalid Amount");
                            return;
                        }
                        if(!isValidEmail(customerId)){
                            editTextCustomerId.setError("Invalid Email");
                            return;
                        }
                        if(!isValidDate(currentDate)){
                            editTextCurrentDate.setError("Invallid Date");
                            return;
                        }


                        try {
                            double totalAmount = Double.parseDouble(totalAmountStr);
                            double advancePayment = Double.parseDouble(advancePaymentStr);
                            double remainingPayment = Double.parseDouble(remainingPaymentStr);

                            // Validate advanced payment not exceeding total amount
                            if (advancePayment > totalAmount) {
                                editTextTotalAmount.setError("Invalid Total Amount");
                                editTextAdvancePayment.setError("Invalid Total Amount");

                                return;
                            }

                            // Validate sum of advance payment and remaining payment equals total amount
                            if (advancePayment + remainingPayment != totalAmount) {
                                editTextTotalAmount.setError("Invalid Total Amount");
                                return;
                            }

                            // Perform your logic to add income using the provided data
                            // Example:
                            String PushId = mAuth.getCurrentUser().getUid();
                            String totalAmountString = String.valueOf(totalAmount);
                            String advancePaymentString = String.valueOf(advancePayment);
                            String remainingPaymentString = String.valueOf(remainingPayment);
                            Incomemodel income = new Incomemodel( customerType,  customerId,  totalAmountString,  currentDate,  advancePaymentString,  remainingPaymentString);
                            incomedatbase.child("All Data").child("income").child(PushId).child(Id).setValue(income);
                            dialog.dismiss();
                            // Optionally, you can show a success message
                            Toast.makeText(getActivity(), "Income added successfully", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), "Invalid number format", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }
    private void openExpenseForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.expenses_add_card, null);
        builder.setView(view);

        // Initialize views
        EditText editTextCustomerId = view.findViewById(R.id.edit_text_customer_id);
        EditText editTextCustomerType = view.findViewById(R.id.edit_text_customer_type);
        EditText editTextTotalAmount = view.findViewById(R.id.edit_text_total_amount);
        EditText editTextCurrentDate = view.findViewById(R.id.edit_text_current_date);

        builder.setTitle("Add Expense");
        builder.setPositiveButton("Add", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button PositiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                PositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PushId = mAuth.getCurrentUser().getUid();
                        String Id = editTextCustomerId.getText().toString().replace(".","dot");
                        String customerId = editTextCustomerId.getText().toString().trim();
                        String customerType = editTextCustomerType.getText().toString().trim();
                        String totalAmount = editTextTotalAmount.getText().toString().trim();
                        String currentDate = editTextCurrentDate.getText().toString().trim();

                        // Validate input fields
                        if (customerId.isEmpty() || customerType.isEmpty() || totalAmount.isEmpty() || currentDate.isEmpty()) {
                            editTextCustomerType.setError("Invalid type");
                            editTextTotalAmount.setError("In valid Amount");
                            editTextCustomerId.setError("Invalid Email");
                            editTextCurrentDate.setError("Invallid Date");

                            return;
                        }
                        if(!isValidEmail(customerId)){
                            editTextCustomerId.setError("Invalid Email");
                            return;
                        }
                        if(!isValidDate(currentDate)){
                            editTextCurrentDate.setError("Invallid Date");
                            return;
                        }


                        // Perform your logic to add expense using the provided data
                        // Example:
                        DatabaseReference expensePushRef = FirebaseDatabase.getInstance().getReference();

                        ExpenseModel expense = new ExpenseModel(currentDate,customerId, customerType, totalAmount );
                        expensePushRef.child("All Data").child("Expenses").child(PushId).child(Id).setValue(expense);

                        // Optionally, you can show a success message
                        Toast.makeText(requireContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });        alertDialog.show();

    }private void calculateTotalIncome() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference.child("All Data").child("income").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncome = 0;

                // Iterate through each income detail
                for (DataSnapshot incomeSnapshot : dataSnapshot.getChildren()) {
                    FetchIncomeModel incomeModel = incomeSnapshot.getValue(FetchIncomeModel.class);
                    if (incomeModel != null) {
                        try {
                            double totalAmount = Double.parseDouble(incomeModel.getTotalAmount());
                            double remainingPayment = Double.parseDouble(incomeModel.getRemainingPayment());
                            double incomeAmount = totalAmount - remainingPayment;
                            // Add income to the total
                            totalIncome += incomeAmount;
                        } catch (NumberFormatException e) {
                            Log.e("IncomeParsingError", "Failed to parse income amount: " + e.getMessage());
                        }
                    }
                }

                // Set the total income to the TextView
                TextView totalIncomeTextView = requireView().findViewById(R.id.total_income);
                totalIncomeTextView.setText(String.valueOf(totalIncome));
                Log.d("CalculateIncome", "Total income: " + totalIncome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("CalculateIncome", "Failed to calculate total income: " + databaseError.getMessage());
            }
        });
    }

    private void calculateTotalExpenses() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference.child("All Data").child("Expenses").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalExpenses = 0;

                // Iterate through each expense detail
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    ExpensesFetchModel expenseModel = expenseSnapshot.getValue(ExpensesFetchModel.class);
                    if (expenseModel != null) {
                        try {
                            double expenseAmount = Double.parseDouble(expenseModel.getTotalAmount());
                            // Add expense to the total
                            totalExpenses += expenseAmount;
                        } catch (NumberFormatException e) {
                            Log.e("ExpenseParsingError", "Failed to parse expense amount: " + e.getMessage());
                        }
                    }
                }

                // Set the total expenses to the TextView
                TextView totalExpensesTextView = requireView().findViewById(R.id.total_expenses);
                totalExpensesTextView.setText(String.valueOf(totalExpenses));
                Log.d("CalculateExpenses", "Total expenses: " + totalExpenses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("CalculateExpenses", "Failed to calculate total expenses: " + databaseError.getMessage());
            }
        });
    }
    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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


}
