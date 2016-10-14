package com.feelinko.feelinko.dagger2.component;

import android.content.SharedPreferences;

import com.feelinko.feelinko.dagger2.module.ApiCommunicationModule;
import com.feelinko.feelinko.dagger2.scope.PerApplication;
import com.feelinko.feelinko.data.ApiCommunication;

import dagger.Component;

/**
 * This Dagger component is used to inject the singleton
 * {@link com.feelinko.feelinko.data.ApiCommunication} inside the presenters.
 *
 * @version 1.0
 */
@PerApplication
@Component(dependencies = SharedPreferencesComponent.class, modules = ApiCommunicationModule.class)
public interface ApiCommunicationComponent {

    // Downstream components need the type exposed for dependency injection
    // method name does not really matter

    ApiCommunication getApiCommunication();

    // We give access to the shared preferences dependency here
    // because some of the views injected in the sub component PresenterComponent need access.
    // This dependency will be provided from the SharedPreferencesComponent from which this component depends
    SharedPreferences getSharedPreferences();
}
