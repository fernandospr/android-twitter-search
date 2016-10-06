package com.fernandospr.twittersearch.repository;

import com.fernandospr.twittersearch.network.DateDeserializer;
import com.fernandospr.twittersearch.network.TwitterApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwitterRepository {

    private static final String BASE_URL = "https://api.twitter.com/";
    private final TwitterApi mTwitterApi;

    public TwitterRepository() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mTwitterApi = retrofit.create(TwitterApi.class);
    }
    
}
