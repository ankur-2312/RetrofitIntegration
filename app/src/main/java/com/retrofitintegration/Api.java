package com.retrofitintegration;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";
    String BASE_URL_1 = "https://webkapil.000webhostapp.com/uploads/";

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

    @Multipart
    @POST("upload_files.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}


