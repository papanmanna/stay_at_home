package com.example.stayathome.ui.reset_password;

import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ChangePasswordPresenter {

    private ChangePasswordViewModel mViewModel;

    public void setView(ChangePasswordViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    void changePassword(String phone, String password) {
        ServiceGenerator.getClient().create(ApiInterface.class)
                .resetPassword(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResetPasswordResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResetPasswordResponse> logInResponse) {
                        mViewModel.setChangePasswordView(logInResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
