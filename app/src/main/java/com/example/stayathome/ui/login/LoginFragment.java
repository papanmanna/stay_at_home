package com.example.stayathome.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stayathome.R;
import com.example.stayathome.auth.MeFragment;
import com.example.stayathome.databinding.FragmentLoginBinding;
import com.example.stayathome.models.LogInResponse;
import com.example.stayathome.ui.registration.RegisterFragment;
import com.example.stayathome.utils.UserManager;
import com.example.stayathome.utils.ViewDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements LoginViewModel {

    private FragmentLoginBinding mBinding;
    private LoginPresenter mPresenter;
    private ViewDialog viewDialog;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        mBinding.setClickHandler(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new LoginPresenter();
        mPresenter.setView(this);
        viewDialog = new ViewDialog(requireActivity());

    }

    @Override
    public void setLoginView(LogInResponse logInResponse) {
        viewDialog.hideDialog();
        if (logInResponse.isStatus()) {
            UserManager.getInstance().loginUser(logInResponse.getToken(), true);
            requireFragmentManager()
                    .beginTransaction()
                    .add(new MeFragment(), "userMe")
                    .commit();
        } else {
            Toast.makeText(requireContext(), logInResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
        viewDialog.hideDialog();
    }

    @Override
    public void clickOnLogIn() {
        String phone = mBinding.etMobile.getText().toString();
        String password = mBinding.etPassword.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            mBinding.etMobile.setError(getString(R.string.error_mobile_length));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mBinding.etPassword.setError(getString(R.string.error_password));
            return;
        }
        viewDialog.showDialog();
        mPresenter.logIn(phone, password);
    }

    @Override
    public void clickOnSignUp() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new RegisterFragment())
                .addToBackStack("registration")
                .commit();
    }
}
