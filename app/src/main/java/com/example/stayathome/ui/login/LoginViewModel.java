package com.example.stayathome.ui.login;

import com.example.stayathome.models.LogInResponse;

import retrofit2.Response;

public interface LoginViewModel {

    void setLoginView(Response<LogInResponse> logInResponse);
    void onError(String error);

    void clickOnLogIn();
    void clickOnSignUp();
}
