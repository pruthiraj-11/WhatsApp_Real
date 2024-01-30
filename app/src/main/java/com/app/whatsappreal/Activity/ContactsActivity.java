package com.app.whatsappreal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivityContactsBinding;

public class ContactsActivity extends AppCompatActivity {
    ActivityContactsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}