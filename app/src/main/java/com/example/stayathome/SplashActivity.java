package com.example.stayathome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stayathome.auth.LoginActivity;
import com.example.stayathome.auth.MeFragment;
import com.example.stayathome.utils.UserManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private Intent intentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (UserManager.getInstance().isUserLoggedIN())
            callMe();
        else
            init();
    }

    private void callMe() {
        MeFragment fragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromSplash", true);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void init() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                intentMain = new Intent(SplashActivity.this, LoginActivity.class);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMain);
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(mRunnable);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

}
