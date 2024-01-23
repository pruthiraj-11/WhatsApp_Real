package com.app.whatsappreal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.whatsappreal.Adapters.ChatListAdapter;
import com.app.whatsappreal.Models.ChatList;
import com.app.whatsappreal.databinding.FragmentChatBinding;

import java.util.ArrayList;

public class Chat extends Fragment {

    FragmentChatBinding binding;
    private final ArrayList<ChatList> lists=new ArrayList<>();
    ChatListAdapter chatListAdapter;
    public Chat() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentChatBinding.inflate(inflater, container, false);

        chatListAdapter=new ChatListAdapter(lists,getContext());
        binding.chatrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatrecyclerview.setAdapter(chatListAdapter);
        return binding.getRoot();
    }
}