package com.techsamuel.roadsideprovider.activity;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepOne;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener{

    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    LocationEngineCallback<LocationEngineResult> locationEngineCallback;
    Marker clickOnMapMaker;
    Marker providerStoreMarker;
    LinearLayout lytStatus;
    Button btnUpdateLocation;
    LatLng selectedLocation;
    RelativeLayout lytChat;
    TextView txtStatus;
    ImageView imgCircle;
    TextView txtCircle;
    LatLng lastLocation=null;
    public static ProviderModel providerModel;
    FloatingActionButton floatingActionButton;
    Style mapboxStyle;
    boolean isActivated;
    TextView providerName;
    ImageView providerImage;
    LinearLayout lytWallet;
    LinearLayout lytCurrentOrders;
    LinearLayout lytPreviousOrders;
    LinearLayout lytMessage;
    LinearLayout lytLanguages;
    LinearLayout lytNotification;
    LinearLayout lytAbout;
    LinearLayout lytTerms;
    LinearLayout lytFaq;
    LinearLayout lytContact;
    LinearLayout lytRate;
    LinearLayout lytExit;
    TextView balance;
    ImageButton logout;
    LinearLayout lytProfile;
    boolean zoomOwnLocation=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providerModel=AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,"");
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //Tools.hideSystemUI(this);
        init();
    }



    private void init(){
        CommonRequests.updateFcmToServer(this);
        locationEngineCallback=new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult locationEngineResult) {
                lastLocation=new LatLng(locationEngineResult.getLastLocation().getLatitude(),locationEngineResult.getLastLocation().getLongitude());
                if(providerModel.getData().get(0).getLatitude().equals("0")&&providerModel.getData().get(0).getLongitude().equals("0")){
                        CommonRequests.updateLocationToServer(MainActivity.this,lastLocation);
                    }

            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Tools.showToast(MainActivity.this,"Location Updates Failed, please turn on gps");

            }
        };
        initNavigationMenu();
        lytStatus=findViewById(R.id.lyt_status);
        btnUpdateLocation=findViewById(R.id.btn_update_location);
        floatingActionButton=findViewById(R.id.fab);
        txtCircle=findViewById(R.id.txt_circle);
        txtStatus=findViewById(R.id.txt_status);
        lytChat=findViewById(R.id.lyt_chat);
        imgCircle=findViewById(R.id.img_circle);
        providerName=findViewById(R.id.provider_name);
        providerImage=findViewById(R.id.profile_image);
        lytWallet=findViewById(R.id.lyt_wallet);
        lytCurrentOrders=findViewById(R.id.lyt_current_orders);
        lytPreviousOrders=findViewById(R.id.lyt_previous_orders);
        lytMessage=findViewById(R.id.lyt_message);
        lytNotification=findViewById(R.id.lyt_notifications);
        lytAbout=findViewById(R.id.lyt_about);
        lytTerms=findViewById(R.id.lyt_terms);
        lytFaq=findViewById(R.id.lyt_faq);
        lytContact=findViewById(R.id.lyt_contact);
        lytRate=findViewById(R.id.lyt_rate);
        lytExit=findViewById(R.id.lyt_exit);
        balance=findViewById(R.id.balance);
        logout=findViewById(R.id.logout);
        lytProfile=findViewById(R.id.lyt_profile);

        if(providerModel.getData().get(0).getStatus().equals("0")){
            isActivated=false;
            txtCircle.setText("Not Activated");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_yellow);
            txtStatus.setText("Waiting...");
        }else if(providerModel.getData().get(0).getStatus().equals("1")){
            txtCircle.setText("Online");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_green);
            txtStatus.setText("Go to Offline");
            isActivated=true;
        }else{
            txtCircle.setText("Offline");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_red);
            txtStatus.setText("Go to Online");
            isActivated=true;
        }

        providerName.setText(providerModel.getData().get(0).getStoreName());
                 Glide
                .with(this)
                .load(Config.BASE_URL+providerModel.getData().get(0).getStorePhoto())
                .centerCrop()
                .placeholder(R.drawable.profile_photo)
                .into(providerImage);


        lytStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isActivated){
                    Tools.showToast(MainActivity.this,"CLicked");
                    //Do Something
                }else{
                    Tools.showToast(MainActivity.this,"Please wait until admin activate your account");
                }

            }
        });
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonRequests.updateLocationToServer(MainActivity.this,selectedLocation);
                markStoreLocation(selectedLocation);

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableLocationComponent(mapboxStyle);
            }
        });
        lytExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApp();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutApp();
            }
        });
        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    private void logoutApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.logout_app)
                .setMessage(R.string.logout_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AppSharedPreferences.init(MainActivity.this);
                        AppSharedPreferences.clear();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, RegisterStepOne.class));
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void exitApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.exit_app)
                .setMessage(R.string.exit_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void markStoreLocation(LatLng latLng){
            IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
            Icon icon = iconFactory.fromResource(R.drawable.green_marker);
            MarkerOptions providerStoreMarkerOptions = new MarkerOptions().position(latLng).title("Provider Store Location").icon(icon);
            if(providerStoreMarker!=null){
                mapboxMap.removeMarker(providerStoreMarker);
            }
            providerStoreMarker=mapboxMap.addMarker(providerStoreMarkerOptions);

    }

    private void initNavigationMenu() {
        ImageView drawerIcon=findViewById(R.id.drawer_icon);

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        // open drawer at start

    }

    private void setButtonInvisible(){
       Handler handler=new Handler();
       Runnable runnable=new Runnable() {
           @Override
           public void run() {
               Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout); //the above transition
               btnUpdateLocation.startAnimation(fadeout);
               btnUpdateLocation.setVisibility(View.GONE);

           }
       };
       handler.postDelayed(runnable,Config.LOCATION_BUTTON_INVISIBLE_TIME);

    }
    private void setButtonVisible(){
        btnUpdateLocation.setVisibility(View.VISIBLE);

    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        LatLng providerStoreLatLng=new LatLng(Double.valueOf(providerModel.getData().get(0).getLatitude()),Double.valueOf(providerModel.getData().get(0).getLongitude()));
        markStoreLocation(providerStoreLatLng);
        @SuppressLint("Range")
        CameraPosition cameraPosition;
        cameraPosition=new CameraPosition.Builder().target(providerStoreLatLng).zoom(12.00).tilt(20.00).build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
        zoomOwnLocation=true;


        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //enableLocationComponent(style);
                mapboxStyle=style;
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        if(clickOnMapMaker!=null){
                            mapboxMap.removeMarker(clickOnMapMaker);
                        }
                       MarkerOptions mapClickMarker = new MarkerOptions().position(point).title("Set Location");
                       selectedLocation=point;
                       clickOnMapMaker=mapboxMap.addMarker(mapClickMarker);
                       setButtonVisible();
                       setButtonInvisible();
                       return true;
                    }
                });


            }
        });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.getLocationEngine().getLastLocation(locationEngineCallback);
//            CameraPosition cameraPosition;
//            LatLng providerStoreLatLng=new LatLng(Double.valueOf(providerModel.getData().get(0).getLatitude()),Double.valueOf(providerModel.getData().get(0).getLongitude()));
//            if(zoomOwnLocation){
//                cameraPosition=new CameraPosition.Builder().target(providerStoreLatLng).zoom(12.00).tilt(20.00).build();
//                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
//                zoomOwnLocation=false;
//            }else{
//                cameraPosition=new CameraPosition.Builder().target(providerStoreLatLng).zoom(0.00).tilt(0.00).build();
//                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
//                zoomOwnLocation=true;
//            }

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, permissionsToExplain.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}