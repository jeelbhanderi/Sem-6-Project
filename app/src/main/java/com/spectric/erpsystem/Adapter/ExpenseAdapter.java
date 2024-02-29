package com.spectric.erpsystem.Adapter;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.ExpenseModel;
import com.spectric.erpsystem.Models.ExpensesFetchModel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context mContext;
    private List<ExpensesFetchModel> mExpenseList;
    private String mCompanyPushID;

    public ExpenseAdapter(Context context, List<ExpensesFetchModel> expenseList,String mCompanyPushID) {
        mContext = context;
        mExpenseList = expenseList;
        this.mCompanyPushID = mCompanyPushID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_data_fetch_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpensesFetchModel expense = mExpenseList.get(position);

        holder.FirstDetail.setText("Customer ID: " + expense.getCustomerId());
        holder.SecondDetail.setText("Total Amount: $" + expense.getTotalAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View itemView = inflater.inflate(R.layout.expense_data_card, null);

                TextView customerIdTextView = itemView.findViewById(R.id.text_view_customer_id);
                TextView customerTypeTextView = itemView.findViewById(R.id.text_view_customer_type);
                TextView totalAmountTextView = itemView.findViewById(R.id.text_view_total_amount);
                TextView currentDateTextView = itemView.findViewById(R.id.text_view_current_date);

                customerIdTextView.setText("Customer ID: " + expense.getCustomerId());
                customerTypeTextView.setText("Customer Type: " + expense.getCustomerType());
                totalAmountTextView.setText("Total Amount: $" + expense.getTotalAmount());
                currentDateTextView.setText("Date: " + expense.getCurrentDate());

                builder.setView(itemView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpensesFetchModel incomeModel = mExpenseList.get(holder.getAdapterPosition()); // Get the income model at the clicked position
                openIncomeForm(incomeModel);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                deleteIncome(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView FirstDetail;
        TextView SecondDetail;
        Button Edit,Delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FirstDetail = itemView.findViewById(R.id.txt_first_detail);
            SecondDetail = itemView.findViewById(R.id.txt_second_deltail);
            Edit = itemView.findViewById(R.id.fetch_EditButton);
            Delete = itemView.findViewById(R.id.fetch_DeleteButton);
        }
    }
    private void openIncomeForm(ExpensesFetchModel incomeModel) {
        EditText editTextCustomerId;
        EditText editTextCustomerType;
        EditText editTextTotalAmount;

        EditText editTextCurrentDate;
        DatabaseReference incomedatbase = FirebaseDatabase.getInstance().getReference();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.expenses_add_card, null);
        builder.setView(view);

        editTextCustomerId = view.findViewById(R.id.edit_text_customer_id);
        editTextCustomerType = view.findViewById(R.id.edit_text_customer_type);
        editTextTotalAmount = view.findViewById(R.id.edit_text_total_amount);
        editTextCurrentDate = view.findViewById(R.id.edit_text_current_date);// Initialize views

        editTextCustomerId.setText(incomeModel.getCustomerId());
        editTextCustomerType.setText(incomeModel.getCustomerType());
        editTextTotalAmount.setText(incomeModel.getTotalAmount());
        editTextCurrentDate.setText(incomeModel.getCurrentDate());
        editTextCustomerId.setEnabled(false);

        builder.setTitle("Edit Expenses");
        builder.setPositiveButton("Edit", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog mydialog = builder.create();
        mydialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button PositiveBtn = mydialog.getButton(AlertDialog.BUTTON_POSITIVE);
                PositiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Id = editTextCustomerId.getText().toString().replace(".","dot");
                        String customerId = editTextCustomerId.getText().toString().trim();
                        String customerType = editTextCustomerType.getText().toString().trim();
                        String totalAmountStr = editTextTotalAmount.getText().toString().trim();
                        String currentDate = editTextCurrentDate.getText().toString().trim();

                        // Validate input fields
                        if (customerId.isEmpty() || customerType.isEmpty() || totalAmountStr.isEmpty() || currentDate.isEmpty()) {
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



                        try {

                            String totalAmountString = totalAmountStr;
                            ExpenseModel expenseModel = new ExpenseModel(currentDate,customerId, customerType, totalAmountString);
                            incomedatbase.child("All Data").child("Expenses").child(mCompanyPushID).child(Id).setValue(expenseModel);

                            // Optionally, you can show a success message
                            Toast.makeText(mContext, "Income added successfully", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(mContext, "Invalid number format", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        mydialog.show();
    }

    private void deleteIncome(int position) {
        ExpensesFetchModel incomeToDelete = mExpenseList.get(position);
        String Id = incomeToDelete.getCustomerId().replace(".","dot");

        mExpenseList.remove(position);
        notifyItemRemoved(position);

        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference()
                .child("All Data").child("Expenses").child(mCompanyPushID).child(Id);
        incomeRef.removeValue();

        Toast.makeText(mContext, "Income deleted successfully", Toast.LENGTH_SHORT).show();
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
