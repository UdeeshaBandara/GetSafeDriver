package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import lk.hd192.getsafedriver.Encryption.Encrypt;
import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.TinyDB;

public class Home extends GetSafeDriverBase {

//    RecyclerView recyclerHome;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        tinyDB= new TinyDB(getApplicationContext());

//        recyclerHome = findViewById(R.id.recycler_home);

//        recyclerHome.setAdapter(new HomeItemAdapter());
//        recyclerHome.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//
//        recyclerHome.setLayoutManager(staggeredGridLayoutManager);
//        recyclerHome.getAdapter().notifyDataSetChanged();

        tinyDB.putBoolean("isSchoolDriver",true);
        tinyDB.putString("token","2|vmt25I0AbcK18twgYHU98jIXzMMpU2dc77u1Fmde");


        homeRoutes();


    }
    private void homeRoutes(){


        findViewById(R.id.card_absence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Absence.class));
            }
        });  findViewById(R.id.card_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewRequest.class));
            }
        });  findViewById(R.id.card_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });  findViewById(R.id.card_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Message.class));
            }
        });  findViewById(R.id.card_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Payment.class));
            }
        });

    }
}