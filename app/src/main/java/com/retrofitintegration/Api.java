package com.retrofitintegration;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @GET("posts")
    Call<List<PoJo>> getData();

    @POST("posts")
    Call<PoJo>  postData(@Body PoJo ex);

    @PUT("posts/34")
    @FormUrlEncoded
    Call<PoJo>  putData(@Field("userId")int userId, @Field("title") String title);

    @PATCH("posts/5")
    @FormUrlEncoded
    Call<PoJo>  patchData(@Field("title") String title);

    @DELETE("posts/{id}")
    Call<ResponseBody> deleteData(@Path("id")int id);

}
