package com.feelinko.feelinko;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.feelinko.feelinko.dagger2.component.ApiCommunicationComponent;
import com.feelinko.feelinko.dagger2.component.DaggerApiCommunicationComponent;
import com.feelinko.feelinko.dagger2.component.DaggerPresenterComponent;
import com.feelinko.feelinko.dagger2.component.DaggerSharedPreferencesComponent;
import com.feelinko.feelinko.dagger2.component.PresenterComponent;
import com.feelinko.feelinko.dagger2.component.SharedPreferencesComponent;
import com.feelinko.feelinko.dagger2.module.SharedPreferencesModule;

/**
 * This is the application class. It is instantiated when the application is started
 * and will persist until the application is not running anymore.
 * It is a singleton
 */
public class FeelinkoApplication extends Application {

    /**
     * The singleton instance
     */
    private static FeelinkoApplication mInstance;

    /**
     * The presenterComponent to inject all the presenters to the views.
     * We store the presenterComponent in the application class because we need to inject singleton presenter's
     * and to do so, we must save the component. By putting the component in the application class,
     * we know that we will always be able to get a references to it
     * and so we will always be able to inject our singleton presenters inside the views.
     */
    private PresenterComponent mPresenterComponent;

    /**
     * This method is called when the application singleton class is created.
     * Here we initialise the facebook api and our PresenterComponent.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        SharedPreferencesComponent sharedPreferencesComponent = DaggerSharedPreferencesComponent.builder()
                .sharedPreferencesModule(new SharedPreferencesModule(this))
                .build();

        ApiCommunicationComponent apiCommunicationComponent = DaggerApiCommunicationComponent.builder()
                .sharedPreferencesComponent(sharedPreferencesComponent).build();

        mPresenterComponent = DaggerPresenterComponent.builder()
                .apiCommunicationComponent(apiCommunicationComponent)
                .build();
    }

    /**
     * This method will enable us to get the singleton instance of our Application class
     * It is used for example in our Injection class in production mode to get the network and unexpected error strings
     *
     * @return the FeelinkoApplication instance
     * @see Injection
     */
    public static FeelinkoApplication getInstance() {
        return mInstance;
    }

    /**
     * This method is used to get the presenterComponent.This is done to inject the singleton presenters in the views.
     *
     * @return the presenterComponent
     */
    public PresenterComponent getPresenterComponent() {
        return mPresenterComponent;
    }
}
