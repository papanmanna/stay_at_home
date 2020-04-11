package com.example.stayathome.ui.registration;

public class RegisterPresenter {
    private RegisterViewModel mViewModel;

    public void setView(RegisterViewModel view) {
        this.mViewModel = view;
    }

//    void signUp(String name, String email, String mobile, String aadhaar, String pinCode, String password) {
//        ApiInterface apiService =
//                ServiceGenerator.getClient().create(ApiInterface.class);
//
//        apiService.signUp(name, email, password, mobile, aadhaar, pinCode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<RegistrationResponse>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<RegistrationResponse> registrationResponse) {
//                        mViewModel.setRegistrationView(registrationResponse.body());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mViewModel.onError(e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

}
