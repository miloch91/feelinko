package com.feelinko.feelinko;

import android.content.Context;

import com.feelinko.feelinko.data.model.StringResources;

/**
 * This class is the injection class for the prod flavor.
 * Note that when we test the app we must use the mock flavor
 *
 * @version 1.0
 */
public class Injection {
    /**
     * This method will set the default values for the network and unexpected errors
     * Since this flavor is to be used in the exported app, we get the error from the android resources.
     *
     * @return a new StringResources object containing the values for the network and unexpected error strings
     * @see StringResources
     */
    public static StringResources getStringResources() {
        Context context = FeelinkoApplication.getInstance();

        return new StringResources(context.getString(R.string.network_error),
                context.getString(R.string.unexpected_error));
    }
}