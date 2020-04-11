package com.example.stayathome.ui.login;

import com.example.stayathome.models.LogInResponse;

public interface LoginViewModel {

    void setLoginView(LogInResponse logInResponse);
    void onError(String error);

    void clickOnLogIn();
    void clickOnSignUp();
}
