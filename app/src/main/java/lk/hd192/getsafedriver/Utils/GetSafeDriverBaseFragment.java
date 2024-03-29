package lk.hd192.getsafedriver.Utils;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import lk.hd192.getsafedriver.R;

public class GetSafeDriverBaseFragment extends Fragment {

    static int device_width, device_height;
    public TinyDB tinyDB;


    public static  boolean isEnable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        tinyDB = new TinyDB(getActivity());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        device_width = displayMetrics.widthPixels;
        device_height = displayMetrics.heightPixels;

    }

    public void showWarningToast(final Dialog dialog, String msg, int type) {


        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 550);
        dialog.setTitle(null);
        switch (type) {
            case 0:
                dialog.setContentView(R.layout.toast_layout_warning);
                break;
            case 1:
                dialog.setContentView(R.layout.toast_layout_location);
                break;


        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView msgToShow = dialog.findViewById(R.id.toast_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        msgToShow.setText(msg);

        dialog.show();
    }


    public void showToast(final Dialog dialog, String msg, int type) {


        // Setting dialogview
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);


        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        switch (type) {
            case 0:
                dialog.setContentView(R.layout.toast_layout_warning);
                break;
            case 1:
                dialog.setContentView(R.layout.toast_layout_location);
                break;
            case 2:
                dialog.setContentView(R.layout.toast_layout_success);
                break;



        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView msgToShow = dialog.findViewById(R.id.toast_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        msgToShow.setText(msg);

        dialog.show();
    }


}
