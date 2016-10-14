package com.feelinko.feelinko.data;

import android.support.annotation.Nullable;

import com.feelinko.feelinko.dagger2.component.DaggerRetrofitApiComponent;
import com.feelinko.feelinko.dagger2.component.DaggerRetrofitComponent;

import com.feelinko.feelinko.dagger2.component.DaggerRetrofitDependenciesComponent;

import com.feelinko.feelinko.dagger2.component.RetrofitComponent;
import com.feelinko.feelinko.dagger2.component.RetrofitDependenciesComponent;
import com.feelinko.feelinko.dagger2.module.RetrofitDependenciesModule;
import com.feelinko.feelinko.dagger2.module.RetrofitModule;
import com.feelinko.feelinko.data.model.Auth;
import com.feelinko.feelinko.data.networkUtils.AuthHeaderRequestInterceptor;
import com.feelinko.feelinko.data.retrofitInterfaces.AuthApi;

import javax.inject.Inject;

import retrofit2.http.Body;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The ApiCommunication class is the tool we will use to communicate with the server and do our requests
 * It uses dagger components to inject:
 * the implementations of the retrofit interfaces
 * and the AuthHeaderRequestInterceptor.
 *
 * @version 1.0
 */
public class ApiCommunication implements AuthApi {

    /**
     * This is the api url. Since the server isn't ready yet we test our app with a mock api
     */
    public static final String API_BASE_URL = "http://57f7d133f37ebf1100f87837.mockapi.io/api/v1/";

    /**
     * The injected AuthApi implementation of the AuthApi interface
     * to access the server's authentication endpoints
     */
    @Inject
    AuthApi mAuthApi;

    /**
     * This OkHttp interceptor will be used to update the user's token.
     * It is a singleton injected with dagger
     *
     * @see AuthHeaderRequestInterceptor
     */
    @Inject
    AuthHeaderRequestInterceptor mAuthHeaderRequestInterceptor;

    /**
     * This is the RetrofitDependenciesComponent instance.
     * We will use this object every time we want to inject the retrofit dependencies in the RetrofitComponent.
     */
    private RetrofitDependenciesComponent mRetrofitDependenciesComponent;

    /**
     * This is the default constructor. Here we create the RetrofitDependenciesComponent once and for all.
     * Dagger's singleton annotation only works if the component is persisted.
     * It doesn't share the scope throughout different instances of components.
     * <p>
     * Once the retrofit dependencies are created, we call injectDepencies
     * to create the RetrofitComponent and RetrofitApiComponent and inject the dependencies in this object.
     *
     * @param token the user's server access token from the shared preferences
     * @see ApiCommunication#injectDependencies()
     */
    public ApiCommunication(@Nullable String token) {
        mRetrofitDependenciesComponent = DaggerRetrofitDependenciesComponent.builder()
                .retrofitDependenciesModule(new RetrofitDependenciesModule(token))
                .build();
        injectDependencies();
    }

    /**
     * Here we create the RetrofitComponent and RetrofitApiComponent and then we inject the dependencies
     * into this object. I have separated these 2 components from the RetrofitDependenciesComponent
     * because every time we have a new token, we want to recreate these components and re-inject the
     * retrofit and retrofit interface dependencies. The reason I couldn't just remove the singleton annotation from the
     * Components to recreate them each time we inject is because it is wasteful. It would mean that every time
     * we create an implementation of the retrofit interfaces in the RetrofitApiModule, we would be recreating an instance of Retrofit.
     */
    private void injectDependencies() {

        RetrofitComponent retrofitComponent = DaggerRetrofitComponent.builder()
                .retrofitDependenciesComponent(mRetrofitDependenciesComponent)
                .retrofitModule(new RetrofitModule(API_BASE_URL))
                .build();

        DaggerRetrofitApiComponent.builder()
                .retrofitComponent(retrofitComponent)
                .build().inject(this);
    }

    /**
     * This method is called when we need to update the user's token.
     *
     * @param token the user's new token
     * @see AuthHeaderRequestInterceptor
     */
    public void updateToken(String token) {
        mAuthHeaderRequestInterceptor.setToken(token);
        injectDependencies();
    }

    /**
     * This method is called to prepare the Observable for the presenters.
     * Here we inform the observable that it is too listen to the server's response on new thread
     * but it should return the response on the main thread so the presenter and the views can be synced
     * and view methods can be called from the presenters. View methods should be called on the UI thread (main thread)
     *
     * @param unPreparedObservable the unprepared Observable obtained from the retrofit api implementation methods
     * @return the Observable prepared to do networking on another thread and send the data to the presenters on the main thread.
     */
    private Observable<?> getPreparedObservable(Observable<?> unPreparedObservable) {
        return unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * {@inheritDoc}
     *
     * @param req the request object containing the facebook access token
     */
    @SuppressWarnings("unchecked")
    @Override
    public Observable<Auth.Response> login(@Body Auth.Request req) {
        return (Observable<Auth.Response>) getPreparedObservable(mAuthApi.login(req));
    }
}
