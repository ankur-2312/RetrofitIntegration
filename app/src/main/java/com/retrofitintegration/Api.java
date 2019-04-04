package com.retrofitintegration;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @GET("posts")
    Call<List<example>> getData();

    @POST("posts")
    Call<example>  sendData(@Body example ex);

    @PUT("posts/5")
    @FormUrlEncoded
    Call<example>  updateData(@Field("userId")int id,@Field("title") String title);

    @PATCH("posts/50")
    @FormUrlEncoded
    Call<example>  patchData(@Field("title") String title);



}
