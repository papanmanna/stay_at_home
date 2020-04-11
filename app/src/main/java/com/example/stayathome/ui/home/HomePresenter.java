package com.example.stayathome.ui.home;

import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;
import com.example.stayathome.utils.UserManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomePresenter {

    private HomeViewModel mViewModel;

    public void setView(HomeViewModel loginViewModel) {
        this.mViewModel = loginViewModel;
    }

    void getAllRequest() {
        ServiceGenerator.getClient().create(ApiInterface.class)
                .getAllRequest(UserManager.getInstance().getBearerAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetRequestResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<GetRequestResponse> response) {
                        mViewModel.setHomeView(response);
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
