package com.doozycod.childrenaudiobook.Activities;

import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.Models.Signup_model;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {


    //signup
    @POST("User/signup_post.php")
    @FormUrlEncoded
    Call<Login_model> signUp(@Field("first_name") String first_name,
                              @Field("last_name") String last_name,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("mobile_number") String mobile_number);


    //login
    @POST("User/login.php")
    @FormUrlEncoded
    Call<Login_model> signIn(@Field("username") String username,
                             @Field("password") String password);


    //books
    @GET("Book/books.php")
    Call<Books_model> getAllBooks(@Query("id") String username);


//     Audio File
//    @Multipart
//    @POST("Library/add-library.php")
//    Call<ResultObject> uploadAudioToServer(@Part MultipartBody.Part audio);
    @Multipart
    @POST("Library/add-library.php")
    Call<ResultObject> uploadAudioToServer(@Field("user_id") String user_id,
                                           @Field("name") String name,
                                           @Field("book_id") String book_id,
                                           @Part("audio_message") MultipartBody.Part audio_message,
                                           @Part("audio_story") MultipartBody.Part audio_story);


}