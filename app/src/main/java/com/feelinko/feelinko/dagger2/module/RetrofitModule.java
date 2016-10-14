package com.feelinko.feelinko.dagger2.module;

import com.feelinko.feelinko.dagger2.scope.PerRetrofitApiComponent;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * This module is responsible to provide the retrofit instance to the RetrofitApiModule
 * to create the implementations of the retrofit interfaces to request the server.
 * It provides a singleton instance because we do not want to recreate the retrofit instance for each interface implementation
 *
 * @version 1.0
 * @see RetrofitApiModule
 */
@Module
public class RetrofitModule {

    private String mBaseUrl;

    public RetrofitModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides
    @PerRetrofitApiComponent
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
