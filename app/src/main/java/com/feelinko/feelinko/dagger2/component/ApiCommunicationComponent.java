package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.ApiCommunicationModule;
import com.feelinko.feelinko.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This Dagger component will be used to inject the singleton
 * {@link com.feelinko.feelinko.data.ApiCommunication} inside the presenters
 * @version 1.0
 */
@Singleton
@Component(modules = ApiCommunicationModule.class)
public interface ApiCommunicationComponent {
    // put classes that require injection here ...
}
