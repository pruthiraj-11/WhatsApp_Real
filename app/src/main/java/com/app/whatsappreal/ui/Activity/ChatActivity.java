package com.app.whatsappreal.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.whatsappreal.Adapters.ChatsMessageAdapter;
import com.app.whatsappreal.Models.ChatsMessageModel;
import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivityChatBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String receivedId;
    private ChatsMessageAdapter chatsMessageAdapter;
    private ArrayList<ChatsMessageModel> chatsMessageModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar5);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        chatsMessageModelArrayList= new ArrayList<>();
        Intent intent=new Intent();
        receivedId=intent.getStringExtra("userId");
        String userName=intent.getStringExtra("userName");
        String imageProfile=intent.getStringExtra("imageProfile");
        binding.chatScreenUserName.setText(userName);
        Glide.with(ChatActivity.this).load(imageProfile).placeholder(R.drawable.placeholder_profile).into(binding.chatscreenUserProfile);
        binding.chatBackbtn.setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();
        });
        binding.userMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    binding.voiceMessageBtn.setImageDrawable(AppCompatResources.getDrawable(ChatActivity.this,R.drawable.mic_24px));
                } else {
                    binding.voiceMessageBtn.setImageDrawable(AppCompatResources.getDrawable(ChatActivity.this,R.drawable.send_24px));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.voiceMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBtnClick();
            }
        });
        binding.chatBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ContactsProfileDetailsActivity.class));
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatMessageRecyclerView.setLayoutManager(linearLayoutManager);
        readChats();
    }

    private void readChats() {
        try {
            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatsMessageModelArrayList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        ChatsMessageModel chatsMessageModel=dataSnapshot.getValue(ChatsMessageModel.class);
                        if (Objects.requireNonNull(chatsMessageModel).getSender().equals(firebaseUser.getUid()) && chatsMessageModel.getReceiver().equals(receivedId)
                        || Objects.requireNonNull(chatsMessageModel).getReceiver().equals(firebaseUser.getUid()) && Objects.requireNonNull(chatsMessageModel).getSender().equals(receivedId)) {
                            chatsMessageModelArrayList.add(chatsMessageModel);
                        }
                    }
                    if (chatsMessageAdapter!=null) {
                        chatsMessageAdapter.notifyItemInserted(chatsMessageModelArrayList.size()-1);
                    } else {
                        binding.chatMessageRecyclerView.setAdapter(new ChatsMessageAdapter(chatsMessageModelArrayList,ChatActivity.this));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBtnClick() {
        if (!binding.userMessage.getText().toString().trim().isEmpty()) {
            sendMessage(binding.userMessage.getText().toString().trim());
            binding.userMessage.setText("");
        }
    }

    private void sendMessage(String newMessage){
        Date date= Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String today= simpleDateFormat.format(date);
        Calendar currentDateTime=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH:mm");
        String currentTime=simpleDateFormat1.format(currentDateTime);
        ChatsMessageModel chatsMessageModel=new ChatsMessageModel();
        chatsMessageModel.setDateTime(today+" "+currentTime);
        chatsMessageModel.setTextMessage(newMessage);
        chatsMessageModel.setType("TEXT");
        chatsMessageModel.setSender(firebaseUser.getUid());
        chatsMessageModel.setReceiver(receivedId);
        databaseReference.child("Chats").push().setValue(chatsMessageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child(receivedId);
        databaseReference1.child("chatId").setValue(receivedId);
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child(receivedId).child(firebaseUser.getUid());
        databaseReference1.child("chatId").setValue(firebaseUser.getUid());
    }
}