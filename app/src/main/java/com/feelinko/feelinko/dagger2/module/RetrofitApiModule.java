package com.feelinko.feelinko.dagger2.module;

import com.feelinko.feelinko.dagger2.scope.PerApiCommunication;
import com.feelinko.feelinko.data.ApiCommunication;
import com.feelinko.feelinko.data.retrofitInterfaces.AuthApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * This module defines the way dagger should create the implementation of the retrofit interfaces
 * that will be injected the ApiCommunication class
 * @see ApiCommunication
 * @version 1.0
 */
@Module
public class RetrofitApiModule {

    @Provides
    @PerApiCommunication
    AuthApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
}
