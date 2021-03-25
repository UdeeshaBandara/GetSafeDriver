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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lk.hd192.getsafedriver.Utils.GetSafeDriverBase;
import lk.hd192.getsafedriver.Utils.NonSwappableViewPager;

public class Register extends GetSafeDriverBase implements AddDriverFirst.RegisterCallBack, AddDriverSecond.RegisterCallSecond, AddDriverThird.RegisterCallBack,AddDriverFourth.LoadingCall {
    private NonSwappableViewPager nonSwappableViewPager;
    Intent intent;

    ArrayList<Image> imagesList;
    LottieAnimationView driverAnimation, locationAnimation, doneAnimation, vanAnimation, images, btnNext, btn_done;
    AddDriverFirst addDriverFirst;
    AddDriverSecond addDriverSecond;
    AddDriverThird addDriverThird;
    public AddDriverFourth addDriverFourth;
    TextView txtSubHeading;
    public static boolean firstCompleted = false, secondCompleted = false;
    int currentPage = 1;
    View view;
    LottieAnimationView loading;


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
        vanAnimation = findViewById(R.id.van_animation);
        locationAnimation = findViewById(R.id.location_animation);
        images = findViewById(R.id.images_animation);
        btn_done = findViewById(R.id.btn_done);

        loading = findViewById(R.id.loading);
        view = findViewById(R.id.disable_layout);

        nonSwappableViewPager.setOffscreenPageLimit(1);
        setupViewPager(nonSwappableViewPager);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage == 4) {

                    try {
                        addDriverFourth.convertToBase64AndUpload();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }}
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPage == 1)


                    addDriverFirst.validateFields();
//                    showPageTwo();

                else if (currentPage == 2)

                    addDriverSecond.validateFields();


                else if (currentPage == 3) {

                    addDriverThird.validateFields();




//                        txtSubHeading.setText("Vehicle Details");
//                        vanAnimation.setVisibility(View.VISIBLE);
//                        locationAnimation.setVisibility(View.GONE);
//                        driverAnimation.setVisibility(View.GONE);
//                        currentPage = 4;

//                    }


                }

            }
        });

    }

    @Override
    public void showPageTwo() {

        nonSwappableViewPager.setCurrentItem(1);
//                        txtSubHeading.setText("Location Details");
        driverAnimation.setVisibility(View.GONE);
        doneAnimation.setVisibility(View.GONE);
        locationAnimation.setVisibility(View.VISIBLE);
        currentPage = 2;


    }


    @Override
    public void showPageFour() {
        nonSwappableViewPager.setCurrentItem(3);
//                        txtSubHeading.setText("Vehicle Details");
        images.setVisibility(View.VISIBLE);
        locationAnimation.setVisibility(View.GONE);
        vanAnimation.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        currentPage = 4;
        driverAnimation.setVisibility(View.GONE);

        btn_done.setVisibility(View.VISIBLE);

    }

    @Override
    public void showPageThree() {
        Log.e("page three", "exe");
        nonSwappableViewPager.setCurrentItem(2);
//                        txtSubHeading.setText("Vehicle Details");
        vanAnimation.setVisibility(View.VISIBLE);
        locationAnimation.setVisibility(View.GONE);
        driverAnimation.setVisibility(View.GONE);
        currentPage = 3;

    }

    @Override
    public void showLoadingImage() {
        view.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        loading.playAnimation();

    }

    @Override
    public void hideLoadingImage() {
        loading.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
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

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment == addDriverFourth)

                fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}