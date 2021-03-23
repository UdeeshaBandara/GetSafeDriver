package lk.hd192.getsafedriver;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.TinyDB;

public class Home extends GetSafeDriverBase {

    //    RecyclerView recyclerHome;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    TinyDB tinyDB;
    ImageView logout;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_te));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        tinyDB = new TinyDB(getApplicationContext());
        logout = findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(dialog,"Are sure you want to logout?",3);
            }
        });

//        recyclerHome = findViewById(R.id.recycler_home);

//        recyclerHome.setAdapter(new HomeItemAdapter());
//        recyclerHome.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//
//        recyclerHome.setLayoutManager(staggeredGridLayoutManager);
//        recyclerHome.getAdapter().notifyDataSetChanged();

        tinyDB.putBoolean("isStaffDriver", true);
        tinyDB.putString("token", "1|C8DrCVRP8a4G3NcFb5BdHvgZkQXrhmB39zY3ynxG");


        homeRoutes();


    }

    private void homeRoutes() {


        findViewById(R.id.card_absence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Absence.class));
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
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });
        findViewById(R.id.card_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Message.class));
            }
        });
        findViewById(R.id.card_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Payment.class));
            }
        });

    }
}