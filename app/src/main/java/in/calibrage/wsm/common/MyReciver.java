package in.calibrage.wsm.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import in.calibrage.wsm.localData.SharedPrefsData;
import in.calibrage.wsm.model.AddressModel;
import in.calibrage.wsm.model.ReqAddGeoLatLong;
import in.calibrage.wsm.model.ResGeoLatLong;
import in.calibrage.wsm.service.ApiService;
import in.calibrage.wsm.service.ServiceFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tahir.raza on 12/2/2017.
 */

public class MyReciver extends BroadcastReceiver {

    public static final String TAG = MyReciver.class.getSimpleName();
    private Subscription mSubscription;
    private Context ctx;
    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        //speedspeedspeed
        double speed = Double.valueOf(intent.getStringExtra("speed"));
       // double altitude = Double.valueOf(intent.getStringExtra("altitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        System.out.println("broadcast latitude:" + latitude);
        System.out.println("broadcast speed:" + speed);
        System.out.println("broadcast longitude:" + longitude);
       addTripImage(context,latitude,longitude,"Sample Address");

        //  double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        //  Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }
    public void addTripImage( final Context ctx,Double lat,Double lo,String addrs) {

        JsonObject object = GeoObject(lat,lo,addrs);
        ApiService service = ServiceFactory.createRetrofitService(ctx, ApiService.class);
        mSubscription = service.addGeoLocations(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResGeoLatLong>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "----- analysis --->> ResGeoLatLong()-->>> onError =" + e.getLocalizedMessage());
                        Toast.makeText(ctx, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResGeoLatLong resTripImages) {
                        Log.d(TAG, "----- analysis --->> ResGeoLatLong()-->>> onNext-->> Token =" + resTripImages.getStatusMessage());




                    }
                });
    }

    private JsonObject GeoObject(Double lat,Double lo,String addrs) {
        ReqAddGeoLatLong requestModel = new ReqAddGeoLatLong();
        requestModel.setCode(SharedPrefsData.getTripDetails(ctx).getCode());
        requestModel.setLatitude(lat);
        requestModel.setLongitude(lo);
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setAddress(getCompleteAddressString(lat,lo));
        AddressModel lastads=new AddressModel(lat,lo,getCompleteAddressString(lat,lo));
        SharedPrefsData.saveAds(ctx,lastads);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
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
                Log.d(TAG, strReturnedAddress.toString());
            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Canont get Address!");
        }
        return strAdd;
    }
}
