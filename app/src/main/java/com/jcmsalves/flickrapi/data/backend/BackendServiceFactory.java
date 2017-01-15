package com.jcmsalves.flickrapi.data.backend;

import com.google.gson.GsonBuilder;
import com.jcmsalves.flickrapi.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by joaoalves on 14/01/2017.
 */

public class BackendServiceFactory {

    private final static String BASE_URL = "https://api.flickr.com/services/";

    public static BackendService makeBackendService() {
        OkHttpClient client = makeOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JsonCleanerConverter.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(BackendService.class);
    }

    public static OkHttpClient makeOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestQueriesInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                .build();
        return client;
    }
}
