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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
    TextView txt_search;
    JSONArray passengerList, originalPassengerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSafeDriverServices = new GetSafeDriverServices();

        recyclerStudents = findViewById(R.id.recycler_students);
        txt_search = findViewById(R.id.txt_search);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        passengerList = new JSONArray();
        originalPassengerList = new JSONArray();
        recyclerStudents.setAdapter(new StudentAdapter());
        recyclerStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txt_search.getText().toString().equals("")) {
                    passengerList = originalPassengerList;
                    recyclerStudents.getAdapter().notifyDataSetChanged();
                } else {

                    StudentAdapter homeAdapter = new StudentAdapter();
                    homeAdapter.getFilter().filter(s);
                }

            }
        });


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

    class StudentAdapter extends RecyclerView.Adapter<StudentItemHolder> implements Filterable {

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
                if (tinyDB.getBoolean("isStaffDriver")) {

                    holder.student_school.setText(passengerList.getJSONObject(position).getString("email"));
                } else {
                    String parent = passengerList.getJSONObject(position).getJSONObject("user").getString("name");
                    holder.parent_name.setText("Parent : " + parent);

                    holder.student_school.setText(passengerList.getJSONObject(position).getString("school_name"));

                }
                holder.passenger_name.setText(passengerList.getJSONObject(position).getString("name"));
                holder.rlt_transport_services.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), Conversation.class);
                            intent.putExtra("name", passengerList.getJSONObject(position).getString("name"));
                            if (tinyDB.getBoolean("isStaffDriver")) {
                                intent.putExtra("id", passengerList.getJSONObject(position).getString("id"));


                            } else {
                                intent.putExtra("id", passengerList.getJSONObject(position).getJSONObject("user").getString("id"));
                                intent.putExtra("child_id", passengerList.getJSONObject(position).getString("id"));
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
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

        @Override
        public Filter getFilter() {

            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                passengerList = new JSONArray();
                int put = 0;

                try {
                    for (int i = 0; i < originalPassengerList.length(); i++) {
                        if (originalPassengerList.getJSONObject(i).getString("name").toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                originalPassengerList.getJSONObject(i).getJSONObject("user").getString("name").toLowerCase().contains(constraint.toString().toLowerCase())) {

                            passengerList.put(put++, originalPassengerList.getJSONObject(i));
                        }

                    }

                } catch (Exception e) {

                }


                return null;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                recyclerStudents.getAdapter().notifyDataSetChanged();
            }
        };
    }

    private void getAllPassengersToMessage() {


        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.GET_ALL_PASSENGERS), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("passenger", result + "");
                    if (result.getBoolean("status")) {
                        passengerList = result.getJSONArray("model");
                        originalPassengerList = result.getJSONArray("model");
                        recyclerStudents.getAdapter().notifyDataSetChanged();

                    } else {
                        originalPassengerList = null;
                        passengerList = null;
                    }


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }


}