package com.app.whatsappreal.ui.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.whatsappreal.commonutil.Common;
import com.app.whatsappreal.databinding.ActivityViewProfilePicBinding;

public class ViewProfilePicActivity extends AppCompatActivity {
    ActivityViewProfilePicBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewProfilePicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewZoom.setImageBitmap(Common.IMAGE_BITMAP);
    }
}