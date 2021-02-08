package lk.hd192.getsafedriver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddDriverFirst extends Fragment implements DatePickerDialog.OnDateSetListener {

    int year;
    int month;
    int day;
    TypeBottomSheet typeBottomSheet;
    SimpleDateFormat simpleDateFormat;
    TextView txtDriverBirthday,txtType;

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

        txtDriverBirthday = view.findViewById(R.id.txt_driver_birthday);
        txtType = view.findViewById(R.id.txt_type);

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
                TypeBottomSheet typeBottomSheet = new TypeBottomSheet(getActivity());
                typeBottomSheet.setContentView(R.layout.bottom_sheet_type);
                typeBottomSheet.show();

            }
        });

    }
    public void validateFields(){




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
                }
            });
            findViewById(R.id.staff_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtType.setText("Staff Transport");
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