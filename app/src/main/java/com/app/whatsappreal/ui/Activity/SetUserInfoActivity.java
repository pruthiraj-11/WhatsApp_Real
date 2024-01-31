package com.app.whatsappreal.ui.Activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.whatsappreal.Models.UsersModel;
import com.app.whatsappreal.R;
import com.app.whatsappreal.databinding.ActivitySetUserInfoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity {
    private ActivitySetUserInfoBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private BottomSheetDialog bottomSheetDialog;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityResultLauncher<String> activityResultLauncher;
    private String userProfileURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=new Intent();
        String phoneNumber=intent.getStringExtra("phonenumber");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        if (firebaseUser != null) {
            //todo
        }
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), o -> {
            if(o!=null){
                binding.initialUserProfileImage.setImageURI(o);
                Bitmap bitmap = null;
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                try {
                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, o);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, o);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                    uploadImage(bitmap);
                } catch (Exception e) {
                    Log.d("tag", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode()== RESULT_OK && o.getData()!=null){
                Bundle bundle=o.getData().getExtras();
                Bitmap bitmap= null;
                if (bundle != null) {
                    bitmap = (Bitmap) bundle.get("data");
                }
                if (bitmap != null) {
                    binding.initialUserProfileImage.setImageBitmap(bitmap);
                    uploadImage(bitmap);
                }
            }
        });
        binding.nextBtn.setOnClickListener(v -> {
            if (binding.userNameInfo.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(SetUserInfoActivity.this)
                        .setMessage("Please enter your name")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
            } else {
                UsersModel usersModel=new UsersModel();
                usersModel.setUserPhone(phoneNumber);
                usersModel.setUserId(firebaseUser.getUid());
                usersModel.setUserName(binding.userNameInfo.getText().toString().trim());
                usersModel.setImageProfile(userProfileURL);
                firestore.collection("Users").document(firebaseUser.getUid()).set(usersModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new AlertDialog.Builder(SetUserInfoActivity.this)
                                .setMessage("Try again")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
                    }
                });
            }
        });
        binding.initialUserProfileImage.setOnClickListener(v -> {
            showBottomSheetDialog();
        });
    }

    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        final StorageReference storageReference= firebaseStorage.getReference().child("user_profile_pic").child(firebaseUser.getUid());
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    userProfileURL=uri.toString();
                    Toast.makeText(SetUserInfoActivity.this, "Profile photo updated", Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(SetUserInfoActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show());
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.media_pick_bottom_sheet_dialog);
        bottomSheetDialog.findViewById(R.id.camera_pick).setOnClickListener(view13 -> {
            bottomSheetDialog.dismiss();
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                launcher.launch(intent);
            } catch (ActivityNotFoundException e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.findViewById(R.id.gallery_pick).setOnClickListener(view12 -> {
            bottomSheetDialog.dismiss();
            activityResultLauncher.launch("image/*");
        });
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(dialog -> bottomSheetDialog=null);
    }
}