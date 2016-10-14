package com.feelinko.feelinko.dagger2.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * This module is responsible to provide the same shared preferences throughout the application
 */
@Module
public class SharedPreferencesModule {

    private Context mContext;

    private static final String APP_SHARED_PREFERENCES_TAG = "Feelinko_SharedPreferences";
    public static final String TOKEN_SHARED_PREFERENCES_TAG = "token_SharedPreferences";
    public static final String REFRESH_TOKEN_SHARED_PREFERENCES_TAG = "refresh_token_SharedPreferences";

    public SharedPreferencesModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return mContext.getSharedPreferences(APP_SHARED_PREFERENCES_TAG, MODE_PRIVATE);
    }
}
