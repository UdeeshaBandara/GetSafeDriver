package lk.hd192.getsafedriver;

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
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.activities.MultiCameraActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class AddDriverFourth extends Fragment {

    public ImageView imageDriver, imgLicOne, imgLicTwo, imgIdOne, imgIdTwo;
    ArrayList<Image> imagesList, imagesToUpload;
    Boolean fromIdOne = false, fromIdTwo = false, fromLicOne = false, fromLicTwo = false, fromDriverPic = false;

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

        imageDriver = view.findViewById(R.id.image_driver);
        imgLicTwo = view.findViewById(R.id.img_lic_two);
        imgLicOne = view.findViewById(R.id.img_lic_one);
        imgIdOne = view.findViewById(R.id.img_id_one);
        imgIdTwo = view.findViewById(R.id.img_id_two);


        imageDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(1);
                fromDriverPic = true;
            }
        });

        imgLicTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
                fromLicTwo = true;
            }
        });
        imgIdTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
                fromIdTwo = true;
            }
        });
        imgLicOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
                fromLicOne = true;

            }
        });
        imgIdOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
                fromIdOne = true;
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
                encodeImage(imagesList.get(0).imagePath);
                fromDriverPic = false;
                if (imagesToUpload.size() > 0) {
                    imagesToUpload.remove(0);
                }
                imagesToUpload.add(0, imagesList.get(0));
                imagesList.clear();


            } else if (fromIdTwo || fromIdOne) {
                if (imagesList.size() == 2) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);
                    imgIdTwo.setImageURI(imagesList.get(1).uri);
                    if (fromIdTwo) fromIdTwo = false;
                    else if (fromIdOne) fromIdOne = false;

                    if (imagesToUpload.size() > 1)
                        imagesToUpload.remove(1);
                    if (imagesToUpload.size() > 2)
                        imagesToUpload.remove(2);
                    imagesToUpload.add(1, imagesList.get(0));
                    imagesToUpload.add(2, imagesList.get(1));
                } else if (fromIdOne) {
                    imgIdOne.setImageURI(imagesList.get(0).uri);
                    if (imagesToUpload.size() > 1)
                        imagesToUpload.remove(1);
                    imagesToUpload.add(1, imagesList.get(0));

                    fromIdOne = false;
                } else if (fromIdTwo) {

                    imgIdTwo.setImageURI(imagesList.get(0).uri);
                    fromIdTwo = false;
                    if (imagesToUpload.size() > 2)
                        imagesToUpload.remove(2);
                    imagesToUpload.add(2, imagesList.get(0));
                }

//                    imgIdOne.setImageURI(imagesList.get(0).uri);


                imagesList.clear();
            } else if (fromLicTwo || fromLicOne) {
                if (imagesList.size() == 2) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    imgLicTwo.setImageURI(imagesList.get(1).uri);
                    if (fromLicOne) fromLicOne = false;
                    else if (fromLicTwo) fromLicTwo = false;
                    if (imagesToUpload.size() > 3)
                        imagesToUpload.remove(3);
                    if (imagesToUpload.size() > 4)
                        imagesToUpload.remove(4);
                    imagesToUpload.add(3, imagesList.get(0));
                    imagesToUpload.add(4, imagesList.get(1));
                } else if (fromLicOne) {
                    imgLicOne.setImageURI(imagesList.get(0).uri);
                    fromLicOne = false;
                    if (imagesToUpload.size() > 3)
                        imagesToUpload.remove(3);
                    imagesToUpload.add(3, imagesList.get(0));
                } else if (fromLicTwo) {
                    imgLicTwo.setImageURI(imagesList.get(0).uri);
                    fromLicTwo = false;
                    if (imagesToUpload.size() > 4)
                        imagesToUpload.remove(4);
                    imagesToUpload.add(4, imagesList.get(0));

                }


            }
            imagesList.clear();

        } catch (Exception e) {

        }


        Log.e("img", imagesList + "");


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
        return encImage;

    }

}