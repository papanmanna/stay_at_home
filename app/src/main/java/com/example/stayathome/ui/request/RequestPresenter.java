package com.example.stayathome.ui.request;

import com.example.stayathome.models.CreateRequestResponse;
import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;
import com.example.stayathome.utils.UserManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RequestPresenter {

    RequestViewModel viewModel;

    void setViewModel(RequestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void createRequest(String stationId, String reason, long date, int startHour, int endHour, int personCount) {
        ServiceGenerator.getClient().create(ApiInterface.class)
                .create(UserManager.getInstance().getBearerAccessToken(),stationId,reason,date,startHour,endHour,personCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CreateRequestResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CreateRequestResponse> response) {
                        viewModel.setHomeView(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewModel.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
