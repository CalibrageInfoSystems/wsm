package in.calibrage.wsm.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.highbryds.gpstracker.GPSService;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import in.calibrage.wsm.R;

public  class BaseActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
    void startService(Activity activity) {
        GPSService.LocationInterval = 5 * 60 * 1000;
        GPSService.LocationFastestInterval = 5 * 60 * 1000;
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
                new Intent(activity, TripActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        GPSService.contentIntent = contentIntent;
        GPSService.NotificationTitle = "wsm";
        GPSService.NotificationTxt = "wsm location service";
        GPSService.drawable_small = R.mipmap.ic_launcher;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(activity, GPSService.class));
            onPrintLog(activity.getClass().getSimpleName(),"startForegroundService()","started");
        } else {
            startService(new Intent(activity, GPSService.class));
            onPrintLog(activity.getClass().getSimpleName(),"startService()","started");
        }
    }
    void stopService(Activity activity)
    {
        Intent myService = new Intent(activity, GPSService.class);
        stopService(myService);
        onPrintLog(activity.getClass().getSimpleName(),"stopService()","started");

    }
    void onPrintLog(String TAG,String method,String responce)
    {
        Log.d(TAG,"--- analysis ----->>"+method+"--->> result :"+responce);
    }
    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
