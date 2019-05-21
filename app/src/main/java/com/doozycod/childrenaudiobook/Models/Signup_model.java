package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Signup_model {

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("result")
    @Expose
    private List<result_objects> result=null;

    public List<result_objects> getResult() {
        return result;
    }

    public void setResult(List<result_objects> result) {
        this.result = result;
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

    public class result_objects{
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("first_name")
        @Expose
        private String first_name;

        @SerializedName("last_name")

        @Expose
        private String last_name;
        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("mobile_number")
        @Expose
        private String mobile_number;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }


    }




}