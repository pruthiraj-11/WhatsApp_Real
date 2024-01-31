package com.app.whatsappreal.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.whatsappreal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            new Handler().postAtTime(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            },3000);
        } else {
            new Handler().postAtTime(() -> {
                startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
                finish();
            },3000);
        }
    }
}