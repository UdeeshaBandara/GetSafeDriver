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

import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class AddDriverFirst extends Fragment implements DatePickerDialog.OnDateSetListener {

    int year;
    int month;
    int day;
    TypeBottomSheet typeBottomSheet;
    GetSafeDriverServices getSafeDriverServices;
    SimpleDateFormat simpleDateFormat;
    boolean isValidated = false;
    public static String driverId;
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
        getSafeDriverServices = new GetSafeDriverServices();

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

    public void validateFields() {

        if (TextUtils.isEmpty(edit_txt_name.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_txt_nic.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_text_license_no_main.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_text_telephone.getText().toString())) {
        } else if (TextUtils.isEmpty(txtDriverBirthday.getText().toString())) {
        } else if (TextUtils.isEmpty(txt_email.getText().toString())) {
        } else if (TextUtils.isEmpty(txtType.getText().toString())) {
        } else {
            registerDriverBasic();
        }

    }

    private void registerDriverBasic() {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("name", "");
        tempParam.put("nic", "");
        tempParam.put("license_no", "");
        tempParam.put("phone", "");
        tempParam.put("birthday", "");
        tempParam.put("email", "");
        tempParam.put("type", "");


        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_SAVE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("saved_status")) {


                    }


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


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