package com.feelinko.feelinko.data.retrofitInterfaces;

import com.feelinko.feelinko.data.model.Auth;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * This interface will enable us to create the retrofit rest adaptor to authenticate the user.
 *
 * @version 1.0
 */
public interface AuthApi {
    /**
     * The method to authenticate the user using his facebook account.
     * We will send the user's facebook access token and we will receive the user's server token and refresh token.
     *
     * @param req the request object containing the facebook access token
     * @return an rx Observable with the response object containing the response from the server
     * @see Auth.Request
     * @see Auth.Response
     */
    @POST("facebook_login")
    Observable<Auth.Response> login(@Body Auth.Request req);
}