package lk.hd192.getsafedriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import lk.hd192.getsafedriver.Utils.DirectionJSONParser;
import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.LocationUpdates;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.UserLocation;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

public class Map extends GetSafeDriverBase {

    MapView mapView;


    private GoogleMap googleMap;
    ArrayList<UserLocation> dropOffLocations = new ArrayList<>();
    LocationManager locationManager;
    GetSafeDriverServices getSafeDriverServices;
    private DatabaseReference mRootRef, locationRef;
    String locationProvider = LocationManager.GPS_PROVIDER;
    CameraPosition cameraPosition;
    TextView txt_name, txt_distance, txt_time;

    String wayPoints;
    LinearLayout lnr_trip_details;
TextView txt_error;
    JSONArray passengerList;
    Polyline polyline;
    LocationListener locationListener;
    LocationManager locationManagerSender;
    TinyDB tinyDB;
    Bitmap originMarker, finalMarker;
    Button btn_start_trip, btn_absent, btn_dropNPick;
    Double currentLat, currentLon;
    Dialog dialog;
    Boolean isPickingCompleted = false;

    public static ArrayList<String> duration = new ArrayList<>();
    public static ArrayList<String> distance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        getSafeDriverServices = new GetSafeDriverServices();
        tinyDB = new TinyDB(getApplicationContext());
        mapView = findViewById(R.id.mapView);
        btn_start_trip = findViewById(R.id.btn_start_trip);
        txt_time = findViewById(R.id.txt_time);
        txt_distance = findViewById(R.id.txt_distance);
        txt_name = findViewById(R.id.txt_name);
        btn_absent = findViewById(R.id.btn_absent);
        btn_dropNPick = findViewById(R.id.btn_dropNPick);
        lnr_trip_details = findViewById(R.id.lnr_trip_details);
        txt_error = findViewById(R.id.txt_error);

        mRootRef = FirebaseDatabase.getInstance().getReference();

//        btnBack = findViewById(R.id.btn_location_back);

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("message");
//        Query lastQuery = myRef.orderByKey().limitToLast(1);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        tinyDB.putString("driver_id","5");

        if (tinyDB.getBoolean("isStaffDriver"))
            locationRef = mRootRef.child("Staff_Drivers").child(tinyDB.getString("driver_id")).child(tinyDB.getString("driver_id")).child("Location");
        else
            locationRef = mRootRef.child("School_Drivers").child(tinyDB.getString("driver_id")).child(tinyDB.getString("driver_id")).child("Location");


        if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 100);
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        originMarker = bitmapSizeByScale(BitmapFactory.decodeResource(getResources(), R.drawable.marker_passenger), 1);
        finalMarker = bitmapSizeByScale(BitmapFactory.decodeResource(getResources(), R.drawable.marker_end), 1);

        currentLat = 6.965495959761049;
        currentLon = 79.95475497680536;

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        btn_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (UserLocation user : dropOffLocations) {
                    if (user.getPassengerName().equals(txt_name.getText().toString())) {
                        user.setAbsent(true);
                        markAbsent(user.getId());
                        ArrayList<String> strList = (ArrayList<String>) (ArrayList<?>) (dropOffLocations);
                        tinyDB.putListString("tripRoute", strList);
                    }
                    if (!user.getPicked())
                        if (user.getAbsent())

                            isPickingCompleted = true;
                        else
                            isPickingCompleted = false;
                    else
                        isPickingCompleted = true;
                }

                Log.e("trip status absent",isPickingCompleted+"");

            }
        });
        btn_dropNPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isPickingCompleted = true;
                for (UserLocation user : dropOffLocations) {
                    if (user.getPassengerName().equals(txt_name.getText().toString())) {
                        if (user.getPicked()) {
                            dropOffPassenger(user.getId());
                            user.setDropped(true);
                        } else {
                            pickUpPassenger(user.getId());
                            user.setPicked(true);
                        }
                        ArrayList<String> strList = (ArrayList<String>) (ArrayList<?>) (dropOffLocations);
                        tinyDB.putListString("tripRoute", strList);
                    }
                    if (!user.getPicked())
                        if (user.getAbsent())

                            isPickingCompleted = true;
                        else
                            isPickingCompleted = false;
                    else
                        isPickingCompleted = true;
                }

                if (isPickingCompleted)
                    drawMapPolyline(new LatLng(currentLat, currentLon));


                Log.e("trip status pick",isPickingCompleted+"");
            }
        });
        btn_start_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);


                if (btn_start_trip.getText().toString().equals("Start Trip")) {
                    txt_error.setVisibility(View.GONE);
                    lnr_trip_details.setVisibility(View.VISIBLE);
                    tinyDB.putBoolean("isTripStart", true);



                    Log.e("drop offs", tinyDB.getListString("tripRoute") + "");
                    btn_start_trip.setBackground(getResources().getDrawable(R.drawable.bg_btn_stop));
                    if (timeOfDay >= 0 && timeOfDay < 12) {

                        getMorningList();
                    } else if (timeOfDay >= 12) {
                        getEveningList();
                    }

                    notifyTripStart();
                    btn_start_trip.setText("End Trip");
                    locationRef.child("status").setValue(btn_start_trip.getText().toString());

                } else {

                    notifyTripEnd();
                    dropOffLocations.clear();
                    txt_error.setVisibility(View.VISIBLE);
                    lnr_trip_details.setVisibility(View.GONE);
                    distance.clear();
                    googleMap.clear();
                    duration.clear();
                    tinyDB.putBoolean("isTripStart", false);
                    tinyDB.remove("tripRoute");

                    btn_start_trip.setBackground(getResources().getDrawable(R.drawable.bg_btn_ok));
                    btn_start_trip.setText("Start Trip");
                    locationRef.child("status").setValue(btn_start_trip.getText().toString());

                }


            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                LocationUpdates locationUpdates= new LocationUpdates( location.getLatitude(), location.getLongitude());
//                locationRef.setValue(locationUpdates);

//                locationRef.child("latitude").setValue(location.getLatitude());
//                locationRef.child("longitude").setValue(location.getLongitude());
                // pushId++;
                java.util.Map messageMap = new HashMap();
                messageMap.put("latitude", location.getLatitude());
                messageMap.put("longitude", location.getLongitude());
                messageMap.put("status", btn_start_trip.getText().toString());


                locationRef.updateChildren(messageMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e("Error", databaseError.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }


        };

        locationManagerSender = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManagerSender.requestLocationUpdates(LocationManager.GPS_PROVIDER, 12000, 0, locationListener);

        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.e("onChildChanged", "exe");
                    LocationUpdates locationUpdates = snapshot.getValue(LocationUpdates.class);
                    currentLat = locationUpdates.getLatitude();
                    currentLon = locationUpdates.getLongitude();
                    googleMap.clear();

                    drawMapPolyline(new LatLng(currentLat, currentLon));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        try {

            MapsInitializer.initialize(getApplicationContext());
            loadMap();

        } catch (Exception e) {

            Log.e("Map Load Err >> ", e + "");
            e.printStackTrace();

        }
//        findViewById(R.id.btn_location_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tinyDB.getBoolean("isTripStart")) {

