package lk.hd192.getsafedriver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class AddDriverSecond extends Fragment {
    CustomEditText editTextAddOne, editTextAddTwo, editTextPick, edit_txt_district;
    GetSafeDriverServices getSafeDriverServices;
    public boolean isSecondValidated = false;
    TinyDB tinyDB;
    View popupView;
    LatLng pinnedLocation;
    String locationProvider = LocationManager.GPS_PROVIDER;
    CameraPosition cameraPosition;
    MapView mPickupLocation;
    Double latitude, longitude;
    GoogleMap googleMap;
    Button mConfirm;
    LocationManager locationManager;
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
        tinyDB = new TinyDB(getActivity());
        editTextAddOne = view.findViewById(R.id.edit_txt_add_one);
        editTextAddTwo = view.findViewById(R.id.edit_txt_add_two);
        editTextPick = view.findViewById(R.id.edit_txt_pick);
        edit_txt_district = view.findViewById(R.id.edit_txt_district);
        getSafeDriverServices = new GetSafeDriverServices();

        editTextPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateMapPopup(v,savedInstanceState);

            }
        });


    }

    public void validateFields() {
        if (TextUtils.isEmpty(editTextAddOne.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextAddOne);
            editTextAddOne.setError("Please enter address one");

        } else if (TextUtils.isEmpty(editTextAddTwo.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextAddTwo);
            editTextAddTwo.setError("Please enter address two");

        } else if (TextUtils.isEmpty(editTextPick.getText().toString())) {
            YoYo.with(Techniques.Bounce)
                    .duration(1000)
                    .playOn(editTextPick);
            editTextPick.setError("Please select location");

        } else {
//            return true;
            driverLocationDetails();
        }

    }

    public interface RegisterCallSecond {

        void showPageThree();
    }

    public static void dimBehind(PopupWindow popupWindow) {

        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }

    public void onCreateMapPopup(View view, Bundle savedInstanceState) {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.map_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView, GetSafeDriverBase.device_width - 150, GetSafeDriverBase.device_height - 250, true);

        popupWindow.showAtLocation(view, Gravity    .CENTER, 0, 0);
        dimBehind(popupWindow);

        mConfirm = popupView.findViewById(R.id.btn_confirmMapLocation);
        mPickupLocation = popupView.findViewById(R.id.map_pickupLocation);


        try {
            MapsInitializer.initialize(getActivity());
            loadMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPickupLocation.onCreate(savedInstanceState);
        mPickupLocation.onResume();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  popupWindow.dismiss();
                return true;
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editTextPick.setText(GetSafeDriverBase.LOC_ADDRESS);
                popupWindow.dismiss();

            }
        });

    }


    public void locationAddress(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addressList;

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);


            if (addressList.size() == 0) {


                // customToast("Oops.. \nNo Address Found in this Area ",0);
                mConfirm.setEnabled(false);

            } else {
                mConfirm.setEnabled(true);
                GetSafeDriverBase.LOC_ADDRESS = addressList.get(0).getAddressLine(0);
            }


        } catch (IOException e) {
            //   customToast("Oops.. \nan error occurred",1);
            e.printStackTrace();
        }
    }

    public void loadMap() {
        mPickupLocation.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                try {
                    googleMap = mMap;
                    googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getActivity(), R.raw.dark_map));
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 100);
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);


                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settings);

                    }
                    final Location location = locationManager.getLastKnownLocation(locationProvider);

                    if (pinnedLocation == null)
                        cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).build();
                    else
                        cameraPosition = new CameraPosition.Builder().target(pinnedLocation).zoom(15).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {

                            locationAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                            pinnedLocation = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);


                            latitude = cameraPosition.target.latitude;
                            longitude = cameraPosition.target.longitude;


                        }
                    });

                } catch (Exception e) {

                }


            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean driverLocationDetails() {


        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("latitude", latitude.toString());
        tempParam.put("longitude", longitude.toString());
        tempParam.put("add1", editTextAddOne.getText().toString());
        tempParam.put("add2", editTextAddTwo.getText().toString());


        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_LOCATION), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res",result+"");

                    if (result.getBoolean("status")) {

                        ((RegisterCallSecond) getActivity()).showPageThree();
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