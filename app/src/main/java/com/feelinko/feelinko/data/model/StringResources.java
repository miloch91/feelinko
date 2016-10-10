package com.feelinko.feelinko.data.model;

/**
 * This class is used to store the default string error used throughout the project
 * We do not simply use the android resources as we do not have access to them during unit tests
 * During unit tests the android framework is an empty shell and access to the resources result in an null value
 * This class will be injected in the program through {@link com.feelinko.feelinko.Injection}
 * @version 1.0
 */
public class StringResources {
    /**
     * the default network error String
     */
    private String mNetworkErr;
    /**
     * the default unexpected error String
     */
    private String mUnexpectedErr;

    /**
     * The default constructor for this class
     * @param mNetworkErr the network error string value
     * @param mUnexpectedErr the unexpected error string value
     */
    public StringResources(String mNetworkErr, String mUnexpectedErr) {
        this.mNetworkErr = mNetworkErr;
        this.mUnexpectedErr = mUnexpectedErr;
    }

    /**
     * Get the network error
     * @return the network error string
     */
    String getNetworkErr() {
        return mNetworkErr;
    }

    /**
     * Get the unexpected error
     * @return the unexpected error string
     */
    String getUnexpectedErr() {
        return mUnexpectedErr;
    }
}
