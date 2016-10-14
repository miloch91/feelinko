package com.feelinko.feelinko.data.networkUtils;

import com.feelinko.feelinko.data.ApiCommunication;
import com.feelinko.feelinko.data.model.ApiError;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class will enable us to convert the retrofit response in an ApiError object
 *
 * @version 1.0
 */
public class ErrorConverter {

    /**
     * This static field is used to convert the response error body in an {@link ApiError} object
     */
    private static Converter<ResponseBody, ApiError> sConverter = new Retrofit.Builder()
            .baseUrl(ApiCommunication.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().responseBodyConverter(ApiError.class, new Annotation[0]);

    /**
     * This static method converts the retrofit response in an ApiError object using the retrofit object
     *
     * @param response the response obtained by the server
     * @return the ApiError converted from the response object
     */
    public static ApiError parseError(Response<?> response) {

        ApiError error;

        try {
            error = sConverter.convert(response.errorBody());
        } catch (Exception e) {
            error = new ApiError();
            error.setUnexpectedError();
        }

        if (error.getError() == null) {
            error = new ApiError();
            error.setUnexpectedError();
        }

        return error;
    }
}