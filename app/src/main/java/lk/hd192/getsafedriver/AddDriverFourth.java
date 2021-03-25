package lk.hd192.getsafedriver;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.load.engine.Resource;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBaseFragment;
import lk.hd192.getsafedriver.Utils.GetSafeDriverServices;
import lk.hd192.getsafedriver.Utils.TinyDB;
import lk.hd192.getsafedriver.Utils.VolleyJsonCallback;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class AddDriverFourth extends GetSafeDriverBaseFragment {

    public ImageView imageDriver, imgLicOne, imgLicTwo, imgIdOne, imgIdTwo, img_vehicle_one, img_vehicle_three, img_vehicle_four, img_vehicle_two;
    List<Image> imagesList;
    ArrayList<String> imagesBase64;
    Image[] imagesToUpload = new Image[9];
    TinyDB tinyDB;
    Dialog dialog;
    public KProgressHUD hud;
    GetSafeDriverServices getSafeDriverServices;
    Boolean fromIdOne = false, fromIdTwo = false, fromLicOne = false, fromLicTwo = false, fromDriverPic = false, fromVehicleOne = false, fromVehicleTwo = false, fromVehicleThree = false, fromVehicleOneFour = false;

    public AddDriverFourth() {
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
        return inflater.inflate(R.layout.fragment_add_driver_fourth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tinyDB = new TinyDB(getActivity());
        getSafeDriverServices = new GetSafeDriverServices();
        imageDriver = view.findViewById(R.id.image_driver);
        imgLicTwo = view.findViewById(R.id.img_lic_two);
        imgLicOne = view.findViewById(R.id.img_lic_one);
        imgIdOne = view.findViewById(R.id.img_id_one);
        imgIdTwo = view.findViewById(R.id.img_id_two);
        img_vehicle_three = view.findViewById(R.id.img_vehicle_three);
        img_vehicle_one = view.findViewById(R.id.img_vehicle_one);
        img_vehicle_two = view.findViewById(R.id.img_vehicle_two);
        img_vehicle_four = view.findViewById(R.id.img_vehicle_four);

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        imagesList = new ArrayList<>();
        imagesBase64 = new ArrayList<>();

        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        imageDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDriverPic = true;
                openGallery(1);

            }
        });

        imgLicTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromLicTwo = true;
                openGallery(2);

            }
        });
        imgIdTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromIdTwo = true;
                openGallery(2);

            }
        });
        imgLicOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromLicOne = true;
                openGallery(2);


            }
        });
        imgIdOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromIdOne = true;
                openGallery(2);

            }
        });
        img_vehicle_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromVehicleOne = true;
                openGallery(4);

            }
        });
        img_vehicle_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromVehicleOne = true;
                openGallery(4);

            }
        });
        img_vehicle_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromVehicleOne = true;
                openGallery(4);

            }
        });
        img_vehicle_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromVehicleOne = true;
                openGallery(4);


            }
        });
    }

//    void openPickerDialog(int limit){
//        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(getActivity())
//                .setTitle("Pick Image")
//                .setMessage("Select image source")
//                .setCancelable(false)
//                .setPositiveButton("Gallery", R.drawable.ic_insert_photo_black_48dp, new BottomSheetMaterialDialog.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//
//                        dialogInterface.dismiss();
//                        openGallery(limit);
//                    }
//                })
//                .setNegativeButton("Camera", R.drawable.ic_camera_menu, new BottomSheetMaterialDialog.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//
//                        dialogInterface.dismiss();
//                        openCamera(limit);
//                    }
//                })
//                .build();
//
//        // Show Dialog
//        mBottomSheetDialog.show();
//
//    }

    void openGallery(int limit) {
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Params params = new Params();
        params.setCaptureLimit(limit);
        params.setPickerLimit(limit);
        params.setToolbarColor(R.color.statusBarColor);
        params.setActionButtonColor(R.color.statusBarColor);
        params.setButtonTextColor(R.color.statusBarColor);
        intent.putExtra(Constants.KEY_PARAMS, params);
        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("frag onActivityResult", data + "");
        if (resultCode != RESULT_OK) {
            return;
        }

        try {

            imagesList = data.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
            if (fromDriverPic) {
                imageDriver.setImageURI(imagesList.get(0).uri);
//                encodeImage(imagesList.get(0).imagePath);
                fromDriverPic = false;

                imagesToUpload[0] = imagesList.get(0);
                Log.e("1 imagesToUpload", imagesToUpload.length + "");
                imagesList.clear();


            } else if (fromIdTwo || fromIdOne) {
                if (imagesList.size() == 2) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);
                    imgIdTwo.setImageURI(imagesList.get(1).uri);
                    if (fromIdTwo) fromIdTwo = false;
                    else if (fromIdOne) fromIdOne = false;

                    imagesToUpload[1] = imagesList.get(0);
                    imagesToUpload[2] = imagesList.get(1);

                } else if (fromIdOne) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);

                    imagesToUpload[1] = imagesList.get(0);

                    fromIdOne = false;
                } else if (fromIdTwo) {

                    imgIdTwo.setImageURI(imagesList.get(0).uri);
                    fromIdTwo = false;
                    imagesToUpload[2] = imagesList.get(0);

                }

//                    imgIdOne.setImageURI(imagesList.get(0).uri);


                imagesList.clear();
            } else if (fromLicTwo || fromLicOne) {
                if (imagesList.size() == 2) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    imgLicTwo.setImageURI(imagesList.get(1).uri);
                    if (fromLicOne) fromLicOne = false;
                    else if (fromLicTwo) fromLicTwo = false;


                    imagesToUpload[3] = imagesList.get(0);
                    imagesToUpload[4] = imagesList.get(1);
                } else if (fromLicOne) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    fromLicOne = false;

                    imagesToUpload[3] = imagesList.get(0);
                } else if (fromLicTwo) {
                    imgLicTwo.setImageURI(imagesList.get(0).uri);
                    fromLicTwo = false;

                    imagesToUpload[4] = imagesList.get(0);

                }

                imagesList.clear();
            } else if (fromVehicleOne) {


                if (imagesList.size() == 4) {

                    img_vehicle_one.setImageURI(imagesList.get(0).uri);
                    img_vehicle_three.setImageURI(imagesList.get(2).uri);
                    img_vehicle_two.setImageURI(imagesList.get(1).uri);
                    img_vehicle_four.setImageURI(imagesList.get(3).uri);

                    imagesToUpload[5] = imagesList.get(0);
                    imagesToUpload[6] = imagesList.get(1);
                    imagesToUpload[7] = imagesList.get(2);
                    imagesToUpload[8] = imagesList.get(3);
                    fromVehicleOne = false;
                    imagesList.clear();
                } else
                    showToast(dialog, "Select at least 4 photos", 0);


            }


        } catch (
                Exception e) {
            e.printStackTrace();

        }


    }

    public interface LoadingCall {

        void showLoadingImage();

        void hideLoadingImage();

    }

    private String encodeImage(String path) throws IOException {
//        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);
        File imagefile = new File(path);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);


//        outputStreamWriter.close();
        //Base64.de
        return "data:image/jpeg;base64," + encImage;

    }

    public void convertToBase64AndUpload() throws IOException {

        boolean validated = true;
        ((LoadingCall) getActivity()).showLoadingImage();
//
        for (Image image : imagesToUpload) {
            if (image == null) {
                ((LoadingCall) getActivity()).hideLoadingImage();
                showToast(dialog, "Please add all photos", 0);
                validated = false;
                break;
            } else {


            }
        }
        if (validated) {
            Log.e("validated ", "ok");
            for (int g = 0; g < imagesToUpload.length; g++) {

                imagesBase64.add(g, encodeImage(imagesToUpload[g].imagePath));
            }
            addDriverImage();
            addDriverIdFront();
            addDriverIdBack();
            addDriverLicence();
            addVehicleImages();
//            ((LoadingCall) getActivity()).hideLoadingImage();
        }


    }

    private void addDriverImage() {
        Log.e("addDriverImage", "exe");

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image", imagesBase64.get(0));

        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_IMAGE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {

                    if (result.getBoolean("saved_status")) {
                        hideHUD();
                    } else ((LoadingCall) getActivity()).hideLoadingImage();

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    private void addDriverIdFront() {

        Log.e("addDriverIdFront", "exe");
        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image", imagesBase64.get(1));
        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_IMAGE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverIdFront", result + "");

                    if (result.getBoolean("saved_status")) {

                    } else ((LoadingCall) getActivity()).hideLoadingImage();
                    hideHUD();

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    private void addDriverIdBack() {

        Log.e("addDriverIdBack", "exe");
        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image", imagesBase64.get(2));

        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_NIC_BACK), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverIdBack", result + "");
                    if (result.getBoolean("saved_status")) {

                    } else ((LoadingCall) getActivity()).hideLoadingImage();
                    hideHUD();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    private void addDriverLicence() {

        Log.e("addDriverLicence", "exe");

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image", imagesBase64.get(3));

        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_LICENCE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverLicence", result + "");
                    if (result.getBoolean("saved_status")) {

                    } else ((LoadingCall) getActivity()).hideLoadingImage();

                    hideHUD();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    private void addVehicleImages() {

        Log.e("addDriverLicence", "exe");

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image1", imagesBase64.get(5));
        tempParam.put("image2", imagesBase64.get(6));
        tempParam.put("image3", imagesBase64.get(7));
        tempParam.put("image4", imagesBase64.get(8));
//        tempParam.put("image5", imagesBase64.get(8));

        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_VEHICLE_IMAGES), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverLicence", result + "");
                    if (result.getBoolean("saved_status")) {

//                        showToast(dialog, "Registration Completed. Please login", 2);
                        hideHUD();
                        tinyDB.putBoolean("isNewUser",true);
                        startActivity(new Intent(getActivity(), Login.class));
                        getActivity().finishAffinity();

                    } else ((LoadingCall) getActivity()).hideLoadingImage();


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });
        ((LoadingCall) getActivity()).hideLoadingImage();
    }

    public void showHUD(String msg) {

        hud.setLabel(msg);
        if (hud.isShowing()) {
            hud.dismiss();
        }
        hud.show();
    }

    public void hideHUD() {
        if (hud.isShowing()) {
            hud.dismiss();
        }
    }

}