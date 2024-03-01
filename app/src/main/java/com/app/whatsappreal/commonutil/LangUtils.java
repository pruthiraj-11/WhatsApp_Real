package com.app.whatsappreal.commonutil;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;
import androidx.core.os.BuildCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.app.whatsappreal.R;

import java.util.LinkedHashSet;
import java.util.Locale;

public class LangUtils {
    public static final String LANG_AUTO = "auto";
    public static final String LANG_DEFAULT = "en";
    private static ArrayMap<String, Locale> sLocaleMap;

    public static void setAppLanguages(@NonNull Context context) {
        if (sLocaleMap == null) sLocaleMap = new ArrayMap<>();
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        String[] locales = context.getResources().getStringArray(R.array.languages_key);
        Locale appDefaultLocale = Locale.forLanguageTag(LANG_DEFAULT);
        for (String locale : locales) {
            conf.setLocale(Locale.forLanguageTag(locale));
            Context ctx = context.createConfigurationContext(conf);
            String langTag = ctx.getString(R.string._lang_tag);
            if (LANG_AUTO.equals(locale)) {
                sLocaleMap.put(LANG_AUTO, null);
            } else if (LANG_DEFAULT.equals(langTag)) {
                sLocaleMap.put(LANG_DEFAULT, appDefaultLocale);
            } else sLocaleMap.put(locale, ConfigurationCompat.getLocales(conf).get(0));
        }
    }

    @NonNull
    public static ArrayMap<String, Locale> getAppLanguages(@NonNull Context context) {
        if (sLocaleMap == null) setAppLanguages(context);
        return sLocaleMap;
    }

    @NonNull
    public static Locale getFromPreference(@NonNull Context context) {
        if (BuildCompat.isAtLeastT()) {
            Locale locale = AppCompatDelegate.getApplicationLocales().getFirstMatch(getAppLanguages(context).keySet()
                    .toArray(new String[0]));
            if (locale != null) {
                return locale;
            }
        }
        String language = "";// TODO: Fetch current language from the shared preferences
        Locale locale = getAppLanguages(context).get(language);
        if (locale != null) {
            return locale;
        }
        Configuration conf = Resources.getSystem().getConfiguration();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? conf.getLocales().get(0) : conf.locale;
    }

    static Locale applyLocale(Context context) {
        return applyLocale(context, LangUtils.getFromPreference(context));
    }

    public static Locale applyLocale(@NonNull Context context, @NonNull Locale locale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale));
        updateResources(context.getApplicationContext(), locale);
        return locale;
    }

    private static void updateResources(@NonNull Context context, @NonNull Locale locale) {
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        Locale current = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? conf.getLocales().get(0) : conf.locale;
        if (current == locale) {
            return;
        }
        conf = new Configuration(conf);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setLocaleApi24(conf, locale);
        } else {
            conf.setLocale(locale);
        }
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    private static void setLocaleApi24(@NonNull Configuration config, @NonNull Locale locale) {
        LocaleList defaultLocales = LocaleList.getDefault();
        LinkedHashSet<Locale> locales = new LinkedHashSet<>(defaultLocales.size() + 1);
        locales.add(locale);
        for (int i = 0; i < defaultLocales.size(); ++i) {
            locales.add(defaultLocales.get(i));
        }
        config.setLocales(new LocaleList(locales.toArray(new Locale[0])));
    }
}
