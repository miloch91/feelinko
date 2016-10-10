package com.feelinko.feelinko;

import com.feelinko.feelinko.data.model.StringResources;

/**
 * This class is the injection class for the fakeStringRessources flavor.
 * Note that when we export the app we must use the prod flavor
 *
 * @version 1.0
 */
public class Injection {
    /**
     * This method will set the default values for the network and unexpected errors
     * Since this flavor is to be used in unit tests and we don't have access to the android framework,
     * we create fake error strings.
     *
     * @return a new StringResources object containing the values for the network and unexpected error strings
     * @see StringResources
     */
    public static StringResources getStringResources() {
        return new StringResources("networkError", "unexpectedError");
    }
}
