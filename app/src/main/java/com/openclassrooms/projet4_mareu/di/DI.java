package com.openclassrooms.projet4_mareu.di;

import com.openclassrooms.projet4_mareu.service.ApiService;

public class DI {

    private static ApiService service = new ApiService();

    public static ApiService getApiservice(){
        return service;
    }

    public static ApiService getNewInstance(){
        return new ApiService();
    }

}