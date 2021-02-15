package lk.hd192.getsafedriver;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;


public class AddDriverSecond extends Fragment {
    CustomEditText editTextAddOne, editTextAddTwo, editTextPick,edit_txt_district;
    GetSafeDriverServices getSafeDriverServices;
    public boolean isSecondValidated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_driver_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextAddOne = view.findViewById(R.id.edit_txt_add_one);
        editTextAddTwo = view.findViewById(R.id.edit_txt_add_two);
        editTextPick = view.findViewById(R.id.edit_txt_pick);
        edit_txt_district = view.findViewById(R.id.edit_txt_district);
        getSafeDriverServices = new GetSafeDriverServices();

    }

    public boolean validateFields() {
        if (TextUtils.isEmpty(editTextAddOne.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextAddOne);
            editTextAddOne.setError("Please enter address one");
            return false;
        } else if (TextUtils.isEmpty(editTextAddTwo.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextAddTwo);
            editTextAddTwo.setError("Please enter address two");
            return false;
        } else if (TextUtils.isEmpty(editTextPick.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextPick);
            editTextPick.setError("Please select location");
            return false;
        }  else if (TextUtils.isEmpty(edit_txt_district.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(edit_txt_district);
            edit_txt_district.setError("Please select drop-off district");
            return false;
        } else {
            driverLocationDetails();
        }
        return false;
    }

    private boolean driverLocationDetails() {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", AddDriverFirst.driverId);
        tempParam.put("latitude", "");
        tempParam.put("longitude", "");
        tempParam.put("add1", editTextAddOne.getText().toString());
        tempParam.put("add2", editTextAddTwo.getText().toString());


        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_LOCATION), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("saved_status")) {
                        isSecondValidated = true;
                    }


                } catch (JSONException ex) {
                    ex.printStackTrace();
                    isSecondValidated = false;
                }


            }
        });
        return isSecondValidated;
    }

}