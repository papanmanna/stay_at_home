package com.example.stayathome.ui.registration;

import com.example.stayathome.models.RegistrationResponse;

public interface RegisterViewModel {
    void setRegistrationView(RegistrationResponse response);

    void onError(String error);

    void clickOnSignUp();

    void clickOnLogIn();
}
