package lk.hd192.getsafedriver.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Home;
import lk.hd192.getsafedriver.Login;
import lk.hd192.getsafedriver.R;

public class SplashScreen extends GetSafeDriverBase {
    GetSafeDriverServices getSafeDriverServices;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getApplicationContext());

        tinyDB.putBoolean("isNewUser",false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e("islogged",tinyDB.getBoolean("isLogged")+"");


        if (tinyDB.getBoolean("isLogged"))
            validateToken();


        else {

            startActivity(new Intent(SplashScreen.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
            finish();
        }


    }

    private void validateToken() {


        HashMap<String, String> tempParam = new HashMap<>();




        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.VALIDATE_TOKEN), 1,tinyDB.getString("token"), new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("splash",result+"");

                    if (result.getBoolean("logged-in-status")){


                        startActivity(new Intent(SplashScreen.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));

                    tinyDB.putString("driver_id",result.getJSONObject("driver").getString("id"));}
                    else {

                        startActivity(new Intent(SplashScreen.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
                    }
                    Log.e("token",tinyDB.getString("token"));

                    finish();


                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

    }
}
