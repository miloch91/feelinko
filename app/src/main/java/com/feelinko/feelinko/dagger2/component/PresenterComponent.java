package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.PresenterModule;
import com.feelinko.feelinko.dagger2.scope.PerView;
import com.feelinko.feelinko.login.LoginActivity;

import dagger.Component;

/**
 * This Dagger component is used to inject the presenters inside the views.
 * It also uses the {@link ApiCommunicationComponent} to inject the api inside the presenters.
 *
 * @version 1.0
 */
@PerView
@Component(dependencies = ApiCommunicationComponent.class, modules = PresenterModule.class)
public interface PresenterComponent {

    // This LoginActivity view also needs to inject the SharedPreferences.
    // It can do so because the SharedPreferences type is exposed throughout the parent components
    void injectLoginPresenter(LoginActivity view);
}
