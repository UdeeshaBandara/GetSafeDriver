package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import lk.hd192.getsafedriver.Utils.AppBarStateChangeListener;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Absence extends AppCompatActivity {

    RecyclerView recycler_absence;
    RelativeLayout status_bar;
    AppBarLayout appBarLayout;
    LinearLayout lnrTop;
    TextView txt_current_date;
    GetSafeDriverServices getSafeDriverServices;
    CalendarView filter_calendar;
    Dialog dialog;
    TinyDB tinyDB;
    JSONArray absentReview;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);
        tinyDB = new TinyDB(getApplicationContext());
        absentReview = new JSONArray();
        recycler_absence = findViewById(R.id.recycler_absence);
        appBarLayout = findViewById(R.id.appBar);
        lnrTop = findViewById(R.id.lnrTop);
        status_bar = findViewById(R.id.status_bar);
        getSafeDriverServices = new GetSafeDriverServices();
        txt_current_date = findViewById(R.id.txt_current_date);
        filter_calendar = findViewById(R.id.filter_calendar);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        getAbsentList();

        recycler_absence.setAdapter(new AbsenceUserAdapter());
        recycler_absence.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recycler_absence.setNestedScrollingEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {

                if (state.name().equals("COLLAPSED")) {
                    lnrTop.setBackground(getDrawable(R.drawable.bg_tool_bar_collapsed));
                    status_bar.setBackground(getDrawable(R.drawable.bg_action_bar_collapse));
                } else {
                    lnrTop.setBackground(getDrawable(R.drawable.bg_tool_bar_expanded));
                    status_bar.setBackground(getDrawable(R.drawable.bg_action_bar));
                }


            }
        });
        filter_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                int sMonth = (month + 1);

                if (sMonth < 10 && dayOfMonth < 10) {
                    selectedDate = year + "-0" + sMonth + "-0" + dayOfMonth;

                } else if (sMonth < 10 && dayOfMonth >= 10) {
                    selectedDate = year + "-0" + sMonth + "-" + dayOfMonth;

                } else if (sMonth >= 10 && dayOfMonth < 10) {
                    selectedDate = year + "-" + sMonth + "-0" + dayOfMonth;

                } else {
                    selectedDate = year + "-" + sMonth + "-" + dayOfMonth;
                }

                txt_current_date.setText("Selected Date : " + selectedDate);
                getAbsentList();


            }
        });
    }

    class AbsenceUserViewHolder extends RecyclerView.ViewHolder {
        TextView passenger_name, passenger_school;
        ImageView mark;

        public AbsenceUserViewHolder(@NonNull View itemView) {
            super(itemView);
            passenger_school = itemView.findViewById(R.id.passenger_school);
            passenger_name = itemView.findViewById(R.id.passenger_name);
            mark = itemView.findViewById(R.id.mark);
        }
    }

    class AbsenceUserAdapter extends RecyclerView.Adapter<AbsenceUserViewHolder> {

        @NonNull
        @Override
        public AbsenceUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_absence_user, parent, false);
            return new AbsenceUserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AbsenceUserViewHolder holder, int position) {
            try {

                if (!absentReview.getJSONObject(position).getBoolean("absent"))
                    holder.mark.setImageDrawable(getResources().getDrawable(R.drawable.icon_check));
                else
                    holder.mark.setImageDrawable(getResources().getDrawable(R.drawable.icon_false));


                holder.passenger_name.setText(absentReview.getJSONObject(position).getString("name"));
                holder.passenger_school.setText(absentReview.getJSONObject(position).getString("phone_no"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return absentReview.length();
        }
    }

    private void getAbsentList() {


        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.ABSENT_REVIEW) + "?date=" + selectedDate, 1, tinyDB.getString("token"), new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    Log.e("result", result + "");

                    if (result.getBoolean("status")) {

                        absentReview = result.getJSONArray("model");
                        recycler_absence.getAdapter().notifyDataSetChanged();


                    }
//                        showToast(dialog, "Something went wrong. Please try again", 0);


                } catch (Exception e) {

                }

            }
        });

    }


}