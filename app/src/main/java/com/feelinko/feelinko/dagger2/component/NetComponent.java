package com.feelinko.feelinko.dagger2.component;

import com.feelinko.feelinko.dagger2.module.NetModule;
import com.feelinko.feelinko.data.ApiCommunication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This component will be used to inject the retrofit singleton instance inside the ApiCommunication class
 * @see ApiCommunication#buildRetrofitInstance()
 * @version 1.0
 */
@Singleton
@Component(modules = NetModule.class)
public interface NetComponent {

    void inject(ApiCommunication retrofitHolder);
}