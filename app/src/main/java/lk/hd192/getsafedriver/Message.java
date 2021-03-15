package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Message extends GetSafeDriverBase {

    RecyclerView recyclerStudents;
    GetSafeDriverServices getSafeDriverServices;
    Dialog dialog;
    JSONArray passengerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSafeDriverServices = new GetSafeDriverServices();

        recyclerStudents = findViewById(R.id.recycler_students);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        passengerList = new JSONArray();
        recyclerStudents.setAdapter(new StudentAdapter());
        recyclerStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
        getAllPassengersToMessage();
    }

    class StudentItemHolder extends RecyclerView.ViewHolder {
        CardView rlt_transport_services;
        TextView student_school, parent_name, passenger_name;

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
            rlt_transport_services = itemView.findViewById(R.id.rlt_transport_services);
            student_school = itemView.findViewById(R.id.student_school);
            passenger_name = itemView.findViewById(R.id.passenger_name);
            parent_name = itemView.findViewById(R.id.parent_name);
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

            try {
//                if (tinyDB.getBoolean("isStaffDriver")) {
//
//
//                    holder.student_school.setText(passengerList.getJSONObject(position).getJSONObject("user").getString("email"));
//                    holder.passenger_name.setText(passengerList.getJSONObject(position).getJSONObject("user").getString("name"));
//                } else {
//                    holder.parent_name.setText("Parent : " + passengerList.getJSONObject(position).getJSONObject("user").getString("name"));
//                    holder.student_school.setText(passengerList.getJSONObject(position).getJSONObject("child").getString("school_name"));
//                    holder.passenger_name.setText(passengerList.getJSONObject(position).getJSONObject("child").getString("name"));
//
//                }
                holder.rlt_transport_services.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Conversation.class));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return passengerList.length();
        }
    }

    private void getAllPassengersToMessage() {


        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.GET_ALL_PASSENGERS), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {
                        passengerList = result.getJSONArray("model");
                        recyclerStudents.getAdapter().notifyDataSetChanged();

                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }


}