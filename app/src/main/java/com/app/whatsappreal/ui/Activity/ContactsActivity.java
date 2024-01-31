package com.app.whatsappreal.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.app.whatsappreal.Adapters.ContactAdapter;
import com.app.whatsappreal.Models.UsersModel;
import com.app.whatsappreal.databinding.ActivityContactsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ContactsActivity extends AppCompatActivity {
    private ActivityContactsBinding binding;
    private ArrayList<UsersModel> usersModelArrayList;
    private ContactAdapter contactAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar5);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (firebaseUser!=null) {
            getContactList();
        }
    }

    private void getContactList() {
        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshots1:queryDocumentSnapshots) {
                    String userId= Objects.requireNonNull(queryDocumentSnapshots1.get("userId")).toString();
                    String userName= Objects.requireNonNull(queryDocumentSnapshots1.get("userName")).toString();
                    String profileURL= Objects.requireNonNull(queryDocumentSnapshots1.get("imageProfile")).toString();
                    String bio= Objects.requireNonNull(queryDocumentSnapshots1.get("bio")).toString();
                    UsersModel usersModel=new UsersModel();
                    usersModel.setUserId(userId);
                    usersModel.setUserName(userName);
                    usersModel.setImageProfile(profileURL);
                    usersModel.setBio(bio);
                    if (!userId.equals(firebaseUser.getUid())) {
                        usersModelArrayList.add(usersModel);
                    }
                }
                contactAdapter=new ContactAdapter(usersModelArrayList,ContactsActivity.this);
                binding.contactRecyclerView.setAdapter(contactAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("contactlist fetch failed","contact list failed"+e.getMessage());
            }
        });
    }
}