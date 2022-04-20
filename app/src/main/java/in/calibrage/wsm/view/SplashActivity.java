package in.calibrage.wsm.view;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import in.calibrage.wsm.R;
import in.calibrage.wsm.common.Common;
import in.calibrage.wsm.localData.SharedPrefsData;

public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        updateResources(this,"te");

        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            // Write you code here if permission already given.
           // canToggleGPS(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (SharedPrefsData.getBool(SplashActivity.this, Common.isLogin)) {

                        if (SharedPrefsData.getBool(SplashActivity.this, Common.isAdmin)) {
                            Intent intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, TripActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                }


            }, SPLASH_TIME_OUT);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (SharedPrefsData.getBool(SplashActivity.this, Common.isLogin)) {

                            if (SharedPrefsData.getBool(SplashActivity.this, Common.isAdmin)) {
                                Intent intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(SplashActivity.this, TripActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }


                }, SPLASH_TIME_OUT);
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
