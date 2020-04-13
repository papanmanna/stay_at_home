package com.example.stayathome.ui.registration;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stayathome.GPSManagerHelper;
import com.example.stayathome.R;
import com.example.stayathome.databinding.FragmentRegisterBinding;
import com.example.stayathome.models.RegistrationResponse;
import com.example.stayathome.ui.verify_otp.OtpFragment;
import com.example.stayathome.utils.ViewDialog;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements RegisterViewModel, GPSManagerHelper.LocationListener {

    private FragmentRegisterBinding mBinding;
    private RegisterPresenter mPresenter;
    private ViewDialog viewDialog;
    GPSManagerHelper gpsManagerHelper;
    private int PERMISSION_ID = 44;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        mBinding.setClickHandler(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new RegisterPresenter();
        mPresenter.setView(this);
        viewDialog = new ViewDialog(requireActivity());
        gpsManagerHelper = new GPSManagerHelper(requireActivity());
        gpsManagerHelper.setLocationListener(this);
        gpsManagerHelper.getLastLocation();
    }

    @Override
    public void clickOnSignUp() {
        String name = mBinding.nameEt.getText().toString();
        String firstName = "", lastName = "";
        String[] splitStr = name.split("\\s+");
        String phone = mBinding.mobileEt.getText().toString();
        String email = mBinding.emailEt.getText().toString();
        String adharId = mBinding.adharEt.getText().toString();
        String pinCode = mBinding.pinEt.getText().toString();
        String password = mBinding.passEt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mBinding.nameEt.setError(getString(R.string.error_name));
            return;
        } else if (splitStr.length > 1) {
            firstName = splitStr[0];
            lastName = splitStr[1];
        } else {
            firstName = splitStr[0];
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            mBinding.mobileEt.setError(getString(R.string.error_mobile_length));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mBinding.emailEt.setError(getString(R.string.error_email));
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.emailEt.setError(getString(R.string.error_email_pattern));
            return;
        }

        if (TextUtils.isEmpty(adharId) || !Pattern.compile("^[0-9]{12}").matcher(adharId).matches()) {
            mBinding.adharEt.setError(getString(R.string.error_adhar));
            return;
        }

        if (TextUtils.isEmpty(pinCode)) {
            mBinding.pinEt.setError(getString(R.string.error_pin));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mBinding.passEt.setError(getString(R.string.error_password));
            return;
        } else if (password.length() < 6) {
            mBinding.passEt.setError(getString(R.string.error_password_length));
            return;
        }

        OtpFragment otpFragment = new OtpFragment();
        Bundle bundle = new Bundle();
        bundle.putString("firstName", firstName);
        bundle.putString("lastName", lastName);
        bundle.putString("email", email);
        bundle.putString("mobile", phone);
        bundle.putString("aadhaar", adharId);
        bundle.putString("pin", pinCode);
        bundle.putString("password", password);
        otpFragment.setArguments(bundle);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, otpFragment)
                .addToBackStack("otp")
                .commit();

//        viewDialog.showDialog();
//        mPresenter.signUp(name,email,phone,adharId,pinCode,password);
    }


    @Override
    public void clickOnLogIn() {
        requireActivity()
                .getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                gpsManagerHelper.getLastLocation();
            }
        }
    }

    @Override
    public void onTakeLocation(double latitude, double longitude) {
        try {
            String zipcode = gpsManagerHelper.getZipCode(latitude, longitude);
            mBinding.pinEt.setText(zipcode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
