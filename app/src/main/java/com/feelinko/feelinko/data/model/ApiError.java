package com.feelinko.feelinko.data.model;

import android.support.annotation.Nullable;

import com.feelinko.feelinko.Injection;

import retrofit2.Response;

/**
 * This model class is used to receive the errors encountered by the server.
 * It is generated with the help from {@link com.feelinko.feelinko.data.networkUtils.ErrorConverter#parseError(Response)}.
 * If the error is not from the server we set it to network error or unexpected error.
 * This class can be instantiated without the android framework so we get the default string errors from a {@link StringResources}.
 * The StringResources object is injected depending on the build flavor.
 * In production mode the user will see the string errors from the resource files.
 * In testing mode we use the fakeStringRessource build that enables us to Inject default strings independent of the Android framework.
 * @see Injection
 * @version 1.0
 */
public class ApiError {
    /**
     * String error to store the error encountered by the server
     */
    private String error;

    /**
     * Here we have a StringResources object that will be injected either from prod/java/com/feelinko.feelinko.Injection.java
     * or fakeStringResources/java/com/feelinko.feelinko.Injection.java
     */
    private StringResources mStringResources;

    /**
     * Default constructor to enable the Json converter to construct the object and Inject the StringResources
     */
    public ApiError(){
        mStringResources = Injection.getStringResources();
    }

    /**
     * Get the error String
     * @return the error encountered by the server (error might be null)
     */
    public @Nullable
    String getError() {
        return error;
    }

    /**
     * Set the error String. This will be used by the Json converter if the Json response contains the key 'error'
     * @param error the new error String
     */
    void setError(String error) {
        this.error = error;
    }

    /**
     * This method is called to set the error to a network error
     */
    public void setNetworkError() {
        setError(mStringResources.getNetworkErr());
    }

    /**
     * This method is called to set the error to an unexpected error
     */
    public void setUnexpectedError() {
        setError(mStringResources.getUnexpectedErr());
    }
}
