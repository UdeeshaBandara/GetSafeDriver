package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.TinyDB;

public class Home extends GetSafeDriverBase {

    RecyclerView recyclerHome;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        tinyDB= new TinyDB(getApplicationContext());

        recyclerHome = findViewById(R.id.recycler_home);

        recyclerHome.setAdapter(new HomeItemAdapter());
        recyclerHome.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//
//        recyclerHome.setLayoutManager(staggeredGridLayoutManager);
//        recyclerHome.getAdapter().notifyDataSetChanged();

        tinyDB.putBoolean("isSchoolDriver",true);
        tinyDB.putString("token","2|J077CxJEhXRM3PzH72ezwVHVGCW6Eg3ki5VG9YrW");

    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cell_location;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            cell_location = itemView.findViewById(R.id.cell_location);
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cellMsg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cellMsg = itemView.findViewById(R.id.cell_msg);
        }
    }

    class AbsenceViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout cell_absence;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);
            cell_absence = itemView.findViewById(R.id.cell_absence);
        }
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cell_payment;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            cell_payment = itemView.findViewById(R.id.cell_payment);
        }
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cell_attendance;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            cell_attendance = itemView.findViewById(R.id.cell_attendance);
        }
    }

    class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View locationView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_location, parent, false);
            View messageView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_message, parent, false);
            View absenceView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_absence, parent, false);
            View paymentView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_payment, parent, false);
            View attendanceView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_attendance, parent, false);
            switch (viewType) {

                case 1:
                    return new LocationViewHolder(locationView);
                case 3:
                    return new MessageViewHolder(messageView);
                case 0:
                    return new AbsenceViewHolder(absenceView);
                case 2:
                    return new PaymentViewHolder(paymentView);
                case 4:
                    return new AttendanceViewHolder(attendanceView);

            }
            return null;

        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 0;
            else if (position == 1)
                return 1;
            else if (position == 2)
                return 2;
            else if (position == 3)
                return 3;
            else
                return 4;

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            try {
                switch (holder.getItemViewType()) {

                    case 0:
                        AbsenceViewHolder absenceViewHolder = (AbsenceViewHolder) holder;
                        absenceViewHolder.cell_absence.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), Absence.class));

                            }
                        });
                        break;
                    case 1:
                        LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
                        locationViewHolder.cell_location.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), Map.class));

                            }
                        });
                        break;
                    case 2:
                        PaymentViewHolder paymentViewHolder = (PaymentViewHolder) holder;
                        paymentViewHolder.cell_payment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), Payment.class));

                            }
                        });
                        break;
                    case 3:
                        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                        messageViewHolder.cellMsg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), Message.class));

                            }
                        });

                        break;
                }


            } catch (Exception e) {

            }

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}