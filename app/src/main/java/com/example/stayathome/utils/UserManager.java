package com.example.stayathome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stayathome.BaseApplication;
import com.example.stayathome.models.Station;

public class UserManager {


    private static final String USER_FNAME = "fname";
    private static final String USER_LNAME = "lname";
    private static final String USER_AADHAAR = "aadhaar";
    private static final String USER_PIN = "pin";
    private static final String USER_POLICE_STATION_ID = "pstation_id";
    private static final String USER_POLICE_STATION_NAME = "pstation_name";
    private static final String USER_EMAIL = "email";
    private static final String USER_PHONE = "phone";
    private static UserManager instance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "sah";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String IS_USER_LOGGED_IN = "is_user_logged_in";


    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
            sharedPreferences = BaseApplication.getInstance().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return instance;
    }

    public void updateAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, null);
    }


    public boolean isUserLoggedIN() {
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public void loginUser(String accessToken, boolean isLoggedin) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putBoolean(IS_USER_LOGGED_IN, isLoggedin);
        editor.apply();
    }

    public void updateUserData(String fName, String lName, String aadhaarId, String pinCode, Station policeStation, String email, String phone) {
        editor.putString(USER_FNAME, fName);
        editor.putString(USER_LNAME, lName);
        editor.putString(USER_AADHAAR, aadhaarId);
        editor.putString(USER_PIN, pinCode);
        if (policeStation != null) {
            editor.putString(USER_POLICE_STATION_ID, policeStation.getId());
            editor.putString(USER_POLICE_STATION_NAME, policeStation.getStationName());
        }
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_PHONE, phone);
        editor.apply();
    }

    public String getUserPoliceStationId() {
        return sharedPreferences.getString(USER_POLICE_STATION_ID, "");
    }

    public String getUserFname() {
        return sharedPreferences.getString(USER_FNAME, "");
    }

    public String getUserLname() {
        return sharedPreferences.getString(USER_LNAME, "");
    }

    public String getUserAadhaar() {
        return sharedPreferences.getString(USER_AADHAAR, "");
    }

    public String getUserPin() {
        return sharedPreferences.getString(USER_PIN, "");
    }

    public String getUserPoliceStationName() {
        return sharedPreferences.getString(USER_POLICE_STATION_NAME, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE, "");
    }

    public void logOutUser() {
        editor.clear().commit();
    }

    public String getBearerAccessToken() {
        return "Bearer " + getAccessToken();
    }
}
