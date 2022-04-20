package in.calibrage.wsm.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import in.calibrage.wsm.R;
import in.calibrage.wsm.common.Common;
import in.calibrage.wsm.localData.SharedPrefsData;
import in.calibrage.wsm.model.ReqLogin;
import in.calibrage.wsm.model.ResUserData;
import in.calibrage.wsm.service.ApiService;
import in.calibrage.wsm.service.ServiceFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdminLoginActivity extends BaseActivity {

    private static final String TAG = AdminLoginActivity.class.getSimpleName();
    private Subscription mSubscription;
    private SpotsDialog mdilogue;
    private Context ctx;

    private EditText emailEditText;
    private EditText passEditText;
    private Button signInBtn;
    private Button driverLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        ctx = this;
        emailEditText = findViewById(R.id.username);
        passEditText = findViewById(R.id.password);
        signInBtn = findViewById(R.id.button);
        driverLoginBtn = findViewById(R.id.driverLoginBtn);

        mdilogue = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();

        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                if (!isValidPassword(email)) {
                    //Set error message for email field
                    emailEditText.setError("eror_username_empty");
                }

                final String pass = passEditText.getText().toString();
                if (!isValidPassword(pass)) {
                    //Set error message for password field
                    passEditText.setError("strPpassword_empty");
                }

                if (isValidPassword(email) && isValidPassword(pass)) {
                    Log.d(TAG, "----- analysis --->> Login()");
                    if (isOnline()) {


                        Login();

                    } else
                        Toast.makeText(ctx, "error_net", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 3) {
            return true;
        }
        return false;
    }

    public void Login() {
        mdilogue.show();
        JsonObject object = LoginObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.postLogin(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResUserData>() {
                    @Override
                    public void onCompleted() {
                        mdilogue.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mdilogue.cancel();
                        Toast.makeText(ctx, ctx.getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "----- analysis --->> Login()-->>> onError =" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResUserData resUserData) {
                        Log.d(TAG, "----- analysis --->> Login()-->>> onNext-->> Token =" + resUserData.getAccessToken());
                        mdilogue.cancel();

                        /*
                         * check he is AE or Not from Our end
                         *
                         * */
                        // if (resUserData.getUserInfos().getRoleId() == 1) {
                        if (resUserData.getUserInfos().getRoleId()== 2) {
                            SharedPrefsData.putBool(ctx, Common.isAdmin, true);
                            SharedPrefsData.saveUserDetails(ctx, resUserData);
                            Intent intent = new Intent(ctx, AdminHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ctx, ctx.getResources().getString(R.string.str_loginwithmanageraccount), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private JsonObject LoginObject() {
        ReqLogin requestModel = new ReqLogin();
        requestModel.setUserName(emailEditText.getText().toString().trim());
        requestModel.setPassword(passEditText.getText().toString().trim());
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }
}
