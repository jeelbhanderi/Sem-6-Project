
package com.spectric.erpsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spectric.erpsystem.Models.Company.CompanyFetchModel;

public class CompanyLoginActivity extends AppCompatActivity {
    EditText etemail,etpassword;
    Button logbtn;
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;
    String Email,Password;
    TextView PageChangetxt,ForgetPasstxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
        etemail = findViewById(R.id.comlogEmail);
        etpassword = findViewById(R.id.comlogPassword);
        logbtn = findViewById(R.id.comlogButton);
        PageChangetxt = findViewById(R.id.LoginPageChangetxt);
        ForgetPasstxt = findViewById(R.id.txt_nav_forget_password);
        ForgetPasstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetintent = new Intent(CompanyLoginActivity.this,Forgot_Password_Activity.class);
                startActivity(forgetintent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email= etemail.getText().toString();
                Password=etpassword.getText().toString();
                mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // User is signed in and email is verified

                            DatabaseReference logref;
                            String LogPushId = FirebaseAuth.getInstance().getUid();
                            logref = FirebaseDatabase.getInstance().getReference();
                            assert LogPushId != null;
                            logref.child("All Data").child("Users").child(LogPushId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        CompanyFetchModel userRole = snapshot.getValue(CompanyFetchModel.class);

                                        if(userRole!=null){
                                            String role = userRole.getRole();
                                            if(role.equals("Admin")){

                                                Intent intent = new Intent(CompanyLoginActivity.this,AdminDashboardActivity.class);
                                                startActivity(intent);
                                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("isLoggedIn", true);
                                                editor.putString("userRole", "Admin");  // Replace with the actual user role
                                                editor.apply();
                                            }
                                            else {

                                                Intent intent = new Intent(CompanyLoginActivity.this,DashBoardActivity.class);
                                                startActivity(intent);
                                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("isLoggedIn", true);
                                                editor.putString("userRole", "Company");  // Replace with the actual user role
                                                editor.apply();
                                            }
                                        }
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });//
                            } else {
                            Toast.makeText(CompanyLoginActivity.this, "User Not verify", Toast.LENGTH_SHORT).show();                        }
//
                    }
                });
            }
        });
        PageChangetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyLoginActivity.this,RegisterCompanyActivity.class);
                startActivity(intent);
            }
        });


    }
}