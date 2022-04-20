package in.calibrage.wsm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.highbryds.gpstracker.GPSService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GPSService.LocationInterval = 1*60*1000;
        GPSService.LocationFastestInterval = 1*60*1000;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        GPSService.contentIntent = contentIntent;
        GPSService.NotificationTitle = "your app is tracking you";
        GPSService.NotificationTxt = "Amazing Stuff";
        GPSService.drawable_small = R.drawable.ic_launcher_background;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(MainActivity.this, GPSService.class));
        } else {

            startService(new Intent(MainActivity.this, GPSService.class));
            Toast.makeText(MainActivity.this, "Tracking Started..", Toast.LENGTH_SHORT).show();
        }
    }
}
