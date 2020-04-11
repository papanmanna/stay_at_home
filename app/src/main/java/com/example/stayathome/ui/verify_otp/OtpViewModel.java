package com.example.stayathome.ui.verify_otp;

import com.example.stayathome.models.RegistrationResponse;

import retrofit2.Response;

public interface OtpViewModel {
    void clickOnVerify();
    void clickOnResend();
    void setRegistrationView(Response<RegistrationResponse> response);

    void onError(String error);

}
