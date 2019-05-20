package com.doozycod.childrenaudiobook.Activities;

import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.Signup_model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {


    //signup
    @POST("User/signup_post.php")
    @FormUrlEncoded
    Call<Signup_model> signUp(@Field("first_name") String first_name,
                              @Field("last_name") String last_name,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("mobile_number") String mobile_number);


    //login
    @POST("User/login.php")
    @FormUrlEncoded
    Call<Login_model> signIn(@Field("username") String username,
                             @Field("password") String password);


    //login
    @GET("Book/books.php")
    Call<Books_model> getAllBooks();



}