package com.feelinko.feelinko.login;

/**
 * This interface specifies the contract between the view and the presenter for the login feature
 */
public interface LoginContract {
    interface View {
        /**
         * This method is called when the facebook login operation is done correctly.
         * Since the presenter cannot have any dependencies on the android framework to do quick unit tests,
         * we have to use the view to save the tokens to the shared preferences
         *
         * @param serverToken        the current user's access token to the server
         * @param serverRefreshToken the current user's refresh token to regenerate the token when it expires
         */
        void saveTokens(String serverToken, String serverRefreshToken);

        /**
         * This method is called when the facebook login process has executed properly
         */
        void startHomeActivity();

        /**
         * This method is called when an error happened on the server side and the tokens could not be generated
         *
         * @param error the error the server encountered
         */
        void showError(String error);
    }

    interface Presenter {
        /**
         * This method is used to send the new facebook token to the server and receive the server's token and refresh token
         *
         * @param facebookToken the new facebook access token
         */
        void onFacebookTokenUpdate(String facebookToken);

        /**
         * This method is called when the view is created and ready to display elements to the screen
         *
         * @param view the view to be attached to the presenter
         */
        void onViewAttached(View view);

        /**
         * This method is called when the view is not available for the presenter.
         * This happens for example during an orientation change
         */
        void onViewDetached();
    }
}
