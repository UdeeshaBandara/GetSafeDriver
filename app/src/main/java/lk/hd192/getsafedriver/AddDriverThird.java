package lk.hd192.getsafedriver;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AddDriverThird extends Fragment {


    TextView txtNonAc, txtAc,txtCam,txtNonCam,editTxtRegistrationNo;

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

        txtAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAc.setBackgroundResource(R.drawable.bg_btn_left_radio_select);
                txtNonAc.setBackgroundResource(R.drawable.bg_btn_right_radio_deselect);

            }
        });
        txtNonAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNonAc.setBackgroundResource(R.drawable.bg_btn_right_radio_select);
                txtAc.setBackgroundResource(R.drawable.bg_btn_left_radio_deselect);

            }
        });
        txtCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCam.setBackgroundResource(R.drawable.bg_btn_left_radio_select);
                txtNonCam.setBackgroundResource(R.drawable.bg_btn_right_radio_deselect);

            }
        });
        txtNonCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNonCam.setBackgroundResource(R.drawable.bg_btn_right_radio_select);
                txtCam.setBackgroundResource(R.drawable.bg_btn_left_radio_deselect);

            }
        });
    }
}