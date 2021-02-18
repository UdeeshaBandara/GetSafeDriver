package lk.hd192.getsafedriver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }
}