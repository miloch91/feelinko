package com.feelinko.feelinko.data.model;

/**
 * This model class is used to authenticate a user.
 * It contains two nested classes.
 * One responsible for the request to authenticate the user
 * and one responsible for the response from the server
 * @version 1.0
 */
public class Auth {

    /**
     * Request object with the facebook access token to enable the server to get the user's friends
     */
    public static class Request {
        /**
         * The user's facebook access token
         */
        @SuppressWarnings("unused")
        private String facebookToken;

        /**
         * The constructor to create the request object
         * @param facebookToken the user's facebook access token
         */
        public Request(String facebookToken) {
            this.facebookToken = facebookToken;
        }
    }

    /**
     * Response class to store the token and refresh token
     * This object will be constructed by retrofit using reflexion
     * so we do not need to specify any setters for the fields
     */
    public static class Response {
        /**
         * The token to store
         */
        @SuppressWarnings("unused")
        private String token;
        /**
         * The refresh token to store
         */
        @SuppressWarnings("unused")
        private String refreshToken;

        /**
         * Get the Token
         * @return the previously saved token.
         */
        public String getToken() {
            return token;
        }

        /**
         * Get the Refresh Token
         * @return the previously saved refresh token.
         */
        public String getRefreshToken() {
            return refreshToken;
        }
    }
}