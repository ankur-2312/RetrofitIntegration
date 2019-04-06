package com.retrofitintegration;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiInstance {

    private static ApiInstance instance;
    private Api api;
    private Api api1;

    private ApiInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL_1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api1 = retrofit1.create(Api.class);

    }


    static ApiInstance getInstance() {
        if (instance == null) {
            instance = new ApiInstance();
        }
        return instance;

    }

    Api getApiInstance() {
        return api;
    }

    Api getApiInstance1() {
        return api1;
    }
}
