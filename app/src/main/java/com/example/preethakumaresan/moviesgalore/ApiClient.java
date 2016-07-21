package com.example.preethakumaresan.moviesgalore;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class ApiClient {


    public static Retrofit getClient() {

           Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}