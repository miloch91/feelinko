package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.RetrofitModule;
import com.feelinko.feelinko.dagger2.scope.PerRetrofitApiComponent;
import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * This Dagger component is used to inject the Retrofit instance inside the RetrofitApiComponent.
 * It also exposes the {@link AuthHeaderRequestInterceptor} to the sub components in order to inject
 * it in the {@link com.feelinko.feelinko.data.ApiCommunication } class through the RetrofitApiComponent .
 *
 * @version 1.0
 * @see RetrofitApiComponent
 */
@PerRetrofitApiComponent
@Component(dependencies = RetrofitDependenciesComponent.class, modules = RetrofitModule.class)
public interface RetrofitComponent {

    Retrofit getRetrofit();

    AuthHeaderRequestInterceptor getAuthHeaderRequestInterceptor();
}
