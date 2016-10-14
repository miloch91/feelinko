package com.feelinko.feelinko.dagger2.component;

import android.content.SharedPreferences;

import com.feelinko.feelinko.dagger2.module.SharedPreferencesModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This Dagger component will be used to inject the shared preferences uniformly created
 * This avoids creating a different shared preference that wouldn't be set up correctly
 *
 * @version 1.0
 */
@Singleton
@Component(modules = SharedPreferencesModule.class)
public interface SharedPreferencesComponent {

    // this method is only written to expose the type to the sub component ApiCommunicationComponent.
    // this is done in order to create the AuthHeaderRequestInterceptor with the token if it already exists
    SharedPreferences getSharedPreferences();
}
