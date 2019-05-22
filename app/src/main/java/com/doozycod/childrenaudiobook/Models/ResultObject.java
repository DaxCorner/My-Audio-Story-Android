package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultObject {


    @SerializedName("status")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public ResultObject(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }
}