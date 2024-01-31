package com.app.whatsappreal.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.app.whatsappreal.Models.ChatList;
import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivityChatBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar5);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Intent intent=new Intent();
        String receivedId=intent.getStringExtra("userId");
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
    }

    private void initBtnClick() {
        if (!binding.userMessage.getText().toString().trim().isEmpty()) {
            sendMessage(binding.userMessage.getText().toString().trim());
        }
    }

    private void sendMessage(String newMessage){
        Date date= Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String today= simpleDateFormat.format(date);
        Calendar currentDateTime=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH:mm");
        String currentTime=simpleDateFormat1.format(currentDateTime);
        ChatList chatList=new ChatList();
        chatList.setLastMessageDate(today+" "+currentTime);
        chatList.setLastMessage(newMessage);

    }
}