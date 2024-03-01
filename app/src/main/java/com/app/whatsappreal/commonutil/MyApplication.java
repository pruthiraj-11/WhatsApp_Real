package com.app.whatsappreal.commonutil;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LangUtils.applyLocale(this);
    }
}
