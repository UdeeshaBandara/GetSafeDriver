package lk.hd192.getsafedriver;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;


public class AddDriverThird extends Fragment {


    TextView txtNonAc, txtAc, txtCam, txtNonCam, editTxtRegistrationNo;
    CustomEditText edit_txt_vehicle_type, edit_txt_vehicle_make, edit_txt_vehicle_model, edit_txt_registration_no, edit_txt_seating;
    TypeBottomSheet typeBottomSheet;
    GetSafeDriverServices getSafeDriverServices;
    String isAc="",isCam="";

    public AddDriverThird() {
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
        return inflater.inflate(R.layout.fragment_add_driver_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtAc = view.findViewById(R.id.txt_ac);
        txtNonAc = view.findViewById(R.id.txt_non_ac);
        txtCam = view.findViewById(R.id.txt_cam);
        txtNonCam = view.findViewById(R.id.txt_non_cam);
        editTxtRegistrationNo = view.findViewById(R.id.edit_txt_registration_no);
        edit_txt_vehicle_type = view.findViewById(R.id.edit_txt_vehicle_type);
        edit_txt_vehicle_make = view.findViewById(R.id.edit_txt_vehicle_make);
        edit_txt_vehicle_model = view.findViewById(R.id.edit_txt_vehicle_model);
        edit_txt_registration_no = view.findViewById(R.id.edit_txt_registration_no);
        edit_txt_seating = view.findViewById(R.id.edit_txt_seating);
        getSafeDriverServices= new GetSafeDriverServices();

        edit_txt_vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeBottomSheet = new TypeBottomSheet(getActivity());
                typeBottomSheet.setContentView(R.layout.bottom_sheet_type);
                typeBottomSheet.show();


            }
        });
        txtAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAc.setBackgroundResource(R.drawable.bg_btn_left_radio_select);
                txtNonAc.setBackgroundResource(R.drawable.bg_btn_right_radio_deselect);
                isAc="true";

            }
        });
        txtNonAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNonAc.setBackgroundResource(R.drawable.bg_btn_right_radio_select);
                txtAc.setBackgroundResource(R.drawable.bg_btn_left_radio_deselect);
                isAc="false";

            }
        });
        txtCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCam.setBackgroundResource(R.drawable.bg_btn_left_radio_select);
                txtNonCam.setBackgroundResource(R.drawable.bg_btn_right_radio_deselect);
                isCam="true";

            }
        });
        txtNonCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNonCam.setBackgroundResource(R.drawable.bg_btn_right_radio_select);
                txtCam.setBackgroundResource(R.drawable.bg_btn_left_radio_deselect);
                isCam="false";

            }
        });
    }

    public void validateFields() {
        if (TextUtils.isEmpty(edit_txt_vehicle_type.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_txt_vehicle_make.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_txt_vehicle_model.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_txt_registration_no.getText().toString())) {
        } else if (TextUtils.isEmpty(edit_txt_seating.getText().toString())) {
        } else if (isAc.equals("")) {
        } else if (isCam.equals("")) {
        } else {

            registerVehicle();

        }

    }
    private void registerVehicle() {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", AddDriverFirst.driverId);
        tempParam.put("type", edit_txt_vehicle_type.getText().toString());
        tempParam.put("make", edit_txt_vehicle_make.getText().toString());
        tempParam.put("model", edit_txt_vehicle_model.getText().toString());
        tempParam.put("registration_no", edit_txt_registration_no.getText().toString());
        tempParam.put("seating_capacity", edit_txt_seating.getText().toString());
        tempParam.put("ac", isAc);
        tempParam.put("camera", isCam);


        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_VEHICLE), 2, new VolleyJsonCallback() {
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
        TextView school_search, staff_search;

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
            school_search = findViewById(R.id.school_search);
            staff_search = findViewById(R.id.staff_search);
            school_search.setText("Van");
            staff_search.setText("Bus");

            school_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_txt_vehicle_type.setText("Van");
                    typeBottomSheet.dismiss();
                }
            });
            staff_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_txt_vehicle_type.setText("Bus");
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
}