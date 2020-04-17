package com.example.stayathome.ui.reset_password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stayathome.R;
import com.example.stayathome.databinding.FragmentChangePasswordBinding;
import com.example.stayathome.utils.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements ChangePasswordViewModel {

    private FragmentChangePasswordBinding mBinding;
    private ChangePasswordPresenter mPresenter;
    private ViewDialog viewDialog;
    private String mobile;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        mBinding.setClickHandler(this);
        if (getArguments() != null) {
            mobile = getArguments().getString("mobile");
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new ChangePasswordPresenter();
        mPresenter.setView(this);
        viewDialog = new ViewDialog(requireActivity());

    }

    @Override
    public void setChangePasswordView(Response<ResetPasswordResponse> response) {
        viewDialog.hideDialog();

        if (response.isSuccessful() && response.body() != null) {
            Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            if (response.body().isStatus()) {
                requireFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else if (response.errorBody() != null) {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(requireActivity(), jObjError.optString("message"), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        viewDialog.hideDialog();
    }

    @Override
    public void clickOnChangePassword() {

        String newPassword = mBinding.etNewPassword.getText().toString();
        String confirmPassword = mBinding.etConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(newPassword)) {
            mBinding.etNewPassword.setError(getString(R.string.error_password));
            return;
        } else if (newPassword.length() < 6) {
            mBinding.etNewPassword.setError(getString(R.string.error_password_length));
            return;
        }

        if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            mBinding.etConfirmPassword.setError(getString(R.string.password_not_match));
            return;
        }

        viewDialog.showDialog();
        mPresenter.changePassword(mobile, newPassword);
    }
}
