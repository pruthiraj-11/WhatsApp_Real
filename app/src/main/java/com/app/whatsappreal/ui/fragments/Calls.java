package com.app.whatsappreal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.whatsappreal.Adapters.CallListAdapter;
import com.app.whatsappreal.Models.CallList;
import com.app.whatsappreal.databinding.FragmentCallsBinding;

import java.util.ArrayList;

public class Calls extends Fragment {
    FragmentCallsBinding binding;
    private ArrayList<CallList> callLists;
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
        binding.callRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.callRecyclerView.setAdapter(new CallListAdapter(callLists,getContext()));
        return binding.getRoot();
    }
}