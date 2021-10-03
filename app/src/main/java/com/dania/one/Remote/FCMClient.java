package com.dania.one.Remote;

import com.dania.one.Common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMClient {

    private static Retrofit instance;

    public static Retrofit getInstance() {

        if (instance == null)
            instance = new Retrofit.Builder()
                    .baseUrl(Common.fcmURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return instance;


    }
}