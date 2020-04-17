package com.example.stayathome.ui.reset_password;

import retrofit2.Response;

public interface ChangePasswordViewModel {

    void setChangePasswordView(Response<ResetPasswordResponse> response);
    void onError(String error);

    void clickOnChangePassword();
}
