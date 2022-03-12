package com.techsamuel.roadsideprovider.activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepOne;
import com.techsamuel.roadsideprovider.adapter.PageAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseReferenceName;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseViewModel;
import com.techsamuel.roadsideprovider.listener.PageItemClickListener;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderRequest;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    boolean isActivated;
    TextView providerName;
    ImageView providerImage;
    LinearLayout lytWallet;
    LinearLayout lytCurrentOrders;
    LinearLayout lytPreviousOrders;
    LinearLayout lytMessage;
    LinearLayout lytLanguages;
    LinearLayout lytNotification;
    LinearLayout lytRate;
    LinearLayout lytExit;
    TextView balance;
    ImageButton logout;
    LinearLayout lytProfile;
    RecyclerView recyclerPage;
    GoogleMap mMap;

    Marker newStoreMarker = null;
    Marker previousStoreMarker = null;
    Marker currentLocationMarker=null;

    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSharedPreferences.init(this);
        providerModel=AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,"");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Tools.hideSystemUI(this);
        init(false);
        initView();
        initSideMenuItem();
        getNewOrder();
    }


    private void initSideMenuItem() {
        recyclerPage = findViewById(R.id.recycler_page);
        ApiInterface apiInterface = ApiServiceGenerator.createService(ApiInterface.class);
        Call<PageModel> call = apiInterface.getPagesByDevicyType(Config.DEVICE_TYPE, Config.LANG_CODE);
        call.enqueue(new Callback<PageModel>() {
            @Override
            public void onResponse(Call<PageModel> call, Response<PageModel> response) {
                //Tools.showToast(MainActivity.this,response.body().getMessage().toString());
                Log.d("PageAdapter", response.body().toString());
                if (response.body().getStatus() == Config.API_SUCCESS) {
                    recyclerPage.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    PageAdapter pageAdapter = new PageAdapter(MainActivity.this, response.body(), new PageItemClickListener() {
                        @Override
                        public void onItemClick(PageModel.Datum item) {
                            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                            intent.putExtra("title", item.getPageName());
                            intent.putExtra("url", item.getPageUrl());
                            intent.putExtra("content", item.getPageContent());
                            startActivity(intent);

                        }
                    });
                    recyclerPage.setAdapter(pageAdapter);
                } else {
                    Log.d("MainActivity", "Failed to update device information");
                }
            }

            @Override
            public void onFailure(Call<PageModel> call, Throwable t) {
                Log.d("MainActivity", t.getMessage().toString());

            }
        });
    }
    int counter=0;

    private void getNewOrder(){
        DatabaseViewModel databaseViewModel=new DatabaseViewModel();
        databaseViewModel.fetchOrderRequest();
        databaseViewModel.fetchedOrderRequest.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderRequest orderRequest = snapshot.getValue(OrderRequest.class);
                    if(orderRequest.getProvider_id()==Integer.parseInt(providerModel.getData().get(0).getId())){
                        if(!orderRequest.isAccepted()&&!orderRequest.isRejected()){
                            counter++;
                           // popUpOrderSheet(orderRequest);
                            new PopUpOrderSheet(MainActivity.this,orderRequest).show();

                        }
                    }
                }
                Log.d("counter",String.valueOf(counter));
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double providerLat=Double.valueOf(providerModel.getData().get(0).getLatitude());
        double providerLong=Double.valueOf(providerModel.getData().get(0).getLongitude());
        LatLng storeLocation = new LatLng(providerLat, providerLong);
        previousStoreMarker=mMap.addMarker(new MarkerOptions().position(storeLocation).title("Marker in Provider Store"));
        previousStoreMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(providerLat, providerLong), 12.0f));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                selectedLocation=latLng;
                if(newStoreMarker!=null){
                    newStoreMarker.remove();
                }
                newStoreMarker=mMap.addMarker(new MarkerOptions().position(latLng).title("Set Store Location Here"));
                setButtonVisible();
                setButtonInvisible();
            }
        });
    }


    class PopUpOrderSheet{
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;
        EditText serviceName;
        EditText orderType;
        EditText location;
        EditText paymentType;
        EditText approxMinute;
        EditText approxKm;
        Button acceptBtn;
        Button rejectBtn;
        Context context;
        OrderRequest orderRequest;
        public PopUpOrderSheet(Context context,OrderRequest orderRequest){
            this.context=context;
            this.orderRequest=orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.sheet_filter, null);
            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

            serviceName=view.findViewById(R.id.service_name);
            orderType=view.findViewById(R.id.order_type);
            location=view.findViewById(R.id.location);
            paymentType=view.findViewById(R.id.payment_type);
            approxMinute=view.findViewById(R.id.approx_minutes);
            approxKm=view.findViewById(R.id.approx_km);
            acceptBtn=view.findViewById(R.id.btn_accept);
            rejectBtn=view.findViewById(R.id.btn_reject);

        }
        public void show(){
            mBottomSheetDialog.show();
            serviceName.setText(orderRequest.getService_name());
            orderType.setText("Order type: "+orderRequest.getOrder_type());
            location.setText(Tools.getAdressFromLatLong(MainActivity.this,String.valueOf(orderRequest.getUserLat()),String.valueOf(orderRequest.getUserLong())));
            paymentType.setText("Payment type: "+orderRequest.getPayment_type());
            approxMinute.setText("Approx minutes: "+orderRequest.getTotalMinutes());
            approxKm.setText("Approx distance: "+orderRequest.getTotalKms()+" Km");
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAcceptOrRejectStatus(orderRequest, DatabaseReferenceName.ACCEPT,true,mBottomSheetDialog);
                }
            });

            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAcceptOrRejectStatus(orderRequest, DatabaseReferenceName.REJECT,true,mBottomSheetDialog);
                }
            });
        }



    }
    void changeAcceptOrRejectStatus(OrderRequest orderRequest,String type,boolean value,BottomSheetDialog mBottomSheetDialog){
        DatabaseViewModel databaseViewModel=new DatabaseViewModel();
        databaseViewModel.fetchOrderRequest();
        databaseViewModel.fetchedOrderRequest.observe(MainActivity.this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderRequest getRequest = snapshot.getValue(OrderRequest.class);
                    if(getRequest.getId()==orderRequest.getId()){
                        databaseViewModel.addAccepOrRejecttInDatabase(type,value,snapshot);
                        databaseViewModel.successAddAcceptOrReject.observe(MainActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                mBottomSheetDialog.cancel();
                            }
                        });
                    }

                }
            }
        });
    }

    void initView(){

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
        lytRate=findViewById(R.id.lyt_rate);
        lytExit=findViewById(R.id.lyt_exit);
        balance=findViewById(R.id.balance);
        logout=findViewById(R.id.logout);
        lytProfile=findViewById(R.id.lyt_profile);

        if(providerModel.getData().get(0).getApplication().equals("review")){
            isActivated=false;
            txtCircle.setText("In Review");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_yellow);
            txtStatus.setText("Waiting...");
        }else  if(providerModel.getData().get(0).getApplication().equals("rejected")){
            isActivated=false;
            txtCircle.setText("Rejected");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_red);
            txtStatus.setText("Please Resubmit");
        }
        else if(providerModel.getData().get(0).getApplication().equals("accepted")){
            if(providerModel.getData().get(0).getAvailability().equals("0")){
                txtCircle.setText("Offline");
                imgCircle.setImageResource(R.drawable.ic_baseline_lens_red);
                txtStatus.setText("Go to Online");

            }else if(providerModel.getData().get(0).getAvailability().equals("1")){
                txtCircle.setText("Online");
                imgCircle.setImageResource(R.drawable.ic_baseline_lens_green);
                txtStatus.setText("Go to Offline");

            }
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
                previousStoreMarker.remove();
                newStoreMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
                CommonRequests.updateLocationToServer(MainActivity.this,selectedLocation);
                //markStoreLocation(selectedLocation);

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init(true);
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
        lytWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,WalletActivity.class);
                startActivity(intent);
            }
        });
        lytCurrentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lytPreviousOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lytRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openMarketForRatings(MainActivity.this);
            }
        });
        lytMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void init(boolean locationBtn){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("DeviceLocation",location.toString());
                            // Logic to handle location object
                        updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,""),
                        Config.LANG_CODE,location.getLatitude(),
                        location.getLongitude(),AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                        AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(mMap!=null && locationBtn){
                            LatLng currentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                            currentLocationMarker=mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));
                        }

                        }else{
                         updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,""),
                        Config.LANG_CODE,0, 0,
                        AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                        AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }
                    }
                });


    }


    private void updateDeviceInformationToServer(String deviceType,String userType,String userId, String langCode, double latitude, double longitude,
                                                 String fcm, String device_id,String firebase_id) {
        String lat=String.valueOf(latitude);
        String lon=String.valueOf(longitude);
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.updateDeviceInformationToServer(deviceType,userType,userId,langCode,lat,lon,fcm,device_id,firebase_id);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                Log.d("Response",response.toString());
                Tools.showToast(MainActivity.this,response.body().getMessage().toString());
               Log.d("MainActivity",response.body().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Log.d("MainActivity","Device information updated successfully");
                    Tools.showToast(MainActivity.this,"Device information updated successfully");
                }else{
                    Log.d("MainActivity","Failed to update device information");
                    Tools.showToast(MainActivity.this,"Failed to update device information");
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("MainActivity",t.getMessage().toString());
                Tools.showToast(MainActivity.this,"Failed to update device information");
            }
        });

    }

    private void logoutApp(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.logout_app)
                .setMessage(R.string.logout_app_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, RegisterStepOne.class));
                        MainActivity.this.finish();
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
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}