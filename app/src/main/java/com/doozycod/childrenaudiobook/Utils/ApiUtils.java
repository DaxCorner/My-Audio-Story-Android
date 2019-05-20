package com.doozycod.childrenaudiobook.Utils;

import com.doozycod.childrenaudiobook.Activities.APIService;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://www.doozycod.in/books-manager/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}