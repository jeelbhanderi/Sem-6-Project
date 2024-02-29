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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spectric.erpsystem.Models.FetchIncomeModel;
import com.spectric.erpsystem.Models.Incomemodel;
import com.spectric.erpsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    private Context mContext;
    private List<FetchIncomeModel> mIncomeList;
    private String mCompanyPushID;
    public IncomeAdapter(Context context, List<FetchIncomeModel> incomeList,String mCompanyPushID) {
        mContext = context;
        mIncomeList = incomeList;
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
        FetchIncomeModel income = mIncomeList.get(position);

        holder.FirstDeatil.setText("Customer ID"+income.getCustomerId());
        holder.SecondDeatil.setText("Total Amount" + income.getTotalAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView customerIdTextView;
                TextView customerTypeTextView;
                TextView totalAmountTextView;
                TextView advancePaymentTextView;
                TextView remainingPaymentTextView;
                TextView currentDateTextView;
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.income_data_card,null);
                customerIdTextView = itemView.findViewById(R.id.text_view_customer_id);
                customerTypeTextView = itemView.findViewById(R.id.text_view_customer_type);
                totalAmountTextView = itemView.findViewById(R.id.text_view_total_amount);
                advancePaymentTextView = itemView.findViewById(R.id.text_view_advance_payment);
                remainingPaymentTextView = itemView.findViewById(R.id.text_view_remaining_payment);
                currentDateTextView = itemView.findViewById(R.id.text_view_current_date);

                customerIdTextView.setText("Customer ID: " + income.getCustomerId());
                customerTypeTextView.setText("Customer Type: " + income.getCustomerType());
                totalAmountTextView.setText("Total Amount: $" + income.getTotalAmount());
               advancePaymentTextView.setText("Advance Payment: $" + income.getAdvancePayment());
                remainingPaymentTextView.setText("Remaining Payment: $" + income.getRemainingPayment());
                currentDateTextView.setText("Date: " + income.getCurrentDate());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(itemView).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchIncomeModel incomeModel = mIncomeList.get(holder.getAdapterPosition()); // Get the income model at the clicked position
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
        return mIncomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView FirstDeatil;
        TextView SecondDeatil;
        Button Edit,Delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             FirstDeatil = itemView.findViewById(R.id.txt_first_detail);
             SecondDeatil = itemView.findViewById(R.id.txt_second_deltail);
             Edit = itemView.findViewById(R.id.fetch_EditButton);
             Delete = itemView.findViewById(R.id.fetch_DeleteButton);
        }
    }
    private void openIncomeForm(FetchIncomeModel incomeModel) {
         EditText editTextCustomerId;
         EditText editTextCustomerType;
         EditText editTextTotalAmount;
         EditText editTextAdvancePayment;
         EditText editTextRemainingPayment;
         EditText editTextCurrentDate;
         DatabaseReference incomedatbase = FirebaseDatabase.getInstance().getReference();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.income_add_card, null);
        builder.setView(view);

        editTextCustomerId = view.findViewById(R.id.edit_text_customer_id);
        editTextCustomerType = view.findViewById(R.id.edit_text_customer_type);
        editTextTotalAmount = view.findViewById(R.id.edit_text_total_amount);
        editTextAdvancePayment = view.findViewById(R.id.edit_text_advance_payment);
        editTextRemainingPayment = view.findViewById(R.id.edit_text_remaining_payment);
        editTextCurrentDate = view.findViewById(R.id.edit_text_current_date);// Initialize views

        editTextCustomerId.setText(incomeModel.getCustomerId());
        editTextCustomerType.setText(incomeModel.getCustomerType());
        editTextTotalAmount.setText(incomeModel.getTotalAmount());
        editTextAdvancePayment.setText(incomeModel.getAdvancePayment());
        editTextRemainingPayment.setText(incomeModel.getRemainingPayment());
        editTextCurrentDate.setText(incomeModel.getCurrentDate());
        editTextCustomerId.setEnabled(false);

        builder.setTitle("Add Income");
        builder.setPositiveButton("Add", null); // Set the button later
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog myDialog = builder.create();
        myDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button PositiveBtn = myDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                PositiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Id = editTextCustomerId.getText().toString().replace(".","dot");
                        String customerId = editTextCustomerId.getText().toString().trim();
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
                        }


                        try {
                            double totalAmount = Double.parseDouble(totalAmountStr);
                            double advancePayment = Double.parseDouble(advancePaymentStr);
                            double remainingPayment = Double.parseDouble(remainingPaymentStr);

                            // Validate advanced payment not exceeding total amount
                            if (advancePayment > totalAmount) {
                                editTextTotalAmount.setError("Invalid Total Amount");
                                editTextAdvancePayment.setError("Invalid Amount");

                                return;
                            }

                            // Validate sum of advance payment and remaining payment equals total amount
                            if (advancePayment + remainingPayment != totalAmount) {
                                editTextTotalAmount.setError("Invalid Total Amount");
                                return;
                            }

                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String PushId = mAuth.getCurrentUser().getUid();
                            String totalAmountString = String.valueOf(totalAmount);
                            String advancePaymentString = String.valueOf(advancePayment);
                            String remainingPaymentString = String.valueOf(remainingPayment);
                            Incomemodel income = new Incomemodel( customerType,  customerId,  totalAmountString,  currentDate,  advancePaymentString,  remainingPaymentString);
                            incomedatbase.child("All Data").child("income").child(mCompanyPushID).child(Id).setValue(income);

                            // Optionally, you can show a success message
                            Toast.makeText(mContext, "Income added successfully", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(mContext, "Invalid number format", Toast.LENGTH_SHORT).show();
                        }
                        myDialog.dismiss();;
                    }
                });
            }
        });
        myDialog.show();
    }

    private void deleteIncome(int position) {
        FetchIncomeModel incomeToDelete = mIncomeList.get(position);

        String Email = incomeToDelete.getCustomerId();
        String id  = Email.replace(".","dot");
        mIncomeList.remove(position);
        notifyItemRemoved(position);

        DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference()
                .child("All Data").child("income").child(mCompanyPushID).child(id);
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
