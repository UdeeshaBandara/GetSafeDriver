package lk.hd192.getsafedriver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.libizo.CustomEditText;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class AddDriverFirst extends Fragment implements DatePickerDialog.OnDateSetListener {

    int year;
    int month;
    int day;
    TypeBottomSheet typeBottomSheet;
    GetSafeDriverServices getSafeDriverServices;
    SimpleDateFormat simpleDateFormat;
    public boolean isValidated = false;
    public static String driverId;
    TinyDB tinyDB;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    CustomEditText txtDriverBirthday, txtType, edit_txt_name, edit_txt_nic, edit_text_license_no_main, edit_text_telephone, txt_email;

    public AddDriverFirst() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_driver_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        mAuth = FirebaseAuth.getInstance();
        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getActivity());
        txtDriverBirthday = view.findViewById(R.id.txt_driver_birthday);
        txtType = view.findViewById(R.id.txt_type);
        edit_txt_name = view.findViewById(R.id.edit_txt_name);
        edit_txt_nic = view.findViewById(R.id.edit_txt_nic);
        edit_text_license_no_main = view.findViewById(R.id.edit_text_license_no_main);
        edit_text_telephone = view.findViewById(R.id.edit_text_telephone);
        txt_email = view.findViewById(R.id.txt_email);

        txtDriverBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(year, month, day, R.style.DatePickerSpinner);
            }
        });
        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //...
                typeBottomSheet = new TypeBottomSheet(getActivity());
                typeBottomSheet.setContentView(R.layout.bottom_sheet_type);
                typeBottomSheet.show();

            }
        });

    }

    public boolean validateFields() {

        if (TextUtils.isEmpty(edit_txt_name.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_txt_name);
            edit_txt_name.setError("Please enter name");
            return false;
        } else if (TextUtils.isEmpty(edit_txt_nic.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_txt_nic);
            edit_txt_nic.setError("Please enter NIC number");
            return false;
        } else if (TextUtils.isEmpty(edit_text_license_no_main.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_text_license_no_main);
            edit_text_license_no_main.setError("Please enter License number");
            return false;
        } else if (TextUtils.isEmpty(edit_text_telephone.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_text_telephone);
            edit_text_telephone.setError("Please enter contact number");
            return false;
        } else if (TextUtils.isEmpty(txtDriverBirthday.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(txtDriverBirthday);
            txtDriverBirthday.setError("Please select birthday");
            return false;
        } else if (TextUtils.isEmpty(txt_email.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_txt_name);
            txt_email.setError("Please enter email");
            return false;
        } else if (TextUtils.isEmpty(txtType.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(txtType);
            txtType.setError("Please select transport type");
            return false;
        } else {
            return registerDriverBasic();
        }

    }

    private boolean registerDriverBasic() {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("name", edit_txt_name.getText().toString());
        tempParam.put("nic", edit_txt_nic.getText().toString());
        tempParam.put("license_no", edit_text_license_no_main.getText().toString());
        tempParam.put("phone", edit_text_telephone.getText().toString());
        tempParam.put("birthday", txtDriverBirthday.getText().toString());
        tempParam.put("email", txt_email.getText().toString());
        tempParam.put("type", txtType.getText().toString());




        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_SAVE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("saved_status")) {
                        tinyDB.putString("phone_no", edit_text_telephone.getText().toString());
                        tinyDB.putString("email", txt_email.getText().toString());
                        tinyDB.putBoolean("isStaffDriver", txtType.getText().toString().equals("School Transport"));
                        registerFirebaseUser();
                        isValidated = true;

                    }


                } catch (JSONException ex) {
                    ex.printStackTrace();
                    isValidated = false;
                }


            }
        });
        return isValidated;

    }
    private void registerFirebaseUser() {
        mAuth.createUserWithEmailAndPassword( tinyDB.getString("email"),tinyDB.getString("phone_no"))
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String Uid = currentUser.getUid();
                            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("Image", "Default");
                            firebaseDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

//                                        startActivity(new Intent(getApplicationContext(), Messaging.class));
//                                        finishAffinity();

                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.


                        }

                        // ...
                    }
                });
    }


    public class TypeBottomSheet extends BottomSheetDialog {
        public TypeBottomSheet(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onStart() {
            super.onStart();
            getBehavior().setPeekHeight(350, true);
            getBehavior().setDraggable(false);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            findViewById(R.id.school_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtType.setText("School Transport");
                    typeBottomSheet.dismiss();

                }
            });
            findViewById(R.id.staff_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtType.setText("Staff Transport");
                    typeBottomSheet.dismiss();
                }
            });
//
        }
        //            txtSchool=v.findViewById(R.id.school_search);
//            txtStaff=v.findViewById(R.id.staff_search);
//
//            txtSchool.setOnClickListener(new View.OnClickListener());

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        txtDriverBirthday.setHint(simpleDateFormat.format(calendar.getTime()));
    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getActivity())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .showDaySpinner(true)
                .maxDate(year - 18, month, day)
                .minDate(year - 65, month, day)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }
}