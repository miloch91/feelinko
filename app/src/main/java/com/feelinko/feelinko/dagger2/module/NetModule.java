package com.feelinko.feelinko.dagger2.module;

import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;
import com.feelinko.feelinko.data.networkUtils.RxErrorHandlingCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This module defines the way dagger should create the Retrofit instance.
 * This instance is not a singleton as we need to recreate it when the token is updated.
 * The tools to create this instance are singleton to avoid recreating them each time.
 * When the token needs to be updated, all that needs to be done is to get the singleton instance of {@link AuthHeaderRequestInterceptor},
 * add the token and regenerate the retrofit instance.
 * @see com.feelinko.feelinko.data.ApiCommunication#updateToken(String)
 * @see com.feelinko.feelinko.dagger2.component.NetComponent
 * @version 1.0
 */
@Module
public class NetModule {
    private String mBaseUrl;

    public NetModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    AuthHeaderRequestInterceptor provideAuthHeaderInterceptor() {
        return new AuthHeaderRequestInterceptor();
    }

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

    @Provides
    Retrofit provideRetrofit(Converter.Factory gsonConverterFactory,
                             CallAdapter.Factory rxJavaCallAdapterFactory,
                             OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}