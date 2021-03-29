package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Payment extends GetSafeDriverBase {
    RecyclerView recycler_payment;
    TextView msg;
    GetSafeDriverServices getSafeDriverServices;
    TinyDB tinyDB;
    Dialog dialog;
    JSONArray paymentList;
//    String[] values = {"Dilshan Madurange", "Saman Perera", "Lakshan Silva", "Harshana Fernando", "Dilshan Madurange", "Saman Perera", "Lakshan Silva", "Harshana Fernando"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
        msg = findViewById(R.id.msg);
        recycler_payment = findViewById(R.id.recycler_payment);
        paymentList = new JSONArray();
        tinyDB = new TinyDB(getApplicationContext());
        getSafeDriverServices = new GetSafeDriverServices();

        recycler_payment.setAdapter(new PaymentAdapter());
        recycler_payment.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        getPaymentDetails();
    }

    class PaymentItemHolder extends RecyclerView.ViewHolder {
        TextView name, amount, date;

        public PaymentItemHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
        }
    }

    class PaymentAdapter extends RecyclerView.Adapter<PaymentItemHolder> {

        @NonNull
        @Override
        public PaymentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_payment, parent, false);
            return new PaymentItemHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull PaymentItemHolder holder, int position) {
            try {

                holder.amount.setText("LKR " + paymentList.getJSONObject(position).getString("total_fee"));
                holder.date.setText(paymentList.getJSONObject(position).getString("datetime").substring(0, 10));
                holder.name.setText(paymentList.getJSONObject(position).getJSONObject("paymentable").getString("name"));
//                holder.name.setText(paymentList.getJSONObject(position).getString("driver_fee"));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return paymentList.length();
        }
    }

    private void getPaymentDetails() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.PAYMENT), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {


                    Log.e("res", result + "");
                    if (result.getBoolean("status")) {

                        paymentList = result.getJSONArray("model");
                        if (paymentList.length() != 0)
                            recycler_payment.getAdapter().notifyDataSetChanged();
                        else {
                            recycler_payment.setVisibility(View.GONE);
                            msg.setVisibility(View.VISIBLE);
                        }

                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });

    }
}