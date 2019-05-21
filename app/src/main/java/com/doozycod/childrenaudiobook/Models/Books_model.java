package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Books_model {
    @SerializedName("user_id")
    @Expose
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("book_list_data")
    @Expose
    private List<book_detail> book_list_data=null;

    public List<book_detail> getBook_list_data() {
        return book_list_data;
    }

    public void setBook_list_data(List<book_detail> book_list_data) {
        this.book_list_data = book_list_data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class book_detail{
        @SerializedName("book_id")
        @Expose
        private String book_id;
        @SerializedName("book_name")
        @Expose
        private String book_name;
        @SerializedName("book_image")
        @Expose
        private String book_image;
        @SerializedName("book_audio_file")
        @Expose
        private String book_audio_file;
        @SerializedName("book_content_file")
        @Expose
        private String book_content_file;
        @SerializedName("is_paid")
        @Expose
        private String is_paid;

        public String getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(String is_paid) {
            this.is_paid = is_paid;
        }

        public String getBook_id() {
            return book_id;
        }

        public void setBook_id(String book_id) {
            this.book_id = book_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getBook_image() {
            return book_image;
        }

        public void setBook_image(String book_image) {
            this.book_image = book_image;
        }

        public String getBook_audio_file() {
            return book_audio_file;
        }

        public void setBook_audio_file(String book_audio_file) {
            this.book_audio_file = book_audio_file;
        }

        public String getBook_content_file() {
            return book_content_file;
        }

        public void setBook_content_file(String book_content_file) {
            this.book_content_file = book_content_file;
        }


    }










}
