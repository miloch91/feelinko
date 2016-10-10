package com.feelinko.feelinko.dagger2.module;

import com.feelinko.feelinko.data.ApiCommunication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module defines the way dagger should create the api communication object that will be injected the presenters
 * @see ApiCommunication
 * @see com.feelinko.feelinko.dagger2.component.ApiCommunicationComponent
 * @version 1.0
 */
@Module
public class ApiCommunicationModule {

    @Provides
    @Singleton
    ApiCommunication provideApiCommunication() {
        return new ApiCommunication();
    }
}
