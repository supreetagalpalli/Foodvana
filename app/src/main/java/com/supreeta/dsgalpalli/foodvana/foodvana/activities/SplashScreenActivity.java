package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants;

import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
                    if (preferences.getBoolean(Constants.KEY_LOGIN_STATUS, false) == true) {
                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    }

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
