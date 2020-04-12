package com.example.stayathome.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stayathome.R;
import com.example.stayathome.home.MainActivity;
import com.example.stayathome.models.MeResponse;
import com.example.stayathome.network.ApiInterface;
import com.example.stayathome.network.ServiceGenerator;
import com.example.stayathome.ui.login.LoginFragment;
import com.example.stayathome.utils.UserManager;
import com.example.stayathome.utils.ViewDialog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private ViewDialog mViewDialog;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDialog = new ViewDialog(requireActivity());
        callMe();
    }

    private void callMe() {
        mViewDialog.showDialog();
        ServiceGenerator.getClient().create(ApiInterface.class)
                .me(UserManager.getInstance().getBearerAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MeResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MeResponse> meResponseResponse) {
                        mViewDialog.hideDialog();
                        if (meResponseResponse.isSuccessful() && meResponseResponse.body() != null && meResponseResponse.body().isStatus()) {
                            MeResponse.Result user = meResponseResponse.body().getResult();
                            UserManager.getInstance().updateUserData(user.getFirstName(), user.getLastName(), user.getAadhaarId(), user.getPinCode(), user.getStation(), user.getEmail(), user.getPhone());
                            Intent intentMain = new Intent(requireActivity(), MainActivity.class);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intentMain);
                            requireActivity().finish();
                        } else {
                            UserManager.getInstance().logOutUser();
                            requireFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, new LoginFragment())
                                    .commit();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewDialog.hideDialog();
                        UserManager.getInstance().logOutUser();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        requireFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new LoginFragment())
                                .commit();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
