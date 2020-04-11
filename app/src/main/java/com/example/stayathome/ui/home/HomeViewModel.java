package com.example.stayathome.ui.home;

import com.example.stayathome.models.GetRequestResponse;

import retrofit2.Response;

public interface HomeViewModel {
    void clickOnCreateRequest();
    void setHomeView(Response<GetRequestResponse> response);
    void onError(String error);

}
