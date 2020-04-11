package com.example.stayathome.ui.request;

import com.example.stayathome.models.CreateRequestResponse;

import retrofit2.Response;

public interface RequestViewModel {
    void clickOnDate();

    void clickOnAttach();

    void clickOnClose();

    void clickOnSubmit();

    void setHomeView(Response<CreateRequestResponse> response);

    void onError(String message);
}
