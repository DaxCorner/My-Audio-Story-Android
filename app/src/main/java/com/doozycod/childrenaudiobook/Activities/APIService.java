package com.doozycod.childrenaudiobook.Activities;

import com.doozycod.childrenaudiobook.Models.BooksModel_login;
import com.doozycod.childrenaudiobook.Models.Books_model;
import com.doozycod.childrenaudiobook.Models.LibraryModel;
import com.doozycod.childrenaudiobook.Models.Login_model;
import com.doozycod.childrenaudiobook.Models.ResultObject;
import com.doozycod.childrenaudiobook.Models.ShareStoryModel;
import com.doozycod.childrenaudiobook.Models.updateProfileModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                             @Field("mobile_number") String mobile_number,
                             @Field("token") String token,
                             @Field("device_id") String device_id);

    //login
    @POST("User/login.php")
    @FormUrlEncoded
    Call<Login_model> signIn(@Field("username") String username,
                             @Field("password") String password,
                             @Field("token") String token,
                             @Field("device_id") String device_id);

    //Update Profile
    @POST("User/profile_update.php")
    @FormUrlEncoded
    Call<updateProfileModel> updateProfile(@Field("user_id") String user_id,
                                           @Field("first_name") String first_name,
                                           @Field("last_name") String last_name,
                                           @Field("email") String email,
                                           @Field("mobile_number") String mobile_number);


    //Change password
    @POST("User/change_password.php")
    @FormUrlEncoded
    Call<ResultObject> changePassword(@Field("user_id") String user_id,
                                      @Field("old_password") String old_password,
                                      @Field("new_password") String new_password);

    //logout
    @GET("User/logout.php")
    Call<ResultObject> logout(@Query("user_id") String user_id,
                              @Query("existing_token") String existing_token);


    //books
    @GET("Book/books.php")
    Call<BooksModel_login> getAllBooks_login(@Query("user_id") String username);

    //books
    @POST("Library/delete_library.php")
    @FormUrlEncoded
    Call<ResultObject> delete_LibraryBook(@Field("user_id") String user_id,
                                          @Field("library_id") String library_id);

    //books
    @POST("Book/paid_book.php")
    @FormUrlEncoded
    Call<ResultObject> PaidBooks(@Field("user_id") String user_id,
                                 @Field("book_id") String book_id);

    //getLibrary
    @GET("Library/get_all_library.php")
    Call<LibraryModel> getLibrary(@Query("user_id") String username);


    //books
    @GET("Book/books_new.php")
    Call<Books_model> getAllBooks();


    @Multipart
    @POST("Library/share.php")
    Call<ShareStoryModel> shareLibraryToUser(@Part("user_id") RequestBody user_id,
                                             @Part("name") RequestBody name,
                                             @Part("book_id") RequestBody book_id,
                                             @Part("phone_number") RequestBody phone_number,
                                             @Part("email") RequestBody email,
                                             @Part MultipartBody.Part audio_message,
                                             @Part MultipartBody.Part audio_story);

    @Multipart
    @POST("Library/add-library.php")
    Call<ResultObject> uploadAudioToServer(@Part("user_id") RequestBody user_id,
                                           @Part("book_id") RequestBody book_id,
                                           @Part("name") RequestBody name,
                                           @Part MultipartBody.Part audio_message,
                                           @Part MultipartBody.Part audio_story);

}