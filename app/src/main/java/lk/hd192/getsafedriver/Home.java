package lk.hd192.getsafedriver;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Home extends GetSafeDriverBase {

    //    RecyclerView recyclerHome;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    TinyDB tinyDB;
    ImageView settings;

    Dialog dialog;
    GetSafeDriverServices getSafeDriverServices;

    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSafeDriverServices = new GetSafeDriverServices();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_te));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        tinyDB = new TinyDB(getApplicationContext());

        settings=findViewById(R.id.settings);

//        Log.e("is staff", tinyDB.getBoolean("isStaffDriver") + "");
//        tinyDB.putBoolean("isStaffDriver",true);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


//        rec
//        Log.e("child ", tinyDB.getString("token"));
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });
        homeRoutes();


    }

    private void homeRoutes() {


        findViewById(R.id.card_absence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverValidate(0);
            }
        });
        findViewById(R.id.card_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewRequest.class));
            }
        });
        findViewById(R.id.card_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Map.class));
                driverValidate(1);
            }
        });
        findViewById(R.id.card_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Message.class));
                driverValidate(2);
            }
        });
        findViewById(R.id.card_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Payment.class));
                driverValidate(3);
            }
        });

    }

    private void driverValidate(int pos) {

        HashMap<String, String> tempParam = new HashMap<>();

        showHUD();
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.GET_ALL_PASSENGERS), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("passenger", result + "");
                    if (result.getBoolean("status")) {

//                        passengerList = result.getJSONArray("model");
                        if (result.getJSONArray("model").length() != 0) {

                            switch (pos) {
                                case 0:
                                    startActivity(new Intent(getApplicationContext(), Absence.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(getApplicationContext(), Map.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(getApplicationContext(), Message.class));
                                    break;
                                case 3:
                                    startActivity(new Intent(getApplicationContext(), Payment.class));
                                    break;


                            }

                        } else {

                            showToast(dialog, "You have no passengers", 0);

                        }  hideHUD();

                    } else {
                        hideHUD();
                        showToast(dialog, "Something went wrong", 0);
                    }


                } catch (Exception e) {
                    hideHUD();
                    e.printStackTrace();
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