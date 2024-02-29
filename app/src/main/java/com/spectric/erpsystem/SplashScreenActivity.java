package com.spectric.erpsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                    Intent intent = new Intent(SplashScreenActivity.this,AdminDashboardActivity.class);
//                    startActivity(intent);
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                String userRole = sharedPreferences.getString("userRole", "");
                if (isLoggedIn) {
                    if ("Company".equals(userRole)) {
                        Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                    } else if ("Admin".equals(userRole)) {
                        Intent intent = new Intent(SplashScreenActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, CompanyLoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, CompanyLoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }


        }, 5000);
    }
}