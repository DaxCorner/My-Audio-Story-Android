package com.doozycod.childrenaudiobook.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultObject {
    @SerializedName("user_id")
    @Expose
    private String success;
    public ResultObject(String success) {
        this.success = success;
    }
    public String getSuccess() {
        return success;
    }
}