package com.app.whatsappreal.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.whatsappreal.Adapters.ChatListAdapter;
import com.app.whatsappreal.Models.ChatList;
import com.app.whatsappreal.databinding.FragmentChatBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class Chat extends Fragment {

    FragmentChatBinding binding;
    private ArrayList<ChatList> lists;
    private ChatListAdapter chatListAdapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    public Chat() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentChatBinding.inflate(inflater, container, false);

        lists=new ArrayList<>();
        chatListAdapter=new ChatListAdapter(lists,getContext());
        binding.chatrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatrecyclerview.setAdapter(chatListAdapter);
        if (firebaseUser!=null) {
            getChatList();
        }
        return binding.getRoot();
    }

    private void getChatList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lists.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String userId= Objects.requireNonNull(dataSnapshot.child("chatId").getValue()).toString();
                    getUserData(userId);
                }
                binding.chatrecyclerview.setAdapter(chatListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("tag",error.getMessage());
            }
        });
    }

    private void getUserData(String userId) {
        firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatList chatList=new ChatList();
                chatList.setUserId(documentSnapshot.getString("userId"));
                chatList.setUserName(documentSnapshot.getString("userName"));
                chatList.setUserProfileURL(documentSnapshot.getString("userProfileURL"));
                lists.add(chatList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}