package in.calibrage.wsm.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import dmax.dialog.SpotsDialog;
import in.calibrage.wsm.R;
import in.calibrage.wsm.common.Common;
import in.calibrage.wsm.common.ImageUtil;
import in.calibrage.wsm.localData.SharedPrefsData;
import in.calibrage.wsm.model.AddressModel;
import in.calibrage.wsm.model.ReqAddGeoLatLong;
import in.calibrage.wsm.model.ReqAddTripImage;
import in.calibrage.wsm.model.ReqTrip;
import in.calibrage.wsm.model.ResGeoLatLong;
import in.calibrage.wsm.model.ResLastTrip;
import in.calibrage.wsm.model.ResTripImages;
import in.calibrage.wsm.service.APIConstantURL;
import in.calibrage.wsm.service.ApiService;
import in.calibrage.wsm.service.ServiceFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleApiClient googleApiClient;
    private Location mylocation;
    public static final String TAG = HomeActivity.class.getSimpleName();
    private Context ctx;
    private ImageView btn_logout;
    private Bitmap bitmap;
    private Button btn_trip, btn_submit;
    private final int PICK_FROM_CAMERA = 200;
    private final int PICK_FROM_GALLERY = 001;
    private final int PICK_FROM_CAMERA1 = 101;
    private final int PICK_FROM_CAMERA2 = 102;
    private final int PICK_FROM_CAMERA3 = 103;
    private final int PICK_FROM_CAMERA4 = 104;
    private final int PICK_FROM_CAMERA5 = 105;
    private final int PICK_FROM_CAMERA6 = 106;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private Subscription mSubscription;
    private SpotsDialog mdilogue;

    private ImageView img1, img2, img3, img4, img5, img6, imgc1, imgc2, imgc3, imgc4, imgc5, imgc6;

    private ResTripImages resTripImagesList = new ResTripImages();


    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isContinue = false;
    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        onSetupToolbar();
        init();
        setviews();


        /* getTrips();
         *//*if (SharedPrefsData.getLastTripDetails(ctx) != null) {
            getTripImages();
        }*/
        getTripImages();
        //startService(HomeActivity.this);
        setUpGClient();
        getMyLocation();

    }


    private void init() {
        ctx = this;
        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        btn_logout = findViewById(R.id.btn_logout);
        btn_trip = findViewById(R.id.btn_trip);
        btn_submit = findViewById(R.id.btn_submit);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);

        imgc1 = findViewById(R.id.img_ca1);
        imgc2 = findViewById(R.id.img_ca2);
        imgc3 = findViewById(R.id.img_ca3);
        imgc4 = findViewById(R.id.img_ca4);
        imgc5 = findViewById(R.id.img_ca5);
        imgc6 = findViewById(R.id.img_ca6);

        imgc1.setVisibility(View.GONE);
        imgc2.setVisibility(View.GONE);
        imgc3.setVisibility(View.GONE);
        imgc4.setVisibility(View.GONE);
        imgc5.setVisibility(View.GONE);
        imgc6.setVisibility(View.GONE);

        btn_trip.setVisibility(View.GONE);
    }

    private void setviews() {
        btn_logout.setOnClickListener(this);
        btn_trip.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);

        imgc1.setOnClickListener(this);
        imgc2.setOnClickListener(this);
        imgc3.setOnClickListener(this);
        imgc4.setOnClickListener(this);
        imgc5.setOnClickListener(this);
        imgc6.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == btn_logout.getId()) {
            stopService(HomeActivity.this);
            SharedPrefsData.putBool(ctx, Common.isLogin, false);
            Intent intent = new Intent(ctx, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (view.getId() == btn_trip.getId()) {
            if (isOnline())
                startTrip();
            else
                Toast.makeText(ctx, "Please Check Your Internet Connection ...", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == img1.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA1);
            }
        } else if (view.getId() == img2.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA2);
            }
        } else if (view.getId() == img3.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA3);
            }
        } else if (view.getId() == img4.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA4);
            }
        } else if (view.getId() == img5.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA5);
            }
        } else if (view.getId() == img6.getId()) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, PICK_FROM_CAMERA6);
            }
        } else if (view.getId() == btn_submit.getId()) {
            if (isOnline())
                endTrip();
            else
                Toast.makeText(ctx, "error_net", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == imgc1.getId()) {
            if (!resTripImagesList.getData().isEmpty() & resTripImagesList.getData().size() > 0)
                deleteTripImages(resTripImagesList.getData().get(0), 0);
        } else if (view.getId() == imgc2.getId()) {
            if (resTripImagesList.getData() != null & resTripImagesList.getData().size() > 1)
                deleteTripImages(resTripImagesList.getData().get(1), 1);
        } else if (view.getId() == imgc3.getId()) {
            if (resTripImagesList.getData() != null & resTripImagesList.getData().size() > 2)
                deleteTripImages(resTripImagesList.getData().get(2), 2);
        } else if (view.getId() == imgc4.getId()) {
            if (resTripImagesList.getData() != null & resTripImagesList.getData().size() > 3)
                deleteTripImages(resTripImagesList.getData().get(3), 3);
        } else if (view.getId() == imgc5.getId()) {
            if (resTripImagesList.getData() != null & resTripImagesList.getData().size() > 4)
                deleteTripImages(resTripImagesList.getData().get(4), 4);
        } else if (view.getId() == imgc6.getId()) {
            if (resTripImagesList.getData() != null & resTripImagesList.getData().size() > 5)
                deleteTripImages(resTripImagesList.getData().get(5), 5);
        }


    }

    //region Local Methods
    public void ShowImage(Activity activity) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        Button btn_camera, btn_galley, btn_cancel;
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_galley = dialog.findViewById(R.id.btn_galley);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_CAMERA);
                } else {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
            }
        });
        btn_galley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                try {
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void onSetupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_CAMERA1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img1);
                    //addTripImage(ImageUtil.convert(bitmap));
                }

                break;
            case PICK_FROM_CAMERA2:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img2);
                    //img2.setImageBitmap(bitmap);
                }

                break;
            case PICK_FROM_CAMERA3:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img3);
                    //img3.setImageBitmap(bitmap);
                }

                break;
            case PICK_FROM_CAMERA4:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img4);
                    //img4.setImageBitmap(bitmap);
                }

                break;
            case PICK_FROM_CAMERA5:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img5);
                    // img5.setImageBitmap(bitmap);
                }

                break;
            case PICK_FROM_CAMERA6:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.d(TAG, "--- analysis ----->> GetImage From Camera >> Base64 =");
                    addTripImage(ImageUtil.convert(bitmap), img6);
                    //  img6.setImageBitmap(bitmap);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                        Log.d(TAG, "--- analysis ----->> GetImage From Gallery >> Base64 =");
                        if (isOnline())
                            addTripImage(ImageUtil.convert(bitmap), img5);
                        else
                            Toast.makeText(ctx, "error_net", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    //endregion

    public void endTrip() {
        mdilogue.show();
        JsonObject object = endTripObject();
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
                        Toast.makeText(ctx, "str_tripnotcompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResLastTrip.Tripdate resTripData) {
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onNext-->> Token =" + resTripData.getCode());
                        mdilogue.cancel();
                        //SharedPrefsData.saveTripDetails(ctx, resTripData);
                        Toast.makeText(ctx, "str_tripcompleted", Toast.LENGTH_SHORT).show();

                        img1.setImageResource(R.drawable.ic_addimg);
                        img2.setImageResource(R.drawable.ic_addimg);
                        img3.setImageResource(R.drawable.ic_addimg);
                        img4.setImageResource(R.drawable.ic_addimg);
                        img5.setImageResource(R.drawable.ic_addimg);
                        img6.setImageResource(R.drawable.ic_addimg);

                        imgc1.setVisibility(View.GONE);
                        imgc2.setVisibility(View.GONE);
                        imgc3.setVisibility(View.GONE);
                        imgc4.setVisibility(View.GONE);
                        imgc5.setVisibility(View.GONE);
                        imgc6.setVisibility(View.GONE);
                        btn_trip.setVisibility(View.GONE);
                        stopService(HomeActivity.this);
                        Intent intent = new Intent(ctx, TripActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        // btn_trip.setVisibility(View.VISIBLE);
                    }
                });
    }

    private JsonObject endTripObject() {
        ReqTrip requestModel = new ReqTrip();
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setTripCode(SharedPrefsData.getTripDetails(ctx).getCode());
        requestModel.setStatusTypeId(2);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public void startTrip() {
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
                        Toast.makeText(ctx, "str_starttripnow", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(ResLastTrip.Tripdate resTripData) {
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onNext-->> Token =" + resTripData.getCode());
                        mdilogue.cancel();
                       /* SharedPrefsData.saveLastTripDetails(ctx, resTripData);
                        SharedPrefsData.putBool(ctx, Common.isLogin, true);*/
                    }
                });
    }

    private JsonObject TripObject() {
        ReqTrip requestModel = new ReqTrip();
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setTripCode(null);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public void addTripImage(String imgBase64, ImageView imgview) {
        mdilogue.show();


        JsonObject object = TripImageObject(imgBase64, imgview);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postTripImage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResTripImages>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onError =" + e.getLocalizedMessage());
                        Toast.makeText(ctx, getResources().getString(R.string.str_tryagain), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResTripImages resTripImages) {
                        Log.d(TAG, "----- analysis --->> startTrip()-->>> onNext-->> Token =" + resTripImages.getAffectedRows());
                        mdilogue.cancel();
                        //Toast.makeText(ctx, "image uploaded successfully", Toast.LENGTH_SHORT).show();
                        resTripImagesList = resTripImages;

                        if (resTripImages.getData().size() > 0) {

                            for (ResTripImages.Datum data : resTripImages.getData()) {

                                if (data.getSqNo() == 1) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img1);
                                }
                                if (data.getSqNo() == 2) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img2);
                                }
                                if (data.getSqNo() == 3) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img3);
                                }
                                if (data.getSqNo() == 4) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img4);
                                }
                                if (data.getSqNo() == 5) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img5);
                                }
                                if (data.getSqNo() == 6) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img6);
                                }

                            }


                        }
                    }
                });
    }

    private JsonObject TripImageObject(String imgBase64, ImageView imgview) {

        ReqAddTripImage requestModel = new ReqAddTripImage();

        requestModel.setId(0);
        requestModel.setLatitude(SharedPrefsData.getAds(ctx).getLat());
        requestModel.setLongitude(SharedPrefsData.getAds(ctx).getLog());
        requestModel.setAddress(SharedPrefsData.getAds(ctx).getAdrs());
        requestModel.setCode(SharedPrefsData.getTripDetails(ctx).getCode());
        if (imgview.getId() == img1.getId()) {
            requestModel.setSqNo(1);
        }
        if (imgview.getId() == img2.getId()) {
            requestModel.setSqNo(2);
        }
        if (imgview.getId() == img3.getId()) {
            requestModel.setSqNo(3);
        }
        if (imgview.getId() == img4.getId()) {
            requestModel.setSqNo(4);
        }
        if (imgview.getId() == img5.getId()) {
            requestModel.setSqNo(5);
        }
        if (imgview.getId() == img6.getId()) {
            requestModel.setSqNo(6);
        }
        requestModel.setFileExtension(".png");
        requestModel.setFileLocation("Android");
        requestModel.setCreatedDate("2019-09-26T10:13:38.6128877+05:30");
        requestModel.setUpdatedDate("2019-09-26T10:13:38.6128877+05:30");
        requestModel.setIsActive(true);
        requestModel.setCreatedByUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setUpdatedByUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        //   requestModel.setCode(SharedPrefsData.getLastTripDetails(ctx).getCode());
        requestModel.setFileName(imgBase64);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    private void deleteTripImages(ResTripImages.Datum imgdata, final int po) {
        mdilogue.show();
        ApiService apiService = ServiceFactory.createRetrofitService(ctx, ApiService.class);
        mSubscription = apiService.deleteTripImage(APIConstantURL.DELETE_TRIP_IMAGE + imgdata.getId()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onNext(String s) {
                        mdilogue.cancel();
                        if (po == 0) {
                            img1.setImageResource(R.drawable.ic_addimg);
                            imgc1.setVisibility(View.GONE);
                        } else if (po == 1) {
                            img2.setImageResource(R.drawable.ic_addimg);
                            imgc2.setVisibility(View.GONE);
                        } else if (po == 2) {
                            img3.setImageResource(R.drawable.ic_addimg);
                            imgc3.setVisibility(View.GONE);
                        } else if (po == 3) {
                            img4.setImageResource(R.drawable.ic_addimg);
                            imgc4.setVisibility(View.GONE);
                        } else if (po == 4) {
                            img5.setImageResource(R.drawable.ic_addimg);
                            imgc6.setVisibility(View.GONE);
                        } else if (po == 5) {
                            img6.setImageResource(R.drawable.ic_addimg);
                            imgc6.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void getTripImages() {
        mdilogue.show();
        ApiService apiService = ServiceFactory.createRetrofitService(ctx, ApiService.class);
        mSubscription = apiService.getTripImages(APIConstantURL.GetTripImagesByCode + SharedPrefsData.getTripDetails(ctx).getCode()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResTripImages>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onNext(ResTripImages resTripImages) {
                        mdilogue.cancel();
                        resTripImagesList = resTripImages;
                        /*
                         * check each item in list based on Possition assighn values
                         *
                         * */
                        if (resTripImages.getData().size() > 0) {

                            for (ResTripImages.Datum data : resTripImages.getData()) {

                                if (data.getSqNo() == 1) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img1);
                                }
                                if (data.getSqNo() == 2) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img2);
                                }
                                if (data.getSqNo() == 3) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img3);
                                }
                                if (data.getSqNo() == 4) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img4);
                                }
                                if (data.getSqNo() == 5) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img5);
                                }
                                if (data.getSqNo() == 6) {
                                    Picasso.with(ctx).load(data.getTripImageUrl()).into(img6);
                                }


                            }
                        }

                    }
                });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Common.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(HomeActivity.this, location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        Log.d(TAG, "-- analysis --- getLocationutil()" + "   lat:" + wayLatitude + "   lang:" + wayLongitude);
                        //txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                    } else {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            }
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
        if (requestCode == Common.GPS_REQUEST) {
            isGPS = true; // flag maintain before get location
        }
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case PICK_FROM_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ctx, "str_captureimg", Toast.LENGTH_SHORT).show();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;


        }
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(HomeActivity.this, location -> {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                Log.d(TAG, "-- analysis --- getLocationutil()" + "   lat:" + wayLatitude + "   lang:" + wayLongitude);
                            } else {
                                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
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
            // Todo Locations Working fine
            AddressModel lastads=new AddressModel(latitude,longitude,Common.getCompleteAddressString(HomeActivity.this, latitude,longitude));
            Log.d(TAG,"Locations : "+ "lat :"+latitude +"&"+ " long"+longitude);
            SharedPrefsData.saveAds(ctx,lastads);
            addGeoBounds(HomeActivity.this);
        }

    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
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
                int permissionLocation = ContextCompat.checkSelfPermission(HomeActivity.this,
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
                                            .checkSelfPermission(HomeActivity.this,
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
                                        status.startResolutionForResult(HomeActivity.this,
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



    public void addGeoBounds( final Context ctx) {

        JsonObject object = GeoObject();
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

    private JsonObject GeoObject() {
        ReqAddGeoLatLong requestModel = new ReqAddGeoLatLong();
        requestModel.setCode(SharedPrefsData.getTripDetails(ctx).getCode());
        requestModel.setLatitude(SharedPrefsData.getAds(ctx).getLat());
        requestModel.setLongitude(SharedPrefsData.getAds(ctx).getLog());
        requestModel.setUserId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setAddress(SharedPrefsData.getAds(ctx).getAdrs());

        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
