package com.example.stayathome.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSManagerHelper {

    private int PERMISSION_ID = 44;
    private Activity activity;
    private LocationListener locationListener;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            locationListener.onTakeLocation(mLastLocation);
        }
    };

    public GPSManagerHelper(Activity activity) {
        this.activity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void setLocationListener(LocationListener listener) {
        locationListener = listener;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(final boolean isNew) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null || isNew) {
                                    requestNewLocationData();
                                } else {
                                    locationListener.onTakeLocation(location);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    public String getZipCode(double lat, double lon) throws IOException {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
        return addresses.get(0).getPostalCode();
    }

    public String getAddress(double lat, double lon) throws IOException {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
        return addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName() + ", " +
                addresses.get(0).getPostalCode();
    }

    public boolean isMockLocationOn(Location location, Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return location.isFromMockProvider();
        } else {
            String mockLocation = "0";
            try {
                mockLocation = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return !mockLocation.equals("0");
        }
    }

    public interface LocationListener {
        void onTakeLocation(Location location);
    }
}
