package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class OTP extends GetSafeDriverBase {
    EditText confirmOTP_1, confirmOTP_2, confirmOTP_3, confirmOTP_4;
    GetSafeDriverServices getSafeDriverServices;
    TinyDB tinyDB;
    Dialog dialog;
    TextView otp_heading;
    private FirebaseAuth mAuth;
    Button btn_otp_back;
    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        confirmOTP_1 = findViewById(R.id.otp_one);
        confirmOTP_2 = findViewById(R.id.otp_two);
        confirmOTP_3 = findViewById(R.id.otp_three);
        confirmOTP_4 = findViewById(R.id.otp_four);
        otp_heading = findViewById(R.id.otp_heading);
        btn_otp_back = findViewById(R.id.btn_otp_back);
        mAuth = FirebaseAuth.getInstance();
        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getApplicationContext());
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        btn_otp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirmOTP_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (confirmOTP_1.getText().toString().length() == 1) {
                    confirmOTP_2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otp_heading.setText("A verification code has been sent to your mobile \n" + tinyDB.getString("phone_no") + "");

        confirmOTP_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (confirmOTP_2.getText().toString().length() == 1) {
                    confirmOTP_3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        confirmOTP_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (confirmOTP_3.getText().toString().length() == 1) {
                    confirmOTP_4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        confirmOTP_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!TextUtils.isEmpty(confirmOTP_1.getText().toString()) & !TextUtils.isEmpty(confirmOTP_2.getText().toString()) & !TextUtils.isEmpty(confirmOTP_3.getText().toString()) & !TextUtils.isEmpty(confirmOTP_4.getText().toString())) {


                    View view = OTP.this.getCurrentFocus();

                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        findViewById(R.id.btn_otp_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateOTP();


            }
        });
    }

    private void validateOTP() {

        HashMap<String, String> tempParam = new HashMap<>();

        tempParam.put("otp", confirmOTP_1.getText().toString() + confirmOTP_2.getText().toString() + confirmOTP_3.getText().toString() + confirmOTP_4.getText().toString());

        Log.e("otp otken", tinyDB.getString("otp_token"));
        tempParam.put("otp-token", tinyDB.getString("otp_token"));

        showHUD();
//        showLoading();
        getSafeDriverServices.networkJsonRequestWithoutHeader(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_VALIDATE_OTP), 2, new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {
                hideHUD();
                try {
                    Log.e("response", result + "");

                    if (result.getBoolean("otp_token_validity")) {
                        tinyDB.putBoolean("isLogged", true);
//                        tinyDB.putBoolean("isStaffDriver", true);
                        tinyDB.putString("token", result.getString("access_token"));
//                        if (result.getString("type").equals("office-transport"))
//                            tinyDB.putBoolean("isStaffDriver", true);
                            tinyDB.putString("driver_id", result.getJSONObject("user").getString("id"));
//                        firebaseLogin();
                        getDeviceToken();


                    } else
                        showToast(dialog, "OTP validation failed", 0);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });


    }

    private void firebaseLogin() {

        mAuth.signInWithEmailAndPassword(tinyDB.getString("email"), tinyDB.getString("phone_no")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.e("firebase login", "success");

                } else {

                }
            }
        });


    }

    public void getDeviceToken() {


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            showToast(dialog, "Please try again", 0);


                            return;


                        } else {

                            updateUserFcmToken(task.getResult().getToken());


                        }

                    }
                });
    }

    private void updateUserFcmToken(String fcmToken) {
        HashMap<String, String> tempParam = new HashMap<>();

        tempParam.put("fcm_token", fcmToken);

        showHUD();
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.UPDATE_FCM), 2, tinyDB.getString("token"), new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    hideHUD();

                    if (result.getBoolean("saved_status")) {

                        tinyDB.putString("fcmToken", fcmToken);
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finishAffinity();

                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {
                    e.printStackTrace();
//                    hideHUD();

                }

            }
        });

    }

    public void showHUD() {


        if (hud.isShowing()) {
            hud.dismiss();
        }
        hud.show();
    }

    public void hideHUD() {
        if (hud.isShowing()) {
            hud.dismiss();
        }
    }

}