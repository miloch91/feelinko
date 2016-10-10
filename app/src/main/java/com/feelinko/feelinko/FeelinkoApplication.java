package com.feelinko.feelinko;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.feelinko.feelinko.dagger2.component.ApiCommunicationComponent;
import com.feelinko.feelinko.dagger2.component.DaggerApiCommunicationComponent;

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
     * The api communication component to inject the apiCommunication object inside the different presenters
     */
    private ApiCommunicationComponent mApiCommunicationComponent;

    /**
     * This method is called when the application class is created.
     * Here we initialise the facebook api and our ApiCommunicationComponent
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mApiCommunicationComponent = DaggerApiCommunicationComponent.builder().build();
    }

    /**
     * This method will enable us to get the singleton instance of our Application class
     * It is used for example in our Injection class in production mode to get the network and unexpected error strings
     * @see Injection
     * @return the FeelinkoApplication instance
     */
    public static FeelinkoApplication getInstance() {
        return mInstance;
    }

    /**
     * This method is to get the ApiCommunicationComponent to inject the singleton instance of ApiCommunication in the presenters.
     * We need to preserve it because the singleton scope is not preserved throughout different instances of components
     * @return the ApiCommunicationComponent
     */
    public ApiCommunicationComponent getApiCommunicationComponent() {
        return mApiCommunicationComponent;
    }
}
