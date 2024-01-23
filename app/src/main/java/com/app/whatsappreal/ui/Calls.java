package com.app.whatsappreal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.whatsappreal.databinding.FragmentCallsBinding;

public class Calls extends Fragment {
    FragmentCallsBinding binding;
    public Calls() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentCallsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}