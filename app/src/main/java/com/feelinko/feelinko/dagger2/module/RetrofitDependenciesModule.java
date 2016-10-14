package com.feelinko.feelinko.dagger2.module;

import android.support.annotation.Nullable;

import com.feelinko.feelinko.dagger2.component.RetrofitDependenciesComponent;
import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;
import com.feelinko.feelinko.data.networkUtils.RxErrorHandlingCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This module defines the way dagger should create the Retrofit dependencies.
 * These dependencies are mostly singleton to avoid recreating them each time.
 * When the token needs to be updated, all that needs to be done is to get the singleton instance
 * of {@link AuthHeaderRequestInterceptor}, update the token
 * and regenerate the retrofit instance as well as the interface implementations which depend on the retrofit instance.
 *
 * @version 1.0
 * @see com.feelinko.feelinko.data.ApiCommunication#updateToken(String)
 * @see RetrofitDependenciesComponent
 */
@Module
public class RetrofitDependenciesModule {

    private String mToken;

    public RetrofitDependenciesModule(@Nullable String token) {
        this.mToken = token;
    }

    @Provides
    @Singleton
    AuthHeaderRequestInterceptor provideAuthHeaderInterceptor() {
        return new AuthHeaderRequestInterceptor(mToken);
    }

    // this method doesn't provide a singleton instance
    // because we want to recreate the OkHttpClient whenever the token is updated
    @Provides
    OkHttpClient provideOkHttpClient(AuthHeaderRequestInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxErrorHandlingCallAdapterFactory() {
        return RxErrorHandlingCallAdapterFactory.create();
    }
}