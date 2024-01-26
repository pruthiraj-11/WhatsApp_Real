package com.app.whatsappreal.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.app.whatsappreal.Activity.auth.PhoneLoginActivity;
import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivitySetUserInfoBinding;

public class SetUserInfoActivity extends AppCompatActivity {
    private ActivitySetUserInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=new Intent();
        String phoneNumber=intent.getStringExtra("phonenumber");
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.userNameInfo.getText().toString().trim().isEmpty()) {
                    new AlertDialog.Builder(SetUserInfoActivity.this)
                            .setMessage("Please enter your name")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
                }
            }
        });
    }
}