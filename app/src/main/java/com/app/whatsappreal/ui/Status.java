package com.app.whatsappreal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.whatsappreal.databinding.FragmentStatusBinding;

public class Status extends Fragment {

    FragmentStatusBinding binding;
    public Status() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentStatusBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}