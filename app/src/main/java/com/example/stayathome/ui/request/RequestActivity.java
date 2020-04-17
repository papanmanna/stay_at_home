package com.example.stayathome.ui.request;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.stayathome.R;
import com.example.stayathome.auth.LoginActivity;
import com.example.stayathome.databinding.ActivityRequestBinding;
import com.example.stayathome.models.CreateRequestResponse;
import com.example.stayathome.models.Station;
import com.example.stayathome.models.TimeModel;
import com.example.stayathome.utils.GPSManagerHelper;
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

public class RequestActivity extends AppCompatActivity implements RequestViewModel, GPSManagerHelper.LocationListener {

    private static final int PICK_FILE_RESULT_CODE = 111;
    ActivityRequestBinding mBinding;
    private RequestPresenter presenter;
    private List<TimeModel> timeModelList = new ArrayList<>();
    private ViewDialog viewDialog;
    private int startHour, endHour;
    private DatePickerDialog datePickerDialog;
    private GPSManagerHelper gpsManagerHelper;
    private int PERMISSION_ID = 44;
    private boolean isSettingOpen, isMockEnabled;
    List<Station> stations;
    private String policeStationId;
    private double lat, lon;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_request);
        mBinding.setHandler(this);
        presenter = new RequestPresenter();
        presenter.setViewModel(this);
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
        gpsManagerHelper = new GPSManagerHelper(this);
        gpsManagerHelper.setLocationListener(this);
        gpsManagerHelper.getLastLocation(false);
        initDateTime();
        initStationSpinner();
    }

    private void initStationSpinner() {
        stations = UserManager.getInstance().getUserPoliceStations();
        StationListAdapter arrayAdapter = new StationListAdapter(RequestActivity.this, R.layout.item_time, stations);
        mBinding.pspinner.setAdapter(arrayAdapter);
        mBinding.pspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                policeStationId = stations.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSettingOpen && gpsManagerHelper != null) {
            gpsManagerHelper.getLastLocation(true);
            isSettingOpen = false;
        }

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
        if (policeStationId == null || TextUtils.isEmpty(policeStationId)) {
            TextView errorText = (TextView) mBinding.pspinner.getSelectedView();
            errorText.setError("Select police station");
            return;
        }
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

        if (address == null) {
            Toast.makeText(this, "Unable to detect location", Toast.LENGTH_SHORT).show();
            gpsManagerHelper.getLastLocation(true);
            return;
        }

        viewDialog.showDialog();
        presenter.createRequest(policeStationId, reason, Utils.getMillisFromDate(date), startHour, endHour, lat, lon, address, 1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                gpsManagerHelper.getLastLocation(false);
            } else if (Build.VERSION.SDK_INT >= 23 && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // User selected the Never Ask Again Option Change settings in app settings manually
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder
                        .setTitle(getResources().getString(R.string.alert_title))
                        .setMessage(getResources().getString(R.string.alert_location_body))
                        .setCancelable(false)
                        .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isSettingOpen = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            } else {
                // User selected Deny Dialog to EXIT App ==> OR <== RETRY to have a second chance to Allow Permissions
                gpsManagerHelper.requestPermissions();
            }
        }
    }

    @Override
    public void onTakeLocation(Location location) {
        if (!gpsManagerHelper.isMockLocationOn(location, this)) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            try {
                address = gpsManagerHelper.getAddress(lat, lon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(getResources().getString(R.string.alert_title))
                    .setMessage(getResources().getString(R.string.alert_mock_location_body))
                    .setCancelable(false)
                    .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
