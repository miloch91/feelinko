package com.feelinko.feelinko.dagger2.module;

import com.feelinko.feelinko.dagger2.scope.PerView;
import com.feelinko.feelinko.data.ApiCommunication;
import com.feelinko.feelinko.login.LoginContract;
import com.feelinko.feelinko.login.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * This module is responsible to provide the presenters dependencies to all the views
 */
@Module
public class PresenterModule {

    @Provides
    @PerView
    LoginContract.Presenter provideLoginPresenter(ApiCommunication apiCommunication) {
        return new LoginPresenter(apiCommunication);
    }
}
