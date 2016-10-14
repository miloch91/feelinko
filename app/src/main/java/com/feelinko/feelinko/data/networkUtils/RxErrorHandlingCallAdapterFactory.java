package com.feelinko.feelinko.data.networkUtils;

import com.feelinko.feelinko.data.model.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * class to simplify rx observable errors returned by retrofit
 * This is a wrapper to the existing {@link RxJavaCallAdapterFactory}
 * This will be used when we create the retrofit builder
 *
 * @version 1.0
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    /**
     * the wrapped RxJavaCallAdapterFactory
     */
    private final RxJavaCallAdapterFactory original;

    /**
     * This private constructor will create the wrapped RxJavaCallAdapterFactory object and all its inner functions
     */
    private RxErrorHandlingCallAdapterFactory() {
        original = RxJavaCallAdapterFactory.create();
    }

    /**
     * This static method is called when we construct the retrofit builder.
     * Here we load all the inner functions of the RxJavaCallAdapterFactory.
     * We do not want to reimplement everything, we just want to add our logic for the error mechanism.
     *
     * @return our instance of RxErrorHandlingCallAdapterFactory.
     */
    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    /**
     * When the get method is called by the retrofit builder,
     * we load our CallAdapter which contains the RxErrorHandlingCallAdapterFactory's callAdapter.
     *
     * @return our CallAdapter that contains the RxErrorHandlingCallAdapterFactory's callAdapter
     */
    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    /**
     * RxCallAdapterWrapper class that contains the wrapped RxCallAdaptor created by the wrapped RxErrorHandlingCallAdapterFactory
     */
    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        /**
         * Retrofit object
         */
        @SuppressWarnings("unused")
        private final Retrofit retrofit;
        /**
         * wrapped CallAdapter
         */
        private final CallAdapter<?> wrapped;

        /**
         * The constructor
         *
         * @param retrofit the retrofit object
         * @param wrapped  the wrapped CallAdaptor
         */
        RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        /**
         * Here we use our wrapped CallAdaptor
         *
         * @return the wrapped responseType method result
         */
        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        /**
         * This is the part that interest us.
         * We use the onErrorResumeNext to call our asRetrofit method
         * to change the exception to a Retrofit exception and pass it to the subscribers.
         */
        @SuppressWarnings("unchecked")
        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Func1<Throwable, Observable>() {
                @Override
                public Observable call(Throwable throwable) {
                    return Observable.error(asRetrofitException(throwable));
                }
            });
        }

        /**
         * This method is called when an error occurs.
         * Through the onErrorResumeNext callback defined above we get the error encountered as a throwable.
         * we then convert it to a retrofit exception containing the valid information
         * and then the onErrorResumeNext will pass it on to our subscribers
         *
         * @param throwable the error encountered
         * @return the retrofit exception created from the throwable
         * @see RetrofitException
         */
        private RetrofitException asRetrofitException(Throwable throwable) {

            RetrofitException.Kind kind;
            ApiError apiError;
            String url = null;
            Response response = null;

            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                response = httpException.response();

                kind = RetrofitException.Kind.HTTP;
                apiError = ErrorConverter.parseError(response);
                url = response.raw().request().url().toString();

            } else if (throwable instanceof IOException) /* A network error happened */ {
                kind = RetrofitException.Kind.NETWORK;
                apiError = new ApiError();
                apiError.setNetworkError();

            } else /* We don't know what happened */ {

                kind = RetrofitException.Kind.UNEXPECTED;
                apiError = new ApiError();
                apiError.setUnexpectedError();
            }

            return new RetrofitException(kind, throwable, apiError, url, response);
        }
    }
}
