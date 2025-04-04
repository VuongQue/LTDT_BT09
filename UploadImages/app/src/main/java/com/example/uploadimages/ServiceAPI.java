package com.example.uploadimages;

import android.app.Notification;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceAPI {
    String BASE_URL = "http://app.iotstar.vn:8081/appfoods/";

    @Multipart
    @POST("upload.php")
    Call<List<ImageUpload>> upload(
            @Part(Const.MY_USERNAME) RequestBody username,
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("upload1.php")
    Call<Notification.MessagingStyle.Message> upload1(
            @Part(Const.MY_USERNAME) RequestBody username,
            @Part MultipartBody.Part avatar
    );
}

