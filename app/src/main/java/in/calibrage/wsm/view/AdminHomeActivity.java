package in.calibrage.wsm.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import dmax.dialog.SpotsDialog;
import in.calibrage.wsm.R;
import in.calibrage.wsm.adapter.DistAdapter;
import in.calibrage.wsm.adapter.DivisionAdapter;
import in.calibrage.wsm.adapter.MandalAdapter;
import in.calibrage.wsm.adapter.VillageAdapter;
import in.calibrage.wsm.common.Common;
import in.calibrage.wsm.localData.SharedPrefsData;
import in.calibrage.wsm.model.ReqDevision;
import in.calibrage.wsm.model.ReqDist;
import in.calibrage.wsm.model.ReqRegister;
import in.calibrage.wsm.model.ReqVillage;
import in.calibrage.wsm.model.Reqmandal;
import in.calibrage.wsm.model.ResDist;
import in.calibrage.wsm.model.ResDivision;
import in.calibrage.wsm.model.ResMandal;
import in.calibrage.wsm.model.ResRegister;
import in.calibrage.wsm.model.ResVillage;
import in.calibrage.wsm.service.ApiService;
import in.calibrage.wsm.service.ServiceFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdminHomeActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = AdminHomeActivity.class.getSimpleName();
    private Subscription mSubscription;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputFirstName;
    private TextInputLayout textInputMiddleName;
    private TextInputLayout textInputLastName;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;
    private TextInputLayout textInputMobileNumber;
    private TextInputLayout textInputAddress;
    private TextInputLayout textInputVehicleNumber;
    private TextInputEditText txt_firstName, txt_middlename, txt_lastname, txt_username, txt_email, txt_password, txt_confirmpassword, txt_mobilNumber, txt_addres, txt_vechilnumber;
    private Button driverLoginBtn;
    private ImageView btn_logout;
    private Context ctx;
    private SpotsDialog mdilogue;
    private Spinner distSpinner, divisionSpinner, mandalSpinner, villageSpinner;
    String villageID = "1";

    // Get Address
    private DistAdapter mDistAdapter;
    private DivisionAdapter mDivisonAdapternew;
    private MandalAdapter mMandalAdapter;
    private VillageAdapter mVillageAdapter;
    private Button addDriverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        addDriverBtn = findViewById(R.id.addDriverBtn);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        init();

        setViews();
        GetDists();

        addDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isEmptyField(txt_firstName)) {

                    Toast.makeText(ctx, "Enter First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_lastname)){

                    Toast.makeText(ctx, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_middlename)) {

                    Toast.makeText(ctx, "Enter Middle Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_username)){

                    Toast.makeText(ctx, "Enter User Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_email)){

                    Toast.makeText(ctx, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_mobilNumber)) {

                    Toast.makeText(ctx, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_password)) {

                    Toast.makeText(ctx, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_confirmpassword)) {

                    Toast.makeText(ctx, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_addres)){

                    Toast.makeText(ctx, "Enter Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmptyField(txt_vechilnumber)){

                    Toast.makeText(ctx, "Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                register();

            }
        });


        //GetDevisons();
        //  GetMandals();
        //GetVillages();

    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
        return result;
    }


    private void setViews() {
        distSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ResDist dist = mDistAdapter.getItem(i);
                Toast.makeText(ctx, dist.getName(), Toast.LENGTH_SHORT).show();
                GetDevisons(dist.getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ResDivision division = mDivisonAdapternew.getItem(i);
                Toast.makeText(ctx, division.getName(), Toast.LENGTH_SHORT).show();
                GetMandals(division.getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mandalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ResMandal mandal = mMandalAdapter.getItem(i);
                Toast.makeText(ctx, mandal.getName(), Toast.LENGTH_SHORT).show();
                GetVillages(mandal.getId() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ResVillage village = mVillageAdapter.getItem(i);
                villageID = village.getId() + "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        ctx = this;
        textInputUserName = findViewById(R.id.text_userName);
        textInputFirstName = findViewById(R.id.text_firstName);
        textInputMiddleName = findViewById(R.id.text_middleName);
        textInputLastName = findViewById(R.id.text_lastName);
        textInputEmail = findViewById(R.id.text_email);
        textInputPassword = findViewById(R.id.text_password);
        textInputConfirmPassword = findViewById(R.id.text_cofirmPassword);
        textInputMobileNumber = findViewById(R.id.text_mobileNumber);
        textInputAddress = findViewById(R.id.text_address);
        textInputVehicleNumber = findViewById(R.id.text_vehicleNumber);
        driverLoginBtn = findViewById(R.id.driverLoginBtn);
        distSpinner = findViewById(R.id.distSpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        mandalSpinner = findViewById(R.id.mandalSpinner);
        villageSpinner = findViewById(R.id.villageSpinner);


        txt_firstName = findViewById(R.id.txt_firstName);
        txt_middlename = findViewById(R.id.txt_middlename);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_confirmpassword = findViewById(R.id.txt_confirmpassword);
        txt_mobilNumber = findViewById(R.id.txt_mobilNumber);
        txt_vechilnumber = findViewById(R.id.txt_vechilnumber);

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
    }

    private boolean validateEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {

            textInputEmail.setError(ctx.getText(R.string.str_fieldempty));
            return false;
        } else if (emailInput.length() > 100) {

            textInputEmail.setError("Email too Long");
            return false;
        } else {

            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

//    private boolean validateUsername() {
//
//        String userName = textInputUserName.getEditText().getText().toString().trim();
//
//        if (userName.isEmpty()) {
//
//            textInputUserName.setError("str_fieldempty");
//            return false;
//        } else if (userName.length() > 100) {
//
//            textInputUserName.setError("UserName too Long");
//            return false;
//        } else {
//
//            textInputUserName.setError(null);
//            textInputUserName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateFirstname() {
//
//        String firstName = textInputFirstName.getEditText().getText().toString().trim();
//
//        if (firstName.isEmpty()) {
//
//            textInputFirstName.setError("str_fieldempty");
//            return false;
//        } else if (firstName.length() > 100) {
//
//            textInputFirstName.setError("FirstName too Long");
//            return false;
//        } else {
//
//            textInputFirstName.setError(null);
//            textInputFirstName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateLastname() {
//
//        String lastName = textInputLastName.getEditText().getText().toString().trim();
//
//        if (lastName.isEmpty()) {
//
//            textInputLastName.setError("str_fieldempty");
//            return false;
//        } else if (lastName.length() > 100) {
//
//            textInputLastName.setError("LastName too Long");
//            return false;
//        } else {
//
//            textInputLastName.setError(null);
//            textInputLastName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateMiddlename() {
//
//        String middleName = textInputMiddleName.getEditText().getText().toString().trim();
//
//        if (middleName.isEmpty()) {
//
//            textInputMiddleName.setError("str_fieldempty");
//            return false;
//        } else if (middleName.length() > 100) {
//
//            textInputMiddleName.setError("MiddleName too Long");
//            return false;
//        } else {
//
//            textInputMiddleName.setError(null);
//            textInputMiddleName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validatePassowrd() {
//
//        String password = textInputPassword.getEditText().getText().toString().trim();
//
//        if (password.isEmpty()) {
//
//            textInputPassword.setError("str_fieldempty");
//            return false;
//        } else if (password.length() > 100) {
//
//            textInputPassword.setError("Password too Long");
//            return false;
//        } else {
//
//            textInputPassword.setError(null);
//            textInputPassword.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateCofirmPassword() {
//
//        String confirmPassword = textInputConfirmPassword.getEditText().getText().toString().trim();
//
//        if (confirmPassword.isEmpty()) {
//
//            textInputConfirmPassword.setError("str_fieldempty");
//            return false;
//        } else if (confirmPassword.length() > 100) {
//
//            textInputConfirmPassword.setError("Confirm Password too Long");
//            return false;
//        } else {
//
//            textInputConfirmPassword.setError(null);
//            textInputConfirmPassword.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateMobileNumber() {
//
//        String mobileNumber = textInputMobileNumber.getEditText().getText().toString().trim();
//
//        if (mobileNumber.isEmpty()) {
//
//            textInputMobileNumber.setError("str_fieldempty");
//            return false;
//        } else {
//
//            textInputMobileNumber.setError(null);
//            textInputMobileNumber.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//    private boolean validateAddress() {
//
//        String address = textInputAddress.getEditText().getText().toString().trim();
//
//        if (address.isEmpty()) {
//
//            textInputAddress.setError("str_fieldempty");
//            return false;
//        } else if (address.length() > 100) {
//
//            textInputAddress.setError("Address too Long");
//            return false;
//        } else {
//
//            textInputAddress.setError(null);
//            textInputAddress.setErrorEnabled(false);
//            return true;
//        }
//
//    }
//
//
//    private boolean validateVehicleNumber() {
//
//        String vehicleNumber = textInputVehicleNumber.getEditText().getText().toString().trim();
//
//        if (vehicleNumber.isEmpty()) {
//
//            textInputVehicleNumber.setError("str_fieldempty");
//            return false;
//        } else if (vehicleNumber.length() > 10) {
//
//            textInputVehicleNumber.setError("VehicleNumber too Long");
//            return false;
//        } else {
//
//            textInputVehicleNumber.setError(null);
//            textInputVehicleNumber.setErrorEnabled(false);
//            return true;
//        }
//
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                stopService(AdminHomeActivity.this);
                SharedPrefsData.putBool(ctx, Common.isLogin, false);
                SharedPrefsData.putBool(ctx, Common.isAdmin, false);
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }


    public void register() {
        mdilogue.show();
        JsonObject object = LoginObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.registerDriver(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResRegister>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();

                        Toast.makeText(ctx, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResRegister resUserData) {
                        mdilogue.cancel();
                        if(resUserData.getIsSuccess())
                        {
                            Toast.makeText(AdminHomeActivity.this, resUserData.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AdminHomeActivity.this, resUserData.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private JsonObject LoginObject() {
        ReqRegister requestModel = new ReqRegister();
        requestModel.setId(0);
        requestModel.setUserId("");
        requestModel.setFirstName(txt_firstName.getText().toString().trim());
        requestModel.setMiddleName(txt_middlename.getText().toString().trim());
        requestModel.setLastName(txt_lastname.getText().toString().trim());
        requestModel.setContactNumber(txt_mobilNumber.getText().toString().trim());
        requestModel.setEmail(txt_email.getText().toString().trim());
        requestModel.setUserName(txt_username.getText().toString().trim());
        requestModel.setPassword(txt_password.getText().toString().trim());
        requestModel.setConfirmPassword(txt_confirmpassword.getText().toString().trim());
        requestModel.setVehicalNumber(txt_vechilnumber.getText().toString());
        requestModel.setRoleId(3);
        requestModel.setManagerId(SharedPrefsData.getUserDetails(ctx).getUserInfos().getId());
        requestModel.setVillageId(villageID);
        requestModel.setTrips(10);
        requestModel.setIsActive(true);
        requestModel.setCreatedByUserId(1);
        requestModel.setCreatedDate("2019-10-02T15:02:26.5224539+05:30");
        requestModel.setUpdatedDate("2019-10-02T15:02:26.5224539+05:30");
        requestModel.setUpdatedByUserId(1);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }


    public void GetDists() {
        JsonObject object = distObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getDisticts(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ResDist>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                       /* Toast.makeText(ctx, ctx.getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());*/
                    }

                    @Override
                    public void onNext(List<ResDist> dists) {
                        Log.d(TAG, "----- analysis --->> GetDists()-->>> onNext-->> Token =" + dists.size());


                        mDistAdapter = new DistAdapter(AdminHomeActivity.this, R.layout.spinner_item, dists);
                        mDistAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        distSpinner.setAdapter(mDistAdapter);
                    }
                });
    }

    private JsonObject distObject() {
        ReqDist requestModel = new ReqDist();
        requestModel.setStateIdsIds("1");
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public void GetDevisons(String divID) {
        mdilogue.show();
        JsonObject object = DivisionObject(divID);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getDevisions(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ResDivision>>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {

                       /* Toast.makeText(ctx, ctx.getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());*/
                    }

                    @Override
                    public void onNext(List<ResDivision> dists) {
                        Log.d(TAG, "----- analysis --->> GetDevisons()-->>> onNext-->> Token =" + dists.size());


                        mDivisonAdapternew = new DivisionAdapter(AdminHomeActivity.this, R.layout.spinner_item, dists);
                        mDivisonAdapternew.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        divisionSpinner.setAdapter(mDivisonAdapternew);


                    }
                });
    }

    private JsonObject DivisionObject(String divID) {
        ReqDevision requestModel = new ReqDevision();
        requestModel.setDistrictIds(divID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public void GetMandals(String Mandal) {

        JsonObject object = MandalsObject(Mandal);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getMandals(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ResMandal>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                       /* Toast.makeText(ctx, ctx.getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());*/
                    }

                    @Override
                    public void onNext(List<ResMandal> dists) {
                        Log.d(TAG, "----- analysis --->> GetDevisons()-->>> onNext-->> Token =" + dists.size());

                        mMandalAdapter = new MandalAdapter(AdminHomeActivity.this, R.layout.spinner_item, dists);
                        mMandalAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        mandalSpinner.setAdapter(mMandalAdapter);


                    }
                });
    }

    private JsonObject MandalsObject(String Mandal) {
        Reqmandal requestModel = new Reqmandal();
        requestModel.setDivisionIds(Mandal);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    public void GetVillages(String mandalid) {

        JsonObject object = VillageObject(mandalid);
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getVillages(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ResVillage>>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {

                       /* Toast.makeText(ctx, ctx.getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());*/
                    }

                    @Override
                    public void onNext(List<ResVillage> villages) {
                        Log.d(TAG, "----- analysis --->> Getvillages()-->>> onNext-->> Token =" + villages.size());

                        mVillageAdapter = new VillageAdapter(AdminHomeActivity.this, R.layout.spinner_item, villages);
                        mVillageAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        villageSpinner.setAdapter(mVillageAdapter);

                    }
                });
    }

    private JsonObject VillageObject(String mandalID) {
        ReqVillage requestModel = new ReqVillage();
        requestModel.setMandalIds(mandalID);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
