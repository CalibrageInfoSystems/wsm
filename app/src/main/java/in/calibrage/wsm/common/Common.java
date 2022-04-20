package in.calibrage.wsm.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {
    /*
     * ALl Constants are Defined here and this Class Contains all Reused Wrapper Methods
     *
     * --- Created BY Mahesh M 25-09-2019
     * */


    public static final String USER_Details = "user_details";
    public static final String Trip_Details = "trip_details";
    public static final String LastTrip_Details = "last_trip_details";
    public static final String isLogin = "login";
    public static final String isAdmin = "isAdmin";

    public static final String tripstarted = "istripstarted";

    public static final String CHANNEL_ID = "in.calibrage.wsm";


/*
    1--In Progress
2--Not Yet Verified
3--Approved
4--Declined*/

    public static final Integer Not_started = 0;
    public static final Integer In_Progress = 1;
    public static final Integer Not_Yet_Verified = 2;
    public static final Integer Approved = 3;
    public static final Integer Declined = 4;
    public static final int GPS_REQUEST = 10;
    public static final int LOCATION_REQUEST = 11;


    @SuppressLint("MissingPermission")
    public static Location getLastKnownLoaction(boolean enabledProvidersOnly, Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location utilLocation = null;
        List<String> providers = manager.getProviders(enabledProvidersOnly);
        for(String provider : providers){

            utilLocation = manager.getLastKnownLocation(provider);
            if(utilLocation != null) return utilLocation;
        }
        return null;
    }
    public static String getCompleteAddressString( Context ctx, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {
                Log.w("getComplete", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("getComplete", "Canont get Address!");
        }
        return strAdd;
    }
}
