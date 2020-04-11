package com.example.stayathome.ui.login;

import com.example.stayathome.models.LogInResponse;
import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginPresenter {
    private LoginViewModel mViewModel;

    public void setView(LoginViewModel loginViewModel) {
        this.mViewModel = loginViewModel;
    }


    void logIn(String phone, String password) {
        ServiceGenerator.getClient().create(ApiInterface.class)
                .login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<LogInResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<LogInResponse> logInResponse) {
                        mViewModel.setLoginView(logInResponse.body());
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
