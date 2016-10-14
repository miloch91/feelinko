package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.RetrofitDependenciesModule;
import com.feelinko.feelinko.data.ApiCommunication;
import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * This component will be used to inject all the retrofit dependencies to create the retrofit instance
 * in the {@link RetrofitComponent}. It also provides the singleton instance of the {@link AuthHeaderRequestInterceptor}
 * that will be injected in the {@link ApiCommunication} object.
 *
 * @version 1.0
 */
@Singleton
@Component(modules = RetrofitDependenciesModule.class)
public interface RetrofitDependenciesComponent {

    // these methods are not being called as is. They are only present to expose the type of the
    // dependencies that should be available to the sub components. Their name is irrelevant
    Converter.Factory getGsonConverterFactory();

    CallAdapter.Factory getRxJavaCallAdapterFactory();

    OkHttpClient getOkHttpClient();

    AuthHeaderRequestInterceptor getAuthRequestIntercceptor();
}