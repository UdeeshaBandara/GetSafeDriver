package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.SplashScreen;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Profile extends GetSafeDriverBase {


    ImageView logout;
    TextView name, tel, acc_type;
    GetSafeDriverServices getSafeDriverServices;
    Dialog dialog;
    TinyDB tinyDB;
    RecyclerView recycler_rfId;
    JSONArray passengerList;
    TextView msg;
    String id;
    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        passengerList = new JSONArray();
        logout = findViewById(R.id.logout);
        tel = findViewById(R.id.tel);
        name = findViewById(R.id.name);
        acc_type = findViewById(R.id.acc_type);
        tinyDB = new TinyDB(getApplicationContext());
        getSafeDriverServices = new GetSafeDriverServices();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        recycler_rfId = findViewById(R.id.recycler_rfId);
        msg = findViewById(R.id.msg);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(dialog, "Are sure you want to logout?", 3);
            }
        });

        recycler_rfId.setAdapter(new StudentAdapter());
        recycler_rfId.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//
        getDriverDetails();
        getAllPassengers();

    }

    class StudentItemHolder extends RecyclerView.ViewHolder {
        CardView rlt_transport_services;
        TextView student_school, parent_name, passenger_name;
        EditText rfid;
        Button btn_rfid;
        FoldingCell fc;
        ImageView close;

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
            rlt_transport_services = itemView.findViewById(R.id.rlt_transport_services);
            student_school = itemView.findViewById(R.id.student_school);
            passenger_name = itemView.findViewById(R.id.passenger_name);
            parent_name = itemView.findViewById(R.id.parent_name);
            fc = itemView.findViewById(R.id.folding_cell);
            rfid = itemView.findViewById(R.id.rfid);
            close = itemView.findViewById(R.id.close);
            btn_rfid = itemView.findViewById(R.id.btn_rfid);
        }
    }

    class StudentAdapter extends RecyclerView.Adapter<StudentItemHolder> {

        @NonNull
        @Override
        public StudentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_student_rfid, parent, false);
            return new StudentItemHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull StudentItemHolder holder, int position) {

            try {
                if (tinyDB.getBoolean("isStaffDriver")) {

                    holder.student_school.setText(passengerList.getJSONObject(position).getString("email"));

                } else {

                    String parent = passengerList.getJSONObject(position).getJSONObject("user").getString("name");
                    holder.parent_name.setText("Parent : " + parent);

                    holder.student_school.setText(passengerList.getJSONObject(position).getString("school_name"));

                }
                id = passengerList.getJSONObject(position).getString("id");
                holder.passenger_name.setText(passengerList.getJSONObject(position).getString("name"));

                holder.fc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getCurrentFocus();

                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        holder.fc.toggle(false);
                    }
                });
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view = getCurrentFocus();

                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        holder.fc.toggle(false);
                    }
                });
                holder.btn_rfid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        saveRFID(holder.rfid.getText().toString());


                    }
                });
//                holder.rlt_transport_services.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            Intent intent = new Intent(getApplicationContext(), Conversation.class);
//                            intent.putExtra("name", passengerList.getJSONObject(position).getString("name"));
//                            if (tinyDB.getBoolean("isStaffDriver")) {
//                                intent.putExtra("id", passengerList.getJSONObject(position).getString("id"));
//
//
//                            } else {
//                                intent.putExtra("id", passengerList.getJSONObject(position).getJSONObject("user").getString("id"));
//                                intent.putExtra("child_id", passengerList.getJSONObject(position).getString("id"));
//                            }
//                            startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            if (passengerList != null)
                return passengerList.length();
            else
                return 0;

        }


    }

    private void getAllPassengers() {


        HashMap<String, String> tempParam = new HashMap<>();

showHUD();
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.GET_ALL_PASSENGERS), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                hideHUD();
                try {
                    Log.e("passenger", result + "");
                    if (result.getBoolean("status")) {

                        passengerList = result.getJSONArray("model");

                        if (passengerList.length() != 0) {
                            Log.e("passengers ", "have");
                            msg.setVisibility(View.GONE);
                            recycler_rfId.setVisibility(View.VISIBLE);

                            recycler_rfId.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.e("passengers ", "dont have");
                            msg.setVisibility(View.VISIBLE);
                            recycler_rfId.setVisibility(View.GONE);
                        }

                    } else {

                        passengerList = null;
                    }


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void getDriverDetails() {


        HashMap<String, String> tempParam = new HashMap<>();

        showHUD();
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.VALIDATE_TOKEN), 1, tinyDB.getString("token"), new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                hideHUD();
                try {

                    Log.e("details", result + "");


                    if (result.getBoolean("logged-in-status")) {
                        name.setText(result.getJSONObject("driver").getString("name"));
                        tel.setText(result.getJSONObject("driver").getString("phone_no"));
                        acc_type.setText(tinyDB.getBoolean("isStaffDriver") ? "Staff Driver" : "Student Driver");

                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

    }

    private void saveRFID(String rfid) {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", id);
        tempParam.put("rfid", rfid);
        showHUD();
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.SAVE_RFID), 2, tinyDB.getString("token"), new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                hideHUD();
                try {

                    if (result.getBoolean("status")) {

                        showToast(dialog, "RFID saved successfully", 2);

                    } else {
                        showToast(dialog, "Failed to save RFID", 0);

                    }


                } catch (Exception e) {
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