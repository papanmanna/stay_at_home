package com.example.stayathome.ui.request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.stayathome.R;
import com.example.stayathome.auth.LoginActivity;
import com.example.stayathome.databinding.ActivityRequestBinding;
import com.example.stayathome.models.CreateRequestResponse;
import com.example.stayathome.models.TimeModel;
import com.example.stayathome.utils.UserManager;
import com.example.stayathome.utils.Utils;
import com.example.stayathome.utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Response;

public class RequestActivity extends AppCompatActivity implements RequestViewModel {

    private static final int PICK_FILE_RESULT_CODE = 111;
    ActivityRequestBinding mBinding;
    private RequestPresenter presenter;
    private List<TimeModel> timeModelList = new ArrayList<>();
    private ViewDialog viewDialog;
    private int startHour, endHour;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request);
        mBinding.setHandler(this);
        presenter = new RequestPresenter();
        presenter.setViewModel(this);
        mBinding.psTv.setText(UserManager.getInstance().getUserPoliceStationName());
        mBinding.pinTv.setText(UserManager.getInstance().getUserPin());
        getTimeList();
        viewDialog = new ViewDialog(this);
        mBinding.startTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startHour = timeModelList.get(position).key;
                TimeListAdapter arrayAdapter = new TimeListAdapter(RequestActivity.this, R.layout.item_time, timeModelList.subList(position + 1, 24));
                mBinding.endTimeSpinner.setAdapter(arrayAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.endTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (timeModelList.size() > mBinding.startTimeSpinner.getSelectedItemPosition() + 1)
                    endHour = timeModelList.get(mBinding.startTimeSpinner.getSelectedItemPosition() + position + 1).key;
                else
                    endHour = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initDateTime();
    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mBinding.dateEt.setText(day + "/" + month + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.setTitle("Select Date");
        mBinding.dateEt.setText(dayOfMonth + "/" + month + "/" + year);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void getTimeList() {
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... objects) {
                try {
                    StringBuilder builder = new StringBuilder();
                    InputStream json = getAssets().open("time.json");
                    BufferedReader in =
                            new BufferedReader(new InputStreamReader(json, "UTF-8"));
                    String str;

                    while ((str = in.readLine()) != null) {
                        builder.append(str);
                    }
                    in.close();
                    JSONArray array = new JSONArray(builder.toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        TimeModel model = new TimeModel();
                        model.key = object.getInt("key");
                        model.value = object.getString("value");
                        timeModelList.add(model);
                    }
                    if (mBinding != null) {
                        TimeListAdapter arrayAdapter = new TimeListAdapter(RequestActivity.this, R.layout.item_time, timeModelList);
                        mBinding.endTimeSpinner.setAdapter(arrayAdapter);
                        mBinding.startTimeSpinner.setAdapter(arrayAdapter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.startTimeSpinner.setSelection(0);
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void clickOnDate() {
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    mBinding.attachFileTv.setText(FilePath);
                    mBinding.attachFileTv.setVisibility(View.VISIBLE);
                    mBinding.addFab.hide();
                }
                break;

        }
    }

    @Override
    public void clickOnAttach() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);
    }

    @Override
    public void clickOnClose() {
        mBinding.addFab.show();
        mBinding.attachFileTv.setText("");
        mBinding.attachFileTv.setVisibility(View.GONE);
    }

    @Override
    public void clickOnSubmit() {
        String reason = mBinding.reasonEt.getText().toString();
        String date = mBinding.dateEt.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            mBinding.reasonEt.setError("Please provide reason");
            return;
        }
        if (TextUtils.isEmpty(date)) {
            mBinding.dateEt.setError("Select date");
            return;
        }
        if (startHour >= endHour) {
            Toast.makeText(this, "You are not allowed at this time", Toast.LENGTH_SHORT).show();
            return;
        }

        viewDialog.showDialog();
        presenter.createRequest(UserManager.getInstance().getUserPoliceStationId(), reason, Utils.getMillisFromDate(date), startHour, endHour, 1);

    }

    @Override
    public void setHomeView(Response<CreateRequestResponse> response) {
        viewDialog.hideDialog();
        if (response.code() == 401) {
            UserManager.getInstance().logOutUser();
            Intent intentMain = new Intent(RequestActivity.this, LoginActivity.class);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentMain);
            finish();
            return;
        }
        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
            setResult(Activity.RESULT_OK);
            finish();
        } else if (response.errorBody() != null) {
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Toast.makeText(this, jObjError.optString("message"), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String message) {
        viewDialog.hideDialog();
    }
}
