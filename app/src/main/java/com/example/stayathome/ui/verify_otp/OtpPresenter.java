package com.example.stayathome.ui.verify_otp;

import com.example.stayathome.models.RegistrationResponse;
import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class OtpPresenter {

    private OtpViewModel mViewModel;

    public void setView(OtpViewModel view) {
        this.mViewModel = view;
    }

    void signUp(String userName, String email, String mobile, String aadhaar, String pinCode, String password) {
        ServiceGenerator.getClient().create(ApiInterface.class)
                .signUp(userName, email, password, mobile, aadhaar, pinCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<RegistrationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<RegistrationResponse> registrationResponse) {

                        mViewModel.setRegistrationView(registrationResponse);


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
