package com.app.whatsappreal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.app.whatsappreal.Activity.auth.PhoneLoginActivity;
import com.app.whatsappreal.Models.UsersModel;
import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivitySetUserInfoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetUserInfoActivity extends AppCompatActivity {
    private ActivitySetUserInfoBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=new Intent();
        String phoneNumber=intent.getStringExtra("phonenumber");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId;
        if (firebaseUser != null) {
            userId=firebaseUser.getUid();
        }
        firestore=FirebaseFirestore.getInstance();
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.userNameInfo.getText().toString().trim().isEmpty()) {
                    new AlertDialog.Builder(SetUserInfoActivity.this)
                            .setMessage("Please enter your name")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
                } else {
                    UsersModel usersModel=new UsersModel();
                    firestore.collection("Users").document(firebaseUser.getUid()).set(usersModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
    }
}