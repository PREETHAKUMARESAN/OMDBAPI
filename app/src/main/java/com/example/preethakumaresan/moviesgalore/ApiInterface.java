package com.example.preethakumaresan.moviesgalore;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public interface ApiInterface {

    @GET("/")
    Call<Movie> getMovieTitle(@QueryMap Map<String, String> options);
}