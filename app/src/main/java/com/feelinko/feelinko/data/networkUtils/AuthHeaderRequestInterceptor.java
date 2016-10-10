package com.feelinko.feelinko.data.networkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class corresponds to the authentication header we will add during the creation of the retrofit object.
 * It is Injected using Dagger 2 and has a singleton scope. This means that to set the token in the header's of our requests
 * we just need to get the singleton instance add the token and regenerate the Retrofit object.
 *
 * @see com.feelinko.feelinko.data.ApiCommunication#updateToken(String)
 * @version 1.0
 */
public class AuthHeaderRequestInterceptor implements Interceptor {
    /**
     * The current user's token given by the server
     */
    private String mToken;

    /**
     * This method set's the token
     * @param token the new token value
     */
    public void setToken(String token) {
        this.mToken = token;
    }

    /**
     * This method defines the code for the interceptor that is run when the {@link com.feelinko.feelinko.dagger2.module.NetModule}
     * provides the OkHttpClient to the retrofit instance being provided to the ApiComponent
     * @see com.feelinko.feelinko.dagger2.module.NetModule#provideOkHttpClient(AuthHeaderRequestInterceptor)
     * @see Interceptor
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();

        if (mToken != null) {
            requestBuilder.header("x-access-token", mToken);
        }

        requestBuilder.method(original.method(), original.body());
        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}