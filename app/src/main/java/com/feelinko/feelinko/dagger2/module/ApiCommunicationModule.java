package com.feelinko.feelinko.dagger2.module;

import android.content.SharedPreferences;

import com.feelinko.feelinko.dagger2.scope.PerApplication;
import com.feelinko.feelinko.data.ApiCommunication;

import dagger.Module;
import dagger.Provides;

/**
 * This module defines the way dagger should create the api communication object that will be injected in the presenters
 *
 * @version 1.0
 * @see ApiCommunication
 * @see com.feelinko.feelinko.dagger2.component.ApiCommunicationComponent
 */
@Module
public class ApiCommunicationModule {

    @Provides
    @PerApplication
    ApiCommunication provideApiCommunication(SharedPreferences sharedPreferences) {
        return new ApiCommunication(sharedPreferences.getString(SharedPreferencesModule.TOKEN_SHARED_PREFERENCES_TAG, null));
    }
}
