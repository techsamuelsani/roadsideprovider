package com.techsamuel.roadsideprovider.activity;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepOne;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.List;


public class WizardActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {

    private static final int MAX_STEP = 4;
    private static final int PERMISSION_REQUEST_CODE =101 ;


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button btn_got_it;
    private String title_array[] = {
            "Demo title 1",
            "Demo title 2",
            "Demo title 3",
            "Demo title 4"
    };
    private String description_array[];
    private int about_images_array[] = {
            R.drawable.ic_baseline_attractions_24,
            R.drawable.ic_baseline_navigation_24,
            R.drawable.ic_baseline_assignment_turned_in_24,
            R.drawable.ic_baseline_notifications_24
    };
    private int color_array[] = {
            R.color.red_600,
            R.color.blue_grey_600,
            R.color.purple_600,
            R.color.deep_orange_600
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.hideSystemUI(this);
        AppSharedPreferences.init(this);
        setContentView(R.layout.activity_wizard);

        description_array= new String[]{
                getString(R.string.medium_lorem_ipsum),
                getString(R.string.medium_lorem_ipsum),
                getString(R.string.medium_lorem_ipsum),
                getString(R.string.medium_lorem_ipsum),
        };


        boolean isPermissionDenied=AppSharedPreferences.read("permissionDenied",true);

        if(isPermissionDenied==false){
            WizardActivity.this.finish();
//            Intent intent=new Intent(WizardActivity.this, UserLoginActivity.class);
//            startActivity(intent);
            checkAuth();
        }

        initComponent();
    }

    private void checkAuth() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            CommonRequests.goToStep(this,firebaseUser);
        }else{
            Intent intent=new Intent(WizardActivity.this, RegisterStepOne.class);
            startActivity(intent);
        }
    }

    private void initComponent() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btn_got_it = (Button) findViewById(R.id.btn_got_it);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btn_got_it.setVisibility(View.GONE);
        btn_got_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askPermission();

            }
        });


    }


//    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//    <uses-permission android:name="android.permission.INTERNET" />
//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    <uses-permission android:name="android.permission.CAMERA" />
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//    <uses-permission android:name="android.permission.CALL_PHONE" />


    private void askPermission() {
        new AskPermission.Builder(WizardActivity.this)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE)
                .setCallback(WizardActivity.this)
                .setErrorCallback(WizardActivity.this)
                .request(PERMISSION_REQUEST_CODE);


    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.overlay_dark_30), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.grey_10), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
            if (position == title_array.length - 1) {
                btn_got_it.setVisibility(View.VISIBLE);
            } else {
                btn_got_it.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onPermissionsGranted(int requestCode) {
        AppSharedPreferences.write("permissionDenied",false);
        WizardActivity.this.finish();
//        Intent intent=new Intent(WizardActivity.this,UserLoginActivity.class);
//        startActivity(intent);
        checkAuth();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Tools.showToast(WizardActivity.this,"Please Allow All Permissions");
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WizardActivity.this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WizardActivity.this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_wizard, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);
            ((RelativeLayout) view.findViewById(R.id.lyt_parent)).setBackgroundColor(getResources().getColor(color_array[position]));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

