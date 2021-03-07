package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class NewRequest extends GetSafeDriverBase {

    GetSafeDriverServices getSafeDriverServices;
    TinyDB tinyDB;
    View popupView;
    Dialog dialog;
    JSONArray requestList;
    RecyclerView recycler_students;
    Button mConfirm, cancel;
    TextView txt_name, txt_pickup, txt_drop, txt_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getApplicationContext());
        requestList = new JSONArray();

        recycler_students = findViewById(R.id.recycler_students);
        recycler_students.setAdapter(new StudentAdapter());
        recycler_students.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//
        getAllRequestForDriver();
    }

    class StudentItemHolder extends RecyclerView.ViewHolder {
        CardView rlt_transport_services;

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
            rlt_transport_services = itemView.findViewById(R.id.rlt_transport_services);
        }
    }

    class StudentAdapter extends RecyclerView.Adapter<StudentItemHolder> {

        @NonNull
        @Override
        public StudentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_student, parent, false);
            return new StudentItemHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull StudentItemHolder holder, int position) {

            holder.rlt_transport_services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(getApplicationContext(), Conversation.class));
                    onCreateMapPopup(v);
                }
            });

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    public static void dimBehind(PopupWindow popupWindow) {

        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }

    public void onCreateMapPopup(View view) {


        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.request_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, GetSafeDriverBase.device_width - 150, WindowManager.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dimBehind(popupWindow);

        mConfirm = popupView.findViewById(R.id.btn_accept);
        cancel = popupView.findViewById(R.id.btn_cancel);
        txt_number = popupView.findViewById(R.id.txt_number);
        txt_drop = popupView.findViewById(R.id.txt_drop);
        txt_pickup = popupView.findViewById(R.id.txt_pickup);
        txt_name = popupView.findViewById(R.id.txt_name);


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  popupWindow.dismiss();
                return true;
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();

                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelRequest();

                popupWindow.dismiss();

            }
        });

    }

    private void getAllRequestForDriver() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.VIEW_ALL_REQUEST), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    requestList = new JSONArray();
                    Log.e("response", result + "");

                    if (result.getBoolean("status")) {

                        requestList = result.getJSONArray("requests");
                        recycler_students.getAdapter().notifyDataSetChanged();

                    } else
                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });

    }

    private void getRequestDetails() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.VIEW_ONE_REQUEST), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    requestList = new JSONArray();
                    Log.e("response", result + "");

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });

    }

    private void acceptRequest() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.ACCEPT_REQUEST), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    requestList = new JSONArray();
                    Log.e("response", result + "");

                    if (result.getBoolean("status")) {
                        showToast(dialog, "Request Confirmed", 2);

                    } else
                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });

    }

    private void cancelRequest() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.DECLINE_REQUEST), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    requestList = new JSONArray();
                    Log.e("response", result + "");

                    if (result.getBoolean("status")) {
                        showToast(dialog, "Request Canceled", 2);


                    } else
                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });

    }
}