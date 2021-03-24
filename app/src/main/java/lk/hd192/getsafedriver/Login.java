package lk.hd192.getsafedriver;


import android.app.Dialog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;


public class Login extends GetSafeDriverBase {

    EditText one, two, three, four, five, six, seven, eight, nine;
    ImageView img_head;
    Dialog dialog;
    RelativeLayout rlt;

    TinyDB tinyDB;

    GetSafeDriverServices getSafeDriverServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        one = findViewById(R.id.txt_number_one);
        two = findViewById(R.id.txt_number_two);
        three = findViewById(R.id.txt_number_three);
        four = findViewById(R.id.txt_number_four);
        five = findViewById(R.id.txt_number_five);
        six = findViewById(R.id.txt_number_six);
        seven = findViewById(R.id.txt_number_seven);
        eight = findViewById(R.id.txt_number_eight);
        nine = findViewById(R.id.txt_number_nine);
        img_head = findViewById(R.id.img_head);
        rlt = findViewById(R.id.rlt);

        tinyDB = new TinyDB(getApplicationContext());
        getSafeDriverServices = new GetSafeDriverServices();
        requestFocus();
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        findViewById(R.id.btn_login_next).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(one.getText().toString()) &
                        !TextUtils.isEmpty(two.getText().toString()) &
                        !TextUtils.isEmpty(three.getText().toString()) &
                        !TextUtils.isEmpty(four.getText().toString()) &
                        !TextUtils.isEmpty(five.getText().toString()) &
                        !TextUtils.isEmpty(six.getText().toString()) &
                        !TextUtils.isEmpty(seven.getText().toString()) &
                        !TextUtils.isEmpty(eight.getText().toString()) &
                        !TextUtils.isEmpty(nine.getText().toString())) {


                    driverSendOtp();


                } else {
                    YoYo.with(Techniques.Bounce)
                            .duration(2500)
                            .playOn(findViewById(R.id.lnr_number));
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
//                    captureImageCameraOrGallery();
                    getDeviceToken();

                }
            }
        });


        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }


    private void requestFocus() {


        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (one.getText().toString().length() == 1) {
                    two.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (two.getText().toString().length() == 1) {
                    three.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (three.getText().toString().length() == 1) {
                    four.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (four.getText().toString().length() == 1) {
                    five.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (five.getText().toString().length() == 1) {
                    six.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (six.getText().toString().length() == 1) {
                    seven.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        seven.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (seven.getText().toString().length() == 1) {
                    eight.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        eight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (eight.getText().toString().length() == 1) {
                    nine.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!TextUtils.isEmpty(one.getText().toString()) &
                        !TextUtils.isEmpty(two.getText().toString()) &
                        !TextUtils.isEmpty(three.getText().toString()) &
                        !TextUtils.isEmpty(four.getText().toString()) &
                        !TextUtils.isEmpty(five.getText().toString()) &
                        !TextUtils.isEmpty(six.getText().toString()) &
                        !TextUtils.isEmpty(seven.getText().toString()) &
                        !TextUtils.isEmpty(eight.getText().toString()) &
                        !TextUtils.isEmpty(nine.getText().toString())) {


                    View view = Login.this.getCurrentFocus();

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


    }

    private void driverSendOtp() {

        HashMap<String, String> tempParam = new HashMap<>();

        tempParam.put("phone", one.getText().toString() +
                two.getText().toString() +
                three.getText().toString() +
                four.getText().toString() +
                five.getText().toString() +
                six.getText().toString() +
                seven.getText().toString() +
                eight.getText().toString() +
                nine.getText().toString());

        tinyDB.putString("phone_no", one.getText().toString() +
                two.getText().toString() +
                three.getText().toString() +
                four.getText().toString() +
                five.getText().toString() +
                six.getText().toString() +
                seven.getText().toString() +
                eight.getText().toString() +
                nine.getText().toString());
        //    tempParam.put("fcm_token", token);


        getSafeDriverServices.networkJsonRequestWithoutHeader(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_OTP), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    Log.e("result", result + "");

                    if (result.getBoolean("otp_sent_status")) {

                        tinyDB.putString("phone_no", one.getText().toString() +
                                two.getText().toString() +
                                three.getText().toString() +
                                four.getText().toString() +
                                five.getText().toString() +
                                six.getText().toString() +
                                seven.getText().toString() +
                                eight.getText().toString() +
                                nine.getText().toString());

                        startActivity(new Intent(getApplicationContext(), OTP.class));

                    }else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {
                    e.printStackTrace();


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
                            Log.e("htok", task.getResult().getToken());

//                            updateUserFcmToken();


                        }

                    }
                });
    }

}