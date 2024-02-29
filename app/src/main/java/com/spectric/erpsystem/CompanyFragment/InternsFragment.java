    package com.spectric.erpsystem.CompanyFragment;

    import android.annotation.SuppressLint;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.spectric.erpsystem.Adapter.InternsFetchAdapter;
    import com.spectric.erpsystem.Models.Intenrs.InternsAddModel;
    import com.spectric.erpsystem.Models.Intenrs.InternsFetchModel;
    import com.spectric.erpsystem.R;

    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.GregorianCalendar;
    import java.util.List;
    import java.util.regex.Pattern;

    public class InternsFragment extends Fragment {

        RecyclerView recyclerView;
        DatabaseReference madatabase;
        InternsFetchAdapter adapter;
        FirebaseAuth mAuth;
        InternsFetchModel fetchModel;
        List<InternsFetchModel> internsFetchModelList;

        Button AddInternbtn;

        public InternsFragment() {
            // Required empty public constructor
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_interns, container, false);
            recyclerView = view.findViewById(R.id.fetch_interns_recycler);
            AddInternbtn = view.findViewById(R.id.nav_add_interns);
            madatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();

            //AddData
            AddInternbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddIntern();
                }
            });

            //ReadData
            internsFetchModelList = new ArrayList<>();
            String PushID = mAuth.getCurrentUser().getUid();

            madatabase.child("All Data").child("inter_ship").child(PushID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    internsFetchModelList.clear(); // Clear the list before adding data
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            fetchModel = dataSnapshot.getValue(InternsFetchModel.class);
                            if (fetchModel.getPushId().equals(PushID)) {
                                internsFetchModelList.add(fetchModel);
                            }
                        }
                        // Initialize and set the adapter inside onDataChange()
                        adapter = new InternsFetchAdapter(getActivity(), internsFetchModelList,PushID);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });

            return view;
        }
        // Add Data function
        // Add Data function
// Add Data function
        @SuppressLint("MissingInflatedId")
        public void AddIntern() {
            View AddView = LayoutInflater.from(getContext()).inflate(R.layout.interns_add_dialog, null);

            EditText etFullName, etField, etContactNo, etEmailAddress, etFees, etEmployeeEmail, etJoiningDate, etEndDate, etDuration;
            RadioGroup rgGender;
            DatabaseReference mdatabase;
            mdatabase = FirebaseDatabase.getInstance().getReference();
            etFullName = AddView.findViewById(R.id.add_intern_full_name);
            etField = AddView.findViewById(R.id.add_intern_field);
            etContactNo = AddView.findViewById(R.id.add_intern_contact_number);
            etEmailAddress = AddView.findViewById(R.id.add_intern_email);
            etFees = AddView.findViewById(R.id.add_intern_fees);
            etEmployeeEmail = AddView.findViewById(R.id.add_intern_employee_email);
            etJoiningDate = AddView.findViewById(R.id.add_intern_start_date);
            etEndDate = AddView.findViewById(R.id.add_intern_end_date);
            etDuration = AddView.findViewById(R.id.add_intern_duration);
            rgGender = AddView.findViewById(R.id.add_intern_gender);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(AddView).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String fullName, field, gender, contactNo, emailAddress, fees, employeeEmail, joiningDate, endDate, duration, id;

                    fullName = etFullName.getText().toString();
                    field = etField.getText().toString();
                    // Get the selected gender
                    int selectedGenderId = rgGender.getCheckedRadioButtonId();
                    RadioButton selectedGender = AddView.findViewById(selectedGenderId);
                    gender = selectedGender.getText().toString();
                    String PushID = mAuth.getCurrentUser().getUid();
                    contactNo = etContactNo.getText().toString();
                    emailAddress = etEmailAddress.getText().toString();
                    fees = etFees.getText().toString();
                    employeeEmail = etEmployeeEmail.getText().toString();
                    joiningDate = etJoiningDate.getText().toString();
                    endDate = etEndDate.getText().toString();
                    if(!isValidEmail(employeeEmail)){
                        Toast.makeText(getActivity(), "Invalid Employee Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!isValidEmail(emailAddress)){
                        Toast.makeText(getActivity(), "Invalid Intern Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!isValidMobileNumber(contactNo)){
                        Toast.makeText(getActivity(), "Invalid Mobile No", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Validate end date format
                    if (!isValidDate(endDate)) {
                        Toast.makeText(getContext(), "Invalid end date format. Please enter in dd-MM-yyyy format.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate end date is after start date and duration is at least 1 month
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date startDate = sdf.parse(joiningDate);
                        Date endDt = sdf.parse(endDate);

                        if (endDt.before(startDate)) {
                            Toast.makeText(getContext(), "End date must be after start date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Calendar startCalendar = new GregorianCalendar();
                        startCalendar.setTime(startDate);
                        Calendar endCalendar = new GregorianCalendar();
                        endCalendar.setTime(endDt);

                        int months = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                        int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                        int diffMonths = months + years * 12;

                        if (diffMonths < 1) {
                            Toast.makeText(getContext(), "Duration must be at least 1 month", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Calculate duration in months
                        duration = String.valueOf(diffMonths);

                        id = emailAddress.replace(".", "dot");

                        // Example: Create an InternsAddModel object and add to the database
                        InternsAddModel internAddModel = new InternsAddModel(fullName, field, gender, contactNo, emailAddress, fees, employeeEmail, joiningDate, endDate, duration, PushID);

                        mdatabase.child("All Data")
                                .child("inter_ship")
                                .child(PushID)
                                .child(id)
                                .setValue(internAddModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Failed to add data. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User clicked Cancel
                }
            }).create().show();
        }

        // Method to validate date format
        private boolean isValidDate(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        private boolean isValidEmail(String email) {
            // Implement your email validation logic here
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        // Function to validate Indian mobile number format
        private boolean isValidMobileNumber(String mobile) {
            // Indian mobile number should start with 7, 8, or 9 and have a total of 10 digits
            return Pattern.matches("[789]\\d{9}", mobile);
        }


    }
