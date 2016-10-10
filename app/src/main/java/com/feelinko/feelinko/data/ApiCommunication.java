package com.feelinko.feelinko.data;

import com.feelinko.feelinko.dagger2.component.DaggerNetComponent;
import com.feelinko.feelinko.dagger2.component.NetComponent;
import com.feelinko.feelinko.dagger2.module.NetModule;
import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * The ApiCommunication class is the tool we will use to communicate with the server and do our requests
 * It uses A retrofit instance injected with dagger 2
 * @see NetComponent#inject(ApiCommunication)
 * @version 1.0
 */
public class ApiCommunication {

    /**
     * The injected retrofit instance
     */
    @Inject
    Retrofit mRetrofit;

    /**
     * This OkHttp interceptor will be used to update the user's token.
     * It is a singleton injected with dagger
     * @see AuthHeaderRequestInterceptor
     */
    @Inject
    AuthHeaderRequestInterceptor mAuthHeaderRequestInterceptor;

    /**
     * This is the NetComponent instance. We will use this object every time we want to inject the net modules.
     * We use this NetComponent to recreate the retrofit object.
     * @see NetComponent#inject(ApiCommunication)
     */
    private NetComponent mNetComponent;

    /**
     * This is the api url. Since the server isn't ready yet we test our app with a mock api
     */
    private static final String API_BASE_URL = "http://57f7d133f37ebf1100f87837.mockapi.io/api/v1/";

    /**
     * This is the default constructor. Here we create the NetComponent once and for all.
     * Dagger's singleton annotation only works if the component is persisted.
     * It doesn't share the scope throughout different instances of components
     */
    public ApiCommunication() {
        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(API_BASE_URL))
                .build();
        buildRetrofitInstance();
    }

    /**
     * Here we simply inject the Retrofit and AuthHeaderRequestInterceptor.
     * This will recreate a Retrofit Instance every time but it won't recreate the tools to make that Instance
     * because they have a singleton annotation. We use this when we need to update the token for example.
     * Dagger 2 maintains a graph of all the dependencies provided by the modules
     * and know which one to create and which ones to persist with the annotations on top of the provider methods in the modules.
     * @see NetModule
     */
    private void buildRetrofitInstance() {
        mNetComponent.inject(this);
    }

    /**
     * This method is called when we need to update the user's token.
     * @see AuthHeaderRequestInterceptor
     * @param token the user's new token
     */
    public void updateToken(String token) {
        mAuthHeaderRequestInterceptor.setToken(token);
        buildRetrofitInstance();
    }
}
