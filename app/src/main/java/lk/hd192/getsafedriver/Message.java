package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;

public class Message extends GetSafeDriverBase {

    RecyclerView recyclerStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        recyclerStudents = findViewById(R.id.recycler_students);

        recyclerStudents.setAdapter(new StudentAdapter());
        recyclerStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
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
                    startActivity(new Intent(getApplicationContext(), Conversation.class));
                }
            });

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}