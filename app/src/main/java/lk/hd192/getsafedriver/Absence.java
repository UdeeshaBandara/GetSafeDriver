package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import lk.hd192.getsafedriver.Utils.AppBarStateChangeListener;

public class Absence extends AppCompatActivity {

    RecyclerView recycler_absence;
    RelativeLayout status_bar;
    AppBarLayout appBarLayout;
    LinearLayout lnrTop;
    TextView txt_current_date;
    CalendarView filter_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence);

        recycler_absence = findViewById(R.id.recycler_absence);
        appBarLayout = findViewById(R.id.appBar);
        lnrTop = findViewById(R.id.lnrTop);
        status_bar = findViewById(R.id.status_bar);
        txt_current_date = findViewById(R.id.txt_current_date);
        filter_calendar = findViewById(R.id.filter_calendar);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

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
                txt_current_date.setText("Selected Date"+dayOfMonth+"-"+month+"-"+year);
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
            if (position % 2 == 0)
                holder.mark.setImageDrawable(getResources().getDrawable(R.drawable.icon_check));
            else
                holder.mark.setImageDrawable(getResources().getDrawable(R.drawable.icon_false));
        }

        @Override
        public int getItemCount() {
            return 32;
        }
    }

}