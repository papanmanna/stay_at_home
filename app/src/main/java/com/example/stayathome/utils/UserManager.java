package com.example.stayathome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stayathome.BaseApplication;
import com.example.stayathome.models.Station;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserManager {


    private static final String USER_FNAME = "fname";
    private static final String USER_LNAME = "lname";
    private static final String USER_AADHAAR = "aadhaar";
    private static final String USER_PIN = "pin";
    private static final String USER_POLICE_STATIONS = "pstations";
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

    public void updateUserData(String fName, String lName, String aadhaarId, String pinCode, List<Station> policeStation, String email, String phone) {
        editor.putString(USER_FNAME, fName);
        editor.putString(USER_LNAME, lName);
        editor.putString(USER_AADHAAR, aadhaarId);
        editor.putString(USER_PIN, pinCode);
        if (policeStation != null) {
            setList(USER_POLICE_STATIONS, policeStation);
        }
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_PHONE, phone);
        editor.apply();
    }

    private <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key, json);
        editor.apply();
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


    public List<Station> getUserPoliceStations() {
        Gson gson = new Gson();
        List<Station> stationsFromShared;
        String jsonPreferences = sharedPreferences.getString(USER_POLICE_STATIONS, "");

        Type type = new TypeToken<List<Station>>() {}.getType();
        stationsFromShared = gson.fromJson(jsonPreferences, type);

        return stationsFromShared;
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
