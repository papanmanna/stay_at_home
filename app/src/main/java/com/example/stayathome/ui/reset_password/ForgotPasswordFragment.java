package com.example.stayathome.ui.reset_password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.stayathome.R;
import com.example.stayathome.databinding.FragmentForgotPasswordBinding;
import com.example.stayathome.ui.verify_otp.OtpFragment;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding mBinding;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        mBinding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();
            }
        });
        return mBinding.getRoot();
    }

    private void sendOtp() {
        String phone = mBinding.etMobile.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            mBinding.etMobile.setError(getString(R.string.error_mobile_length));
            return;
        }

        OtpFragment otpFragment = new OtpFragment();
        Bundle bundle = new Bundle();

        bundle.putString("mobile", phone);
        bundle.putBoolean("isResetPassword", true);

        otpFragment.setArguments(bundle);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, otpFragment)
                .addToBackStack("otp")
                .commit();

    }


}
