package com.feelinko.feelinko.data.networkUtils;

import android.support.annotation.Nullable;

import com.feelinko.feelinko.data.model.ApiError;

import java.io.IOException;

import retrofit2.Response;

/**
 * This model class is used to provide a simple to use unified process for handling errors encountered while doing retrofit requests.
 * It is created within our custom {@link RxErrorHandlingCallAdapterFactory}.
 *
 * @version 1.0
 */
public class RetrofitException extends RuntimeException {

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * The request URL which produced the error.
     */
    private final String url;
    /**
     * The retrofit response object.
     */
    private final Response response;
    /**
     * The kind error encountered.
     */
    private final Kind kind;
    /**
     * The ApiError object containing the error message
     */
    private final ApiError apiError;

    /**
     * The constructor to create the exception.
     * Note that in the case of Network or Unexpected errors, url and response are null.
     *
     * @param kind      the type of error
     * @param exception the throwable that caused the exception to be created
     * @param error     the ApiError created to transmit the message to the UI
     * @param url       the url of the route where the error occurred
     * @param response  the retrofit response retrieved from the request
     */
    RetrofitException(Kind kind, Throwable exception, ApiError error, @Nullable String url, @Nullable Response response) {
        super(error.getError(), exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.apiError = error;
    }

    /**
     * Get the url which produced the error
     *
     * @return the url String
     */
    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    /**
     * Get the Retrofit Response object containing status code, headers, body, etc.
     *
     * @return the Response object
     */
    @SuppressWarnings("unused")
    public Response getResponse() {
        return response;
    }

    /**
     * Get the {@link Kind} of event which triggered this error.
     *
     * @return the Kind value
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Get the {@link ApiError} object which contains the error message.
     *
     * @return the ApiError object
     */
    @SuppressWarnings("unused")
    public ApiError getApiError() {
        return apiError;
    }
}
