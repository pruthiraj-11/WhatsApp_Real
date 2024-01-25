package com.app.whatsappreal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

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

    }
}