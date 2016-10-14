package com.feelinko.feelinko.login;

import com.feelinko.feelinko.data.ApiCommunication;
import com.feelinko.feelinko.data.model.Auth;
import com.feelinko.feelinko.data.networkUtils.RetrofitException;

import javax.inject.Inject;

import rx.Observer;

/**
 * This is the login presenter. This class contains all the logic for the login feature decoupled from the android framework.
 * This makes it easier to unit test the logic layer of the application.
 * This object is injected to the view as a singleton so it will persist throughout the lifecycle of the view bound to it.
 *
 * @version 1.0
 */
public class LoginPresenter implements LoginContract.Presenter {

    /**
     * This is the object to communicate with the api.
     * This object is used to do requests to the server.
     * It is injected through the constructor with dagger.
     *
     * @see com.feelinko.feelinko.dagger2.component.RetrofitComponent
     * @see ApiCommunication
     */
    private ApiCommunication mApi;
    /**
     * This is the bound login view. Through this view the UI is displayed.
     * When the activity bound to this presenter isn't available, this field will be set to null.
     */
    private LoginContract.View mLoginView;
    /**
     * This lambda is responsible for holding the logic to be executed on the view when it is available.
     * This is because the view isn't always available. When there is an orientation change for instance,
     * the activity and thereby the view is completely destroyed. This means that we need to wait for the
     * activity to be ready again to start doing actions on the view.
     */
    private Runnable mPendingViewMethods;

    /**
     * This is the constructor. It is created with the dagger module {@link com.feelinko.feelinko.dagger2.module.PresenterModule}.
     * Through the dagger components we will inject a singleton instance of the apiCommunication class to request the server.
     *
     * @param api the apiCommunication object to request the server.
     * @see com.feelinko.feelinko.dagger2.component.PresenterComponent
     */
    @Inject
    public LoginPresenter(ApiCommunication api) {
        this.mApi = api;

        mLoginView = null;
        mPendingViewMethods = null;
    }

    /**
     * {@inheritDoc}
     *
     * @param facebookToken the new facebook access token
     */
    @Override
    public void onFacebookTokenUpdate(String facebookToken) {

        // As you can see the weather the request is successful or an error occurred,
        // we save the code that should be executed on the view inside a lambda.
        // We then call callPendingViewMethods() to verify that the view is ready to be stimulated.
        // If the view is not available then the view actions will be executed
        // on the next binding of the view to this presenter.
        mApi.login(new Auth.Request(facebookToken)).subscribe(new Observer<Auth.Response>() {
            @Override
            public void onCompleted() {
                callPendingViewMethods();
            }

            @Override
            public void onError(Throwable e) {
                RetrofitException error = (RetrofitException) e;
                if (error.getKind() == RetrofitException.Kind.HTTP
                        || error.getKind() == RetrofitException.Kind.NETWORK) {
                    mPendingViewMethods = () -> mLoginView.showError(error.getMessage());
                    callPendingViewMethods();
                } else /*RetrofitException.Kind.UNEXPECTED error*/ {
                    throw error;
                }
            }

            @Override
            public void onNext(Auth.Response response) {
                mPendingViewMethods = () -> {
                    mLoginView.saveTokens(response.getToken(), response.getRefreshToken());
                    mLoginView.startHomeActivity();
                };
            }
        });
    }

    /**
     * This is the method called to execute the code to be called upon the view if it is available.
     */
    private void callPendingViewMethods() {
        if (mLoginView != null && mPendingViewMethods != null) {
            mPendingViewMethods.run();
            mPendingViewMethods = null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param view the view to be attached to the presenter
     */
    @Override
    public void onViewAttached(LoginContract.View view) {
        mLoginView = view;
        // Here we call the method to display view actions if they are pending.
        // This happens for instance if the presenter gets the response from the server
        // when the view is going through an orientation change
        callPendingViewMethods();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewDetached() {
        mLoginView = null;
    }
}