//            btn_start_trip.setBackground(getResources().getDrawable(R.drawable.bg_btn_stop));
//            btn_start_trip.setText("End Trip");
//            txt_error.setVisibility(View.GONE);
//            lnr_trip_details.setVisibility(View.VISIBLE);
//            dropOffLocations = new ArrayList<>();
////            for (String s :  tinyDB.getListString("tripRoute")) {
//////                Object o=s;
//////                userLocation.fo
////
////            }
//            dropOffLocations.add(new UserLocation(tinyDB.getListString("tripRoute").get(0)));
//
//            Log.e("FROM string",  tinyDB.getListString("tripRoute").get(0)+"");
//            Log.e("formt iny",  dropOffLocations.get(0).getDropLatitude()+"");
//            drawMapPolyline(new LatLng(currentLat, currentLon));


        }
        else{

            txt_error.setVisibility(View.VISIBLE);
            lnr_trip_details.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onStop() {

        if (!dropOffLocations.isEmpty()) {

            ArrayList<String> strings = new ArrayList<>();
            for (Object object : dropOffLocations) {
                strings.add(object != null ? object.toString() : null);
            }
            tinyDB.putListString("tripRoute", strings);
            Log.e("tiny", tinyDB.getListString("tripRoute") + "");
            distance.clear();
            googleMap.clear();
            duration.clear();

            Log.e("new array", dropOffLocations.get(0) + "");

        }

        super.onStop();

    }

    public void loadMap() {

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {

                googleMap = gMap;
                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getApplicationContext(), R.raw.dark_map));
                if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, 100);
                    return;
                }


                googleMap.setMyLocationEnabled(false);

                try {


                    new CountDownTimer(2000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {


                        }
                    }.start();


                } catch (Exception e) {


                    //Toast.makeText(getApplicationContext(), "Something Went Wrong in Location Service !", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Map.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Map.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Map.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Map.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    //
//    public void locationAddress(double lat, double lon) {
//        Geocoder geocoder;
//        List<Address> addressList;
//
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        try {
//            addressList = geocoder.getFromLocation(lat, lon, 1);
//
//            Log.e("address", addressList.size() + " ");
//
//            if (addressList.size() == 0) {
//
//                showToast(dialog, "Oops.. \nNo Address Found in this Area ", 1);
//
//
//            } else {
////                mConfirm.setEnabled(true);
////                LOC_ADDRESS = addressList.get(0).getAddressLine(0);
//            }
//
//
//        } catch (IOException e) {
//
//
//            showToast(dialog, "Oops.. \nan error occurred ", 0);
//
//            e.printStackTrace();
//        }
//    }


//    float[] result=new float[100];
//    private void sortLocations() {
//        try {
//        result[0]=3;
//        result[1]=3;
//        result[2]=3;
//
//    for (int counter = 1; counter < dropOffLocations.size() - 1; counter++) {
//
//
//        Location.distanceBetween(dropOffLocations.get(0).getLatitude(),
//                dropOffLocations.get(0).getLongitude(),
//                dropOffLocations.get(counter).getLatitude(),
//                dropOffLocations.get(counter).getLongitude(), result);
//
//        Log.e("sorted dis",  counter+"");
//        Log.e("sorted dis",  result.length+"");
//        Collections.sort(dropOffLocations);
//    }
//
//}catch (Exception e){
//    Log.e("sort ex",e.getMessage());
//}
//    }

    public void drawMapPolyline(LatLng origin) {
        Log.e("draw polyline","exe");
        try {

            for (int f = 0; f < distance.size(); f++) {
                if (Double.parseDouble(distance.get(f)) < 500)
                    informDriverArrival(dropOffLocations.get(f).getId());

            }
            LatLng dest;

            if (dropOffLocations.size() != 0) {

                if (isPickingCompleted) {
                    dest = new LatLng(dropOffLocations.get(dropOffLocations.size() - 1).getDropLatitude(), dropOffLocations.get(dropOffLocations.size() - 1).getDropLongitude());

                    googleMap.addMarker(new MarkerOptions()
                            .position(origin).icon(BitmapDescriptorFactory.fromBitmap(finalMarker)).title("Your location"));


                    wayPoints = "&waypoints=";
                    for (int l = 0; l < dropOffLocations.size(); l++) {

                        if (dropOffLocations.size() - 1 == l) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(dest).icon(BitmapDescriptorFactory.fromBitmap(finalMarker)).title(dropOffLocations.get(l).getPassengerName()));
                            continue;
                        }
                        if (l == 0)

                            wayPoints += dropOffLocations.get(l).getDropLatitude() + "," + dropOffLocations.get(l).getDropLongitude();

                        else
                            wayPoints += "|" + dropOffLocations.get(l).getDropLatitude() + "," + dropOffLocations.get(l).getDropLongitude();


                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(dropOffLocations.get(l).getDropLatitude(), dropOffLocations.get(l).getDropLongitude())).icon(BitmapDescriptorFactory.fromBitmap(originMarker)).title(dropOffLocations.get(l).getPassengerName()));


                    }
                } else {

                    dest = new LatLng(dropOffLocations.get(dropOffLocations.size() - 1).getPickUpLatitude(), dropOffLocations.get(dropOffLocations.size() - 1).getPickUpLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(origin).icon(BitmapDescriptorFactory.fromBitmap(finalMarker)).title("Your location"));


                    wayPoints = "&waypoints=";
                    for (int l = 0; l < dropOffLocations.size(); l++) {

                        if (dropOffLocations.size() - 1 == l) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(dest).icon(BitmapDescriptorFactory.fromBitmap(finalMarker)).title(dropOffLocations.get(l).getPassengerName()));
                            continue;
                        }
                        if (l == 0)

                            wayPoints += dropOffLocations.get(l).getPickUpLatitude() + "," + dropOffLocations.get(l).getPickUpLongitude();

                        else
                            wayPoints += "|" + dropOffLocations.get(l).getPickUpLatitude() + "," + dropOffLocations.get(l).getPickUpLongitude();


                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(dropOffLocations.get(l).getPickUpLatitude(), dropOffLocations.get(l).getPickUpLongitude())).icon(BitmapDescriptorFactory.fromBitmap(originMarker)).title(dropOffLocations.get(l).getPassengerName()));


                    }
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
//                            Log.e("selected latlong", marker.getPosition() + "");
                        for (int v = 0; v < dropOffLocations.size(); v++) {
                            if (dropOffLocations.get(v).getPassengerName().equals(marker.getTitle()))
                                if (distance.size() != 0 & duration.size() != 0) {
                                    txt_name.setText(": " + dropOffLocations.get(v).getPassengerName());
                                    txt_distance.setText(": " + distance.get(v));
                                    txt_time.setText(": " + duration.get(v));
                                } else {

                                    txt_name.setText("");
                                    txt_distance.setText("");
                                    txt_time.setText("");
                                }

                        }

                        return false;
                    }
                });
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(dest);

                LatLngBounds bounds = builder.build();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 75));

                CameraPosition camPos = new CameraPosition.Builder(googleMap.getCameraPosition()).target(bounds.getCenter()).tilt(35).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPos));
//
//        int padding = 1;
//
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);


//        googleMap.animateCamera(cu);


                String url = getDirectionsUrl(origin, dest);


                DownloadTask downloadTask = new DownloadTask();


                downloadTask.execute(url);
            }
        }
        catch (Exception e){
            Log.e("ez",e.getMessage());
         e.printStackTrace();
        }

    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //avoid highways
        String avoidList = "&avoid=highways";


        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + wayPoints + avoidList + "&key=" + getString(R.string.API_KEY);

        // Output format
        String output = "json";


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("direction url", url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


//            Log.e("POLY - results ", result + "");

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(14);
                lineOptions.color(getResources().getColor(R.color.button_blue));
            }

            // Drawing polyline in the Google Map for the i-th route

            if (polyline != null) {
                polyline.remove();
            }

            try {

                polyline = Map.this.googleMap.addPolyline(lineOptions);

            } catch (Exception e) {
                e.printStackTrace();
            }


//            googleMap.addPolyline(lineOptions);
        }
    }


    public Bitmap bitmapSizeByScale(Bitmap bitmapIn, float scale_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scale_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scale_zero_to_one_f), false);

        return bitmapOut;
    }

    public Double calculateDistance(Double latOne, Double lonOne, Double latTwo, Double lonTwo) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lonOne = Math.toRadians(lonOne);
        lonTwo = Math.toRadians(lonTwo);
        latOne = Math.toRadians(latOne);
        latTwo = Math.toRadians(latTwo);

        // Haversine formula
        double dlon = lonTwo - lonOne;
        double dlat = latTwo - latOne;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(latOne) * Math.cos(latTwo)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r) * 1000;
    }

    void getPickupLatLong() {
        try {
            Log.e("getPickupLatLong","exe");
            for (int jk = 0; jk < passengerList.length(); jk++) {

                dropOffLocations.add(jk, new UserLocation(passengerList.getJSONObject(jk).getString("id"), passengerList.getJSONObject(jk).getString("name"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("pick_up_latitude"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("pick_up_longitude"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("drop_off_latitude"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("drop_off_longitude"), calculateDistance(currentLat, currentLon, passengerList.getJSONObject(jk).getJSONObject("location").getDouble("pick_up_latitude"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("pick_up_longitude")), false, false, false));

            }
            Collections.sort(dropOffLocations);
            for (int h = 0; h < dropOffLocations.size(); h++) {

                Log.e("distance  ", dropOffLocations.get(h).getDistance() + "");

            }
            drawMapPolyline(new LatLng(currentLat, currentLon));
            ArrayList<String> strList = (ArrayList<String>) (ArrayList<?>) (dropOffLocations);
            tinyDB.putListString("tripRoute", strList);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    void getDropLatLong() {
//        try {
//            Log.e("eve", "list");
//            for (int jk = 0; jk < passengerList.length(); jk++) {
//
//                dropOffLocations.add(jk, new UserLocation(passengerList.getJSONObject(jk).getString("name"),  calculateDistance(currentLat, currentLon, passengerList.getJSONObject(jk).getJSONObject("location").getDouble("drop_off_latitude"), passengerList.getJSONObject(jk).getJSONObject("location").getDouble("drop_off_longitude"))));
//
//            }
//            Collections.sort(dropOffLocations);
//            drawMapPolyline(new LatLng(dropOffLocations.get(0).getLatitude(), dropOffLocations.get(0).getLongitude()));
//        } catch (Exception e) {
//
//        }

    }

    private void getMorningList() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.JOURNEY_LIST_MORNING), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    passengerList = new JSONArray();


                    if (result.getBoolean("status")) {
                        Log.e("response", "ok");
                        passengerList = result.getJSONArray("models");
                        getPickupLatLong();
                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void getEveningList() {

        HashMap<String, String> tempParam = new HashMap<>();


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.JOURNEY_LIST_MORNING), 1, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    passengerList = new JSONArray();
                    Log.e("response", result + "");

                    if (result.getBoolean("status")) {

                        passengerList = result.getJSONArray("models");
                        getPickupLatLong();
                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void notifyTripStart() {

        HashMap<String, String> tempParam = new HashMap<>();

        tempParam.put("latitude", currentLat.toString());
        tempParam.put("longitude", currentLon.toString());
        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.JOURNEY_START), 2, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void markAbsent(String id) {

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", id);


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.MARK_PASSENGER_ABSENT), 2, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void pickUpPassenger(String id) {

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", id);


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.PASSENGER_PICKUP), 2, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void dropOffPassenger(String id) {

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", id);


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.PASSENGER_DROP), 3, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void notifyTripEnd() {

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("latitude", currentLat.toString());
        tempParam.put("longitude", currentLon.toString());

        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.JOURNEY_END), 2, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }

    private void informDriverArrival(String id) {

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", id);


        getSafeDriverServices.networkJsonRequestWithHeaders(this, tempParam, getString(R.string.BASE_URL) + getString(R.string.INFORM_DRIVER_ARRIVAL), 2, tinyDB.getString("token"), new VolleyJsonCallback() {

            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("status")) {


                    } else
                        showToast(dialog, result.getString("validation_errors"), 0);


                } catch (Exception e) {

                    Log.e("ex", e.getMessage());
                }

            }
        });


    }
}