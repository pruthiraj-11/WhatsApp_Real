package com.app.whatsappreal.Activity.SettingsActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.app.whatsappreal.Activity.ViewProfilePicActivity;
import com.app.whatsappreal.R;
import com.app.whatsappreal.commonutil.Common;
import com.app.whatsappreal.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private FirebaseStorage firebaseStorage;
    private BottomSheetDialog bottomSheetDialog;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityResultLauncher<String> activityResultLauncher;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar3);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(this);
        if (firebaseUser!=null) {
            getInfo();
        }
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(), o -> {
            if(o!=null){
                binding.profileActivityUserpic.setImageURI(o);
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
                    Toast.makeText(this, "Failed to process image.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,"Profile picture not picked.",Toast.LENGTH_SHORT).show();
            }
        });

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
            if (o.getResultCode()== RESULT_OK && o.getData()!=null){
                Bundle bundle=o.getData().getExtras();
                Intent intent=o.getData();
                Bitmap bitmap= null;
                if (bundle != null) {
                    bitmap = (Bitmap) bundle.get("data");
                }
                if (bitmap != null) {
                    binding.profileActivityUserpic.setImageBitmap(bitmap);
                    uploadImage(bitmap);
                }
            } else {
                Toast.makeText(this,"Profile picture not captured.",Toast.LENGTH_SHORT).show();
            }
        });
        binding.addprofileuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        binding.profileActivityUserpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap=((BitmapDrawable)binding.profileActivityUserpic.getDrawable()).getBitmap();
                Common.IMAGE_BITMAP=bitmap;
                ActivityOptionsCompat activityOptionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, binding.profileActivityUserpic,"image");
                Intent intent=new Intent(ProfileActivity.this, ViewProfilePicActivity.class);
                startActivity(intent,activityOptionsCompat.toBundle());
            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void uploadImage(Bitmap bitmap) {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        final StorageReference storageReference= firebaseStorage.getReference().child("user_profile_pic").child(firebaseUser.getUid());
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Toast.makeText(ProfileActivity.this,"Profile picture uploaded successfully",Toast.LENGTH_SHORT).show();
                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("imageProfile",uri.toString());
                    progressDialog.dismiss();
                    firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ProfileActivity.this,"Profile picture uploaded successfully",Toast.LENGTH_SHORT).show();
                            getInfo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           progressDialog.dismiss();
                           Toast.makeText(ProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        }).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show());

        binding.userNameEditlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogEditName();
            }
        });
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
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog=null;
            }
        });
    }

    private void showBottomSheetDialogEditName() {
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_edit_name);
        EditText editText=getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name,null).findViewById(R.id.editTextText_new_username);
        bottomSheetDialog.findViewById(R.id.user_editname_savebtn).setOnClickListener(view13 -> {
            if (!editText.getText().toString().isEmpty()) {
                updateUserName(editText.getText().toString().trim());
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.user_editname_cancelbtn).setOnClickListener(view12 -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog=null;
            }
        });
    }

    private void updateUserName(String newUserName) {
        firestore.collection("Users").document(firebaseUser.getUid()).update("userName",newUserName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this, "Updated.", Toast.LENGTH_SHORT).show();
                getInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("update name data", "onFailure"+Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username= Objects.requireNonNull(documentSnapshot.get("userName")).toString();
                String userphone= Objects.requireNonNull(documentSnapshot.get("userPhone")).toString();
                String about= Objects.requireNonNull(documentSnapshot.get("bio")).toString();
                String imageProfile= Objects.requireNonNull(documentSnapshot.get("imageProfile")).toString();
                Glide.with(getApplicationContext()).load(imageProfile).placeholder(R.drawable.placeholder_profile).into(binding.profileActivityUserpic);
                binding.profileActivityUsername.setText(username);
                binding.profileActivityAbout.setText(about);
                binding.profileActivityUserphone.setText(userphone);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("get data", "onFailure"+Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}