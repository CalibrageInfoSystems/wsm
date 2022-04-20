package in.calibrage.wsm.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dmax.dialog.SpotsDialog;
import in.calibrage.wsm.R;
import in.calibrage.wsm.common.Common;
import in.calibrage.wsm.localData.SharedPrefsData;
import in.calibrage.wsm.model.AddressModel;
import in.calibrage.wsm.model.ReqTrip;
import in.calibrage.wsm.model.ResLastTrip;
import in.calibrage.wsm.service.APIConstantURL;
import in.calibrage.wsm.service.ApiService;
import in.calibrage.wsm.service.ServiceFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class TripActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final String TAG = TripActivity.class.getSimpleName();
    private ImageView btn_logout, img_refresh;
    private Button btn_trip;
    private Context ctx;
    private SpotsDialog mdilogue;
    private Subscription mSubscription;
    private RecyclerView rcv_items;
    private LinearLayoutManager lytManager;
    protected TripsAdapter adapter;
    private GoogleApiClient googleApiClient;
    private Location mylocation;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        init();
        setviews();
        if (isOnline()) {
            getTrips();
        } else
            Toast.makeText(ctx, ctx.getResources().getText(R.string.error_net), Toast.LENGTH_SHORT).show();
        /*setUpGClient();
        getMyLocation();*/
    }

    private void init() {
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        ctx = this;
        btn_logout = findViewById(R.id.btn_logout);
        btn_trip = findViewById(R.id.btn_trip);
        rcv_items = findViewById(R.id.rcv_items);
        img_refresh = findViewById(R.id.img_refresh);
        lytManager = new GridLayoutManager(ctx, 2);
    }

    private void setviews() {
        rcv_items.setLayoutManager(lytManager);
        btn_logout.setOnClickListener(this);
        btn_trip.setOnClickListener(this);
        img_refresh.setOnClickListener(this);
        // getTrips();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void startTrip() {
        mdilogue.show();
        JsonObject object = TripObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postTrip(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResLastTrip.Tripdate>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onError =" + e.getLocalizedMessage());
                        Toast.makeText(ctx, "You can't start trip now", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResLastTrip.Tripdate resTripData) {
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onNext-->> Token =" + resTripData.getCode());
                        mdilogue.cancel();
                      //  startService(TripActivity.this);
                        SharedPrefsData.saveTripDetails(ctx, resTripData);
                        Intent intent = new Intent(ctx, HomeActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // finish();
                    }
                });
    }

    private JsonObject TripObject() {
        ReqTrip requestModel = new ReqTrip();
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setTripCode("");
        requestModel.setStatusTypeId(1);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    private void getTrips() {
        mdilogue.show();
        ApiService apiService = ServiceFactory.createRetrofitService(ctx, ApiService.class);
        mSubscription = apiService.getCurrentDayTrips(APIConstantURL.GetCurrentDayTripsByUser + SharedPrefsData.getUserDetails(ctx).getUserInfos().getId()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResLastTrip>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onNext(ResLastTrip resLastTrip) {
                        mdilogue.cancel();
                        onPrintLog(TAG, "getTrips()", resLastTrip.getTripCount() + "");
                        adapter = new TripsAdapter(resLastTrip.getTripdate(), ctx);
                        rcv_items.setAdapter(adapter);
                       /* if (resLastTrip != null) {
                            if (resLastTrip.getTripdate().size() > 0) {
                                ResLastTrip.Tripdate tripdate = resLastTrip.getTripdate().get(resLastTrip.getTripdate().size() - 1);
                                SharedPrefsData.saveLastTripDetails(ctx,tripdate);
                                onPrintLog(TAG, "getStatusTypeId()", tripdate.getStatusTypeId() + "");
                                if (tripdate.getStatusTypeId() == Common.In_Progress) {

                                    Intent intent = new Intent(ctx, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }*/

                    }
                });
    }





/*

    private JsonObject getTripsObject() {
        ReqLastTrip requestModel = new ReqLastTrip();
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setFromDate("2019-9-26T16:00:51.222194+05:30");
        requestModel.setToDate("2019-10-26T16:00:51.222194+05:30");
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                stopService(TripActivity.this);
                SharedPrefsData.putBool(ctx, Common.isLogin, false);
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_trip:
                // startService(TripActivity.this);
                startTrip();
                break;
            case R.id.img_refresh:
                getTrips();
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            Double latitude = mylocation.getLatitude();
            Double longitude = mylocation.getLongitude();
            Log.d(TAG,"Locations : "+ "lat :"+latitude +"&"+ " long"+longitude);
            // Todo Locations Working fine
            AddressModel lastads=new AddressModel(latitude,longitude,Common.getCompleteAddressString(TripActivity.this, latitude,longitude));
            SharedPrefsData.saveAds(ctx,lastads);
        }
    }


    public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.viewHolder> {

        private List<ResLastTrip.Tripdate> tripslist = new ArrayList<>();
        private Context ctx;

        public TripsAdapter(List<ResLastTrip.Tripdate> tripslist, Context ctx) {
            this.tripslist = tripslist;
            this.ctx = ctx;

        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rcv_status_box, parent, false);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
            Integer po = position + 1;
            holder.txt_triptext.setText(po + "");
            if (tripslist.size() > position) {

                if (tripslist.get(position).getStatusTypeId() == Common.Not_started)
                    holder.txt_triptext.setBackgroundResource(R.drawable.bg_notstarted);
                else if (tripslist.get(position).getStatusTypeId() == Common.In_Progress)
                    holder.txt_triptext.setBackgroundResource(R.drawable.bg_inprogress);
                else if (tripslist.get(position).getStatusTypeId() == Common.Not_Yet_Verified)
                    holder.txt_triptext.setBackgroundResource(R.drawable.bg_notverified);
                else if (tripslist.get(position).getStatusTypeId() == Common.Approved)
                    holder.txt_triptext.setBackgroundResource(R.drawable.bg_aproved);
                else if (tripslist.get(position).getStatusTypeId() == Common.Declined)
                    holder.txt_triptext.setBackgroundResource(R.drawable.bg_decline);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tripslist.get(position).getStatusTypeId() == Common.Not_started)
                        startTrip();
                    else if (tripslist.get(position).getStatusTypeId() == Common.Declined || tripslist.get(position).getStatusTypeId() == Common.In_Progress) {
                        /* Saving last Clicked Trip Details in List insted passing Bundle */
                        SharedPrefsData.saveTripDetails(ctx, tripslist.get(position));
                        startActivity(new Intent(TripActivity.this, HomeActivity.class));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (tripslist != null)
                return tripslist.size();
            else
                return 0;
        }

        class viewHolder extends RecyclerView.ViewHolder {
            private TextView txt_triptext;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                txt_triptext = itemView.findViewById(R.id.txt_triptext);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(TripActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(TripActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }


    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(TripActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(5 * 60 * 1000);
                    locationRequest.setFastestInterval(5 * 60 * 1000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(TripActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(TripActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }

    }
}
