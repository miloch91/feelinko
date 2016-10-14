package com.feelinko.feelinko.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.feelinko.feelinko.FeelinkoApplication;
import com.feelinko.feelinko.R;

import com.feelinko.feelinko.dagger2.module.SharedPreferencesModule;
import com.feelinko.feelinko.home.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.feelinko.feelinko.dagger2.module.SharedPreferencesModule.REFRESH_TOKEN_SHARED_PREFERENCES_TAG;
import static com.feelinko.feelinko.dagger2.module.SharedPreferencesModule.TOKEN_SHARED_PREFERENCES_TAG;

/**
 * This is the LoginActivity class. It is the view responsible for the display of the user's login.
 * For now the user logs in with facebook.
 * This view is bound to the LoginPresenter and together they provide the logic for login process.
 *
 * @version 1.0
 * @see LoginContract.View
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    /**
     * This is the LoginButton present on the screen. It is bound to the xlm using butterknife
     */
    @BindView(R.id.login_button)
    LoginButton mLoginButton;
    /**
     * This is the root view present on the screen. It is bound to the xlm using butterknife.
     * We want a reference to this object to display the Snackbar
     *
     * @see LoginActivity#showError(String)
     */
    @BindView(R.id.activity_login)
    View mRootView;
    /**
     * This is the facebook CallManager.
     * we use it to retrieve the response from the facebook activity launched to get the facebook access token
     */
    private CallbackManager mCallbackManager;
    /**
     * This is the presenter bound to this view.
     * It contains all the logic decoupled from the android framework to enable easy unit tests.
     *
     * @see LoginContract.Presenter
     */
    @Inject
    LoginContract.Presenter mPresenter;
    /**
     * This is the apps shared preferences. We inject it with dagger. This guaranties us that it is constructed
     * in the right way throughout the app
     */
    @Inject
    public SharedPreferences mSharedPreferences;

    /**
     * This method is called when the activity is created. It is here we initialise everything.
     *
     * @param savedInstanceState the previously saved bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // here we bind the login presenter and through the dependency graph also bind the shared preferences
        ((FeelinkoApplication) getApplication()).getPresenterComponent()
                .injectLoginPresenter(this);
        // if there already is a token in the shared preferences we move directly to the home activity
        // because the dagger modules already have configured the retrofit dependencies with the token
        // extracted from the shared preferences. Note that if the facebook token is not valid anymore,
        // the server will notify us with an error code and we will erase all the tokens from the shared preferences
        // and redirect the user to this view.
        if (mSharedPreferences.contains(SharedPreferencesModule.TOKEN_SHARED_PREFERENCES_TAG)) {
            startHomeActivity();
            return;
        }

        // here we use butterknife to bind the view components defined in the xml
        // to the variables in this class with a BindView annotation
        ButterKnife.bind(this);

        // this is the code to get back the access token from facebook
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mPresenter.onFacebookTokenUpdate(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // here user canceled connection attempt
                showError(getString(R.string.facebook_login_canceled_error));
            }

            @Override
            public void onError(FacebookException e) {
                // here connection failed
                showError(getString(R.string.unexpected_error));
            }
        });
    }

    /**
     * This method is called when the facebook activity returns with the information on the user's
     * connection attempt.
     * It also contains the user's login information if the facebook login process was successful.
     * In this method, we transmit the results to the CallbackManager to get the login results
     * inside our callback defined in the {@link LoginActivity#onCreate(Bundle)}
     *
     * @param requestCode the facebook activity request code
     * @param resultCode  the code specifying if the process was successful
     * @param data        the data containing the user's login information if process successful
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This method is called just before the activity is running.
     * Here we tell to the presenter that elements are ready to be displayed.
     * In other words, the view is ready.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onViewAttached(this);
    }

    /**
     * When this method is called, the view is not available anymore.
     * This for instance happens when another activity comes into the foreground.
     * Here we tell the presenter to refrain from doing view actions.
     */
    @Override
    protected void onPause() {
        mPresenter.onViewDetached();
        super.onPause();
    }

    /**
     * {@inheritDoc}
     *
     * @param serverToken        the current user's access token to the server
     * @param serverRefreshToken the current user's refresh token to regenerate the token when it expires
     */
    @Override
    public void saveTokens(String serverToken, String serverRefreshToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(TOKEN_SHARED_PREFERENCES_TAG, serverToken);
        editor.putString(REFRESH_TOKEN_SHARED_PREFERENCES_TAG, serverRefreshToken);

        editor.apply();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    /**
     * {@inheritDoc}
     *
     * @param error the error the server encountered
     */
    @Override
    public void showError(String error) {

        Snackbar bar = Snackbar.make(mRootView, error, Snackbar.LENGTH_LONG);
        bar.setAction(getString(R.string.snackBar_dismiss_info), (View view) -> bar.dismiss());
        bar.show();
    }
}
