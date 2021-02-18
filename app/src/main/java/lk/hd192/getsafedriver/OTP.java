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
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        confirmOTP_1 = findViewById(R.id.otp_one);
        confirmOTP_2 = findViewById(R.id.otp_two);
        confirmOTP_3 = findViewById(R.id.otp_three);
        confirmOTP_4 = findViewById(R.id.otp_four);
        mAuth = FirebaseAuth.getInstance();
        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getApplicationContext());
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

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

        tempParam.put("otp-token", "");


//        showLoading();
        getSafeDriverServices.networkJsonRequestWithoutHeader(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_VALIDATE_OTP), 2, new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {
//                hideLoading();
                try {
                    Log.e("response", result + "");

                    if (result.getBoolean("otp_token_validity")) {
                        tinyDB.putBoolean("isLogged", true);
                        tinyDB.putString("token", result.getString("access_token"));
                        firebaseLogin();
                        getDeviceToken();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finishAffinity();

                    } else
                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }
    private void firebaseLogin(){

        mAuth.signInWithEmailAndPassword( tinyDB.getString("email"), tinyDB.getString("phone_no")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.e("firebase login","success");

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
    private void updateUserFcmToken(String token) {
    }

}