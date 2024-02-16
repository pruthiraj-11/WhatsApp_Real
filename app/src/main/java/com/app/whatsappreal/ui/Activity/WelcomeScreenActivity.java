package com.app.whatsappreal.ui.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.whatsappreal.R;
import com.app.whatsappreal.Util.UtilClass;
import com.app.whatsappreal.ui.Activity.auth.PhoneLoginActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    private CardView appLanguageChangeCardLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        String [] permissionList1={Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.RECORD_AUDIO
        };
        String [] permissionList2={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
        };
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            UtilClass.requestPermissions(getApplicationContext(),permissionList1);
        } else {
            UtilClass.requestPermissions(getApplicationContext(),permissionList2);
        }
        appLanguageChangeCardLayout=findViewById(R.id.app_locale_change);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeScreenActivity.this, PhoneLoginActivity.class));
            finish();
        });
        appLanguageChangeCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAppLanguage();
            }
        });
    }

    private void changeAppLanguage() {
        //todo
    }
}