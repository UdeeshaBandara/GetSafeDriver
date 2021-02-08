package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


        recyclerStudents=findViewById(R.id.recycler_students);

        recyclerStudents.setAdapter(new StudentAdapter());
        recyclerStudents.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
//

    }
    class StudentItemHolder extends RecyclerView.ViewHolder{

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    class StudentAdapter extends RecyclerView.Adapter<StudentItemHolder>{

        @NonNull
        @Override
        public StudentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=  LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_student, parent, false);
            return new StudentItemHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull StudentItemHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}