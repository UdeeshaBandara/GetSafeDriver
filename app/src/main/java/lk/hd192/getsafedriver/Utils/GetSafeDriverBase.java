package lk.hd192.getsafedriver.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lk.hd192.getsafedriver.R;

class GetSafeDriverBase extends AppCompatActivity {




    public static int device_width, device_height;
    public TinyDB tinyDB;
    public static String PICKUP_LAT, PICKUP_LOG, DROP_LAT, DROP_LOG, PICKUP_LOCATION, DROP_LOCATION, LOC_ADDRESS;
    public int dropNPickAddressDistinguish;
    public static Double pickLat;

    public static Double pickLng;
    public static boolean isEnable;
    public LocationManager locationManager;
    public static boolean MAP_SELECTED = false;

    public static String pickAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tinyDB = new TinyDB(getApplicationContext());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        device_width = displayMetrics.widthPixels;
        device_height = displayMetrics.heightPixels;

    }

    public void isDeviceLocationTurnedOn(final Dialog dialog) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))

            isEnable = true;

        else
            isEnable = false;

        if (!isEnable) {

            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);


            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 550);
            dialog.setTitle(null);
            dialog.setContentView(R.layout.toast_layout_location);

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            TextView msgToShow = dialog.findViewById(R.id.toast_message);
            Button btnOk = dialog.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settings);
                }
            });
            msgToShow.setText("Please enable location service");

            dialog.show();


        }

    }





    public void showWarningToast(final Dialog dialog, String msg, int type) {


        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 550);
        dialog.setTitle(null);
        switch (type) {
            case 0:
                dialog.setContentView(R.layout.toast_layout_warning);
                break;
            case 1:
                dialog.setContentView(R.layout.toast_layout_location);
                break;


        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView msgToShow = dialog.findViewById(R.id.toast_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        msgToShow.setText(msg);

        dialog.show();
    }

}
