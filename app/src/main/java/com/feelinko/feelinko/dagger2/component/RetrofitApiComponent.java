package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.RetrofitApiModule;
import com.feelinko.feelinko.dagger2.scope.PerApiCommunication;
import com.feelinko.feelinko.data.ApiCommunication;

import dagger.Component;

/**
 * This Dagger component is used to inject the implementations of the Retrofit interfaces
 * inside the ApiCommunication class.
 *
 * @version 1.0
 */
@PerApiCommunication
@Component(dependencies = RetrofitComponent.class, modules = RetrofitApiModule.class)
public interface RetrofitApiComponent {

    // The ApiCommunication class also needs to inject the AuthHeaderRequestInterceptor
    // It will be able to do so because the AuthHeaderRequestInterceptor is exposed throughout the parent components
    void inject(ApiCommunication apiCommunication);
}
