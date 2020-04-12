package com.example.stayathome.ui.verify_otp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stayathome.R;
import com.example.stayathome.databinding.FragmentOtpBinding;
import com.example.stayathome.models.RegistrationResponse;
import com.example.stayathome.utils.ViewDialog;
import com.msg91.sendotpandroid.library.internal.SendOTP;
import com.msg91.sendotpandroid.library.listners.VerificationListener;
import com.msg91.sendotpandroid.library.roots.RetryType;
import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder;
import com.msg91.sendotpandroid.library.roots.SendOTPResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment implements VerificationListener, OtpViewModel {

    private FragmentOtpBinding mBinding;
    private String firstName, lastName, mobile, email, aadhaarId, password, pinCode;
    private OtpPresenter mPresenter;
    private ViewDialog viewDialog;

    public OtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOtpBinding.inflate(inflater, container, false);
        mBinding.setClickHandler(this);
        if (getArguments() != null) {
            firstName = getArguments().getString("firstName");
            lastName = getArguments().getString("lastName");
            email = getArguments().getString("email");
            mobile = getArguments().getString("mobile");
            aadhaarId = getArguments().getString("aadhaar");
            pinCode = getArguments().getString("pin");
            password = getArguments().getString("password");
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new OtpPresenter();
        mPresenter.setView(this);
        viewDialog = new ViewDialog(requireActivity());
        init();
    }


    private void init() {
        viewDialog.showDialog();
        new SendOTPConfigBuilder()
                .setCountryCode(91)
                .setMobileNumber(mobile)
                .setVerifyWithoutOtp(true)//direct verification while connect with mobile network
                .setAutoVerification(requireActivity())//Auto read otp from Sms And Verify
                .setSenderId("ABCDEF")
                .setMessage("##OTP## is Your verification digits.")
                .setOtpLength(4)
                .setVerificationCallBack(this).build();
        SendOTP.getInstance().getTrigger().initiate();
    }

    @Override
    public void onSendOtpResponse(final SendOTPResponseCode responseCode, final String s) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewDialog.hideDialog();
                if (responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode == SendOTPResponseCode.OTP_VERIFIED) {
                    //otp verified OR direct verified by send otp 2.O
                    Toast.makeText(requireContext(), getString(R.string.successfull_verify_otp), Toast.LENGTH_SHORT).show();
                    signUp();
                } else if (responseCode == SendOTPResponseCode.READ_OTP_SUCCESS) {
                    //Auto read otp from sms successfully
                    // you can get otp form message filled
                    if (s != null) {
                        mBinding.txtPinEntry.setText(s);
                        viewDialog.showDialog();
                        SendOTP.getInstance().getTrigger().verify(s);
                    }
                } else if (responseCode == SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER) {
                    // Otp send to number successfully
                    Toast.makeText(requireContext(), getString(R.string.successfull_send_otp), Toast.LENGTH_SHORT).show();
                } else if (responseCode == SendOTPResponseCode.SERVER_ERROR_OTP_NOT_VERIFIED) {
                    //exception found
                    Toast.makeText(requireContext(), getString(R.string.otp_not_verified), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp() {
        viewDialog.showDialog();
        mPresenter.signUp(firstName, lastName, email, mobile, aadhaarId, pinCode, password);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SendOTP.getInstance().getTrigger().stop();
    }

    @Override
    public void clickOnVerify() {
        viewDialog.showDialog();
        SendOTP.getInstance().getTrigger().verify(mBinding.txtPinEntry.getText().toString());
    }

    @Override
    public void clickOnResend() {
        viewDialog.showDialog();
        SendOTP.getInstance().getTrigger().resend(RetryType.TEXT);
    }

    @Override
    public void setRegistrationView(Response<RegistrationResponse> response) {
        viewDialog.hideDialog();
        if (response.isSuccessful() && response.body() != null) {
            if (response.body().isStatus()) {
                requireFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                requireFragmentManager().popBackStack();
                Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (response.errorBody() != null) {
            requireFragmentManager().popBackStack();
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(requireActivity(), jObjError.optString("message"), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            requireFragmentManager().popBackStack();
        }
    }

    @Override
    public void onError(String error) {
        viewDialog.hideDialog();
        requireFragmentManager().popBackStack();

    }
}
