package lk.hd192.getsafedriver;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;

import java.util.ArrayList;
import java.util.List;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.NonSwappableViewPager;

public class Register extends GetSafeDriverBase {
    private NonSwappableViewPager nonSwappableViewPager;
    Intent intent;

    ArrayList<Image> imagesList;
    LottieAnimationView driverAnimation, locationAnimation, doneAnimation, btnNext;
    AddDriverFirst addDriverFirst;
    AddDriverSecond addDriverSecond;
    AddDriverThird addDriverThird;
    public AddDriverFourth addDriverFourth;
    TextView txtSubHeading;
    public static boolean firstCompleted = false, secondCompleted = false;
    int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addDriverFirst = new AddDriverFirst();
        addDriverSecond = new AddDriverSecond();
        addDriverThird = new AddDriverThird();
        addDriverFourth = new AddDriverFourth();

        nonSwappableViewPager = findViewById(R.id.view_pager_container);
        btnNext = findViewById(R.id.btn_next);
        driverAnimation = findViewById(R.id.driver_animation);
        doneAnimation = findViewById(R.id.done_animation);
        locationAnimation = findViewById(R.id.location_animation);
        txtSubHeading = findViewById(R.id.txt_sub_heading);

        nonSwappableViewPager.setOffscreenPageLimit(1);
        setupViewPager(nonSwappableViewPager);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPage == 1) {
//                    addDriverFirst.validateFields();
//                    if (firstCompleted & currentPage == 1) {
//                        addDriverFirst.addKidBasicDetails();

                    nonSwappableViewPager.setCurrentItem(1);
                    txtSubHeading.setText("Location Details");
                    driverAnimation.setVisibility(View.GONE);
                    doneAnimation.setVisibility(View.GONE);
                    locationAnimation.setVisibility(View.VISIBLE);
                    currentPage = 2;
//                    }
                } else if (currentPage == 2) {

//                    addDriverSecond.validateFields();

//                    if (secondCompleted) {
//                        addDriverSecond.addKidLocationDetails();
//                        addDriverThird.updateFields();
                    nonSwappableViewPager.setCurrentItem(2);
                    txtSubHeading.setText("Vehicle Details");
                    doneAnimation.setVisibility(View.VISIBLE);
                    locationAnimation.setVisibility(View.GONE);
                    driverAnimation.setVisibility(View.GONE);
                    currentPage = 3;

//                    }
                } else if (currentPage == 3) {

//                    addDriverSecond.validateFields();

//                    if (secondCompleted) {
//                        addDriverSecond.addKidLocationDetails();
//                        addDriverThird.updateFields();
                    nonSwappableViewPager.setCurrentItem(3);
                    txtSubHeading.setText("Vehicle Details");
                    doneAnimation.setVisibility(View.VISIBLE);
                    locationAnimation.setVisibility(View.GONE);
                    driverAnimation.setVisibility(View.GONE);
                    currentPage = 4;

                    btnNext.setVisibility(View.GONE);
//                    }
                }

            }
        });

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);

        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public int getCount() {

            return 4;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter sectionsPager = new SectionsPagerAdapter(getSupportFragmentManager());

        sectionsPager.addFragment(addDriverFirst);
        sectionsPager.addFragment(addDriverSecond);
        sectionsPager.addFragment(addDriverThird);
        sectionsPager.addFragment(addDriverFourth);

        viewPager.setAdapter(sectionsPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("on ac","activity");
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment==addDriverFourth)

            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        Log.e("img", "result ok");
//        switch (requestCode) {
//            case Constants.TYPE_MULTI_CAPTURE:
//                imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
//                break;
//            case Constants.TYPE_MULTI_PICKER:
//                imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
//                break;
//        }
//        Log.e("img", imagesList + "");
////            imageDriver.setImageURI(Uri.parse(imagesList.get(0).imagePath));
//
//
//    }
}