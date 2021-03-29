package lk.hd192.getsafedriver;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.FileOutputStream;
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
    String[] imagesToUpload = new String[9];
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

                imagesToUpload[0] = compressImage(imagesList.get(0).uri.toString());
//                saveBitmapToFile(new File(imagesToUpload[0].imagePath));
//                compressImage(imagesToUpload[0].uri.toString());
                Log.e("1 imagesToUpload", imagesToUpload.length + "");
                imagesList.clear();


            } else if (fromIdTwo || fromIdOne) {
                if (imagesList.size() == 2) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);
                    imgIdTwo.setImageURI(imagesList.get(1).uri);
                    if (fromIdTwo) fromIdTwo = false;
                    else if (fromIdOne) fromIdOne = false;

                    imagesToUpload[1] = compressImage(imagesList.get(0).uri.toString());
                    imagesToUpload[2] = compressImage(imagesList.get(1).uri.toString());

                } else if (fromIdOne) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);

                    imagesToUpload[1] = compressImage(imagesList.get(0).uri.toString());

                    fromIdOne = false;
                } else if (fromIdTwo) {

                    imgIdTwo.setImageURI(imagesList.get(0).uri);
                    fromIdTwo = false;
                    imagesToUpload[2] = compressImage(imagesList.get(0).uri.toString());

                }

//                    imgIdOne.setImageURI(imagesList.get(0).uri);


                imagesList.clear();
            } else if (fromLicTwo || fromLicOne) {
                if (imagesList.size() == 2) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    imgLicTwo.setImageURI(imagesList.get(1).uri);
                    if (fromLicOne) fromLicOne = false;
                    else if (fromLicTwo) fromLicTwo = false;


                    imagesToUpload[3] = compressImage(imagesList.get(0).uri.toString());
                    imagesToUpload[4] = compressImage(imagesList.get(1).uri.toString());
                } else if (fromLicOne) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    fromLicOne = false;

                    imagesToUpload[3] = compressImage(imagesList.get(0).uri.toString());
                } else if (fromLicTwo) {
                    imgLicTwo.setImageURI(imagesList.get(0).uri);
                    fromLicTwo = false;

                    imagesToUpload[4] = compressImage(imagesList.get(0).uri.toString());

                }

                imagesList.clear();
            } else if (fromVehicleOne) {


                if (imagesList.size() == 4) {

                    img_vehicle_one.setImageURI(imagesList.get(0).uri);
                    img_vehicle_three.setImageURI(imagesList.get(2).uri);
                    img_vehicle_two.setImageURI(imagesList.get(1).uri);
                    img_vehicle_four.setImageURI(imagesList.get(3).uri);

                    imagesToUpload[5] = compressImage(imagesList.get(0).uri.toString());
                    imagesToUpload[6] = compressImage(imagesList.get(1).uri.toString());
                    imagesToUpload[7] = compressImage(imagesList.get(2).uri.toString());
                    imagesToUpload[8] = compressImage(imagesList.get(3).uri.toString());
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
//        compressImage(imagesList.get(0).uri.toString());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);


//        outputStreamWriter.close();
        //Base64.de
        return "data:image/jpg;base64," + encImage;

    }

    public void convertToBase64AndUpload() throws IOException {

        boolean validated = true;

        for (String image : imagesToUpload) {
            if (image == null) {

                showToast(dialog, "Please add all photos", 0);
                validated = false;
                break;
            } else {


            }
        }
        if (validated) {
            Log.e("validated ", "ok");
            for (int g = 0; g < imagesToUpload.length; g++) {

                imagesBase64.add(g, encodeImage(imagesToUpload[g]));
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
//        Log.e("addDriverImage", "exe");
        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image", imagesBase64.get(0));

        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_IMAGE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverImage", result + "");

                    if (result.getBoolean("saved_status")) {
//                        hideHUD();
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
//        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_NIC_FRONT), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverIdFront", result + "");

                    if (result.getBoolean("saved_status")) {

                    } else ((LoadingCall) getActivity()).hideLoadingImage();
//                    hideHUD();

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

//        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_NIC_BACK), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverIdBack", result + "");
                    if (result.getBoolean("saved_status")) {

                    }
//                    hideHUD();
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

//        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_LICENCE), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addDriverLicence", result + "");
                    if (result.getBoolean("saved_status")) {

                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }
        });

    }

    private void addVehicleImages() {

        Log.e("addVehicleImages", "exe");

        HashMap<String, String> tempParam = new HashMap<>();
        tempParam.put("id", tinyDB.getString("driver_id"));
        tempParam.put("image1", imagesBase64.get(5));
        tempParam.put("image2", imagesBase64.get(6));
        tempParam.put("image3", imagesBase64.get(7));
        tempParam.put("image4", imagesBase64.get(8));
//        tempParam.put("image5", imagesBase64.get(8));

//        showHUD("Saving");
        getSafeDriverServices.networkJsonRequestWithoutHeader(getActivity(), tempParam, getString(R.string.BASE_URL) + getString(R.string.DRIVER_VEHICLE_IMAGES), 2, new VolleyJsonCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

                try {
                    Log.e("res addVehicleImages", result + "");
                    if (result.getBoolean("saved_status")) {

//                        showToast(dialog, "Registration Completed. Please login", 2);
                        hideHUD();
                        tinyDB.putBoolean("isNewUser", true);
                        startActivity(new Intent(getActivity(), Login.class));
                        getActivity().finishAffinity();

                    } else hideHUD();
//                    else ((LoadingCall) getActivity()).hideLoadingImage();


                } catch (JSONException ex) {
                    hideHUD();
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


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}