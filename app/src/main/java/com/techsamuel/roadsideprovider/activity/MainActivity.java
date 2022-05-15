package com.techsamuel.roadsideprovider.activity;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.BuildConfig;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.activity.register.RegisterStepOne;
import com.techsamuel.roadsideprovider.adapter.PageAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseReferenceName;
import com.techsamuel.roadsideprovider.firebase_db.DatabaseViewModel;
import com.techsamuel.roadsideprovider.helper.DirectionsJSONParser;
import com.techsamuel.roadsideprovider.listener.PageItemClickListener;
import com.techsamuel.roadsideprovider.location_update.LocationUpdatesService;
import com.techsamuel.roadsideprovider.location_update.LocationUtils;
import com.techsamuel.roadsideprovider.model.ProviderLocation;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderRequest;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.CommonRequests;
import com.techsamuel.roadsideprovider.tools.Tools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener{

    LinearLayout lytStatus;
    Button btnUpdateLocation;
    LatLng selectedLocation;
    RelativeLayout lytChat;
    DrawerLayout drawerLayout;
    TextView txtStatus;
    ImageView imgCircle;
    TextView txtCircle;
    LatLng lastLocation = null;
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
    Marker currentLocationMarker = null;
    FusedLocationProviderClient fusedLocationClient;

    private MyReceiver myReceiver;

    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = "resPMain";
    PopUpOrderAcceptedSheet popUpOrderAcceptedSheet;
    PopUpTimerSheet popUpTimerSheet;
    ImageView btnShow;

    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        AppSharedPreferences.init(this);
        providerModel = AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL, "");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_main);
        if (LocationUtils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Tools.hideSystemUI(this);
        init(false);
        initView();
        initSideMenuItem();
        getNewOrder();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(LocationUtils.KEY_REQUESTING_LOCATION_UPDATES)) {
            sharedPreferences.getBoolean(LocationUtils.KEY_REQUESTING_LOCATION_UPDATES,
                    false);
        }
    }



    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                DatabaseViewModel databaseViewModel = new DatabaseViewModel();
                ProviderLocation providerLocation=new ProviderLocation(location.getLatitude(),location.getLongitude());
                databaseViewModel.addProviderLocationInDatabase(Config.ORDER_ID,providerLocation);
                databaseViewModel.successAddProviderLocation.observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {

                    }
                });
            }
        }


    }


    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                // Permission denied.

                Snackbar.make(
                        findViewById(R.id.drawer_layout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        getApplicationContext().getPackageName(), null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
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

    private void getNewProviderLocation(OrderRequest orderRequest){
        DatabaseViewModel databaseViewModel=new DatabaseViewModel();
        databaseViewModel.fetchProviderLocation(orderRequest.getId());
        databaseViewModel.fetchedProviderLocation.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                ProviderLocation providerLocation=dataSnapshot.getValue(ProviderLocation.class);
                drawArrivalRoute(providerLocation,orderRequest);

            }
        });
    }

    private void getNewOrder() {
        DatabaseViewModel databaseViewModel = new DatabaseViewModel();
        databaseViewModel.fetchOrderRequest();
        databaseViewModel.fetchedOrderRequest.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderRequest orderRequest = snapshot.getValue(OrderRequest.class);
                    if (orderRequest.getProvider_id() == Integer.parseInt(providerModel.getData().get(0).getId())) {
                        if (Tools.isNotExpire(orderRequest.getDate())) {
                            if (!orderRequest.isAccepted() && !orderRequest.isRejected()) {
                                new PopUpOrderRequestSheet(MainActivity.this, orderRequest).show();
                            } else if (orderRequest.isRejected() && !orderRequest.isAccepted()) {
                                //Order is Rejected
                            } else {
                                //Order is accepted and rejected both by malfunction
                            }

                        }
                        if (orderRequest.isAccepted() && !orderRequest.isRejected()&& !orderRequest.isMarkAsArrived()) {
                            //Order is Accepted
                            Config.ORDER_ID=orderRequest.getId();

                            getNewProviderLocation(orderRequest);
                            if(popUpOrderAcceptedSheet==null){
                                popUpOrderAcceptedSheet = new PopUpOrderAcceptedSheet(MainActivity.this, orderRequest);
                                popUpOrderAcceptedSheet.show();
                            }

                            if (!checkPermissions()) {
                                requestPermissions();
                            } else {
                                if(mService!=null){
                                    mService.requestLocationUpdates();
                                }

                            }
                        }
                        if(orderRequest.isMarkAsArrived()&&!orderRequest.isVehicleDetailsSaved()){
                            //Provider arrived
                            getNewProviderLocation(orderRequest);
                            PopUpUserVehicleDetailsSheet popUpUserVehicleDetailsSheet = null;
                            if(popUpUserVehicleDetailsSheet==null){
                                popUpUserVehicleDetailsSheet=new PopUpUserVehicleDetailsSheet(MainActivity.this,orderRequest);
                            }
                            popUpUserVehicleDetailsSheet.show();
                        }

                        if(orderRequest.isVehicleDetailsSaved()&&!orderRequest.isVehicleFullPhotoSaved()){
                            getNewProviderLocation(orderRequest);
                            if(orderRequest.getOrder_type().equals(Config.ORDER_TYPE_NONE)){
                                //
                                PopUpVehicleFullPhotoSheet popUpVehicleFullPhotoSheet=new PopUpVehicleFullPhotoSheet(MainActivity.this,orderRequest);
                                popUpVehicleFullPhotoSheet.show();
                            }else{
                                popUpTimerSheet=new PopUpTimerSheet(MainActivity.this,orderRequest);
                                popUpTimerSheet.show();
                            }
                           //User vehicle details saved
                            Tools.showToast(MainActivity.this,"Vehicle Details saved success");
                        }
                        if(orderRequest.isVehicleFullPhotoSaved()&&!orderRequest.isOrderPaid()){
                            getNewProviderLocation(orderRequest);
                            Tools.showToast(MainActivity.this,"Invoice sent to user");
                        }

                        if(orderRequest.isOrderPaid()&&!orderRequest.isCashReceived()){
                            getNewProviderLocation(orderRequest);
                            if(orderRequest.getPayment_type().equals(Config.PAYMENT_TYPE_CASH)){
                                PopUpPaymentConfirmationSheet popUpPaymentConfirmationSheet=new PopUpPaymentConfirmationSheet(MainActivity.this,orderRequest);
                                popUpPaymentConfirmationSheet.show();

                            }

                        }

                        if(orderRequest.isCashReceived()&&!orderRequest.isMarkAsCompleted()){
                            getNewProviderLocation(orderRequest);
                            Tools.showToast(MainActivity.this,"Waiting for mark as completed and rating and reviews");
                        }


                    }
                }

            }
        });
    }
    class PopUpPaymentConfirmationSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;

        Context context;
        OrderRequest orderRequest;
        LinearLayout payInvoice;

        public PopUpPaymentConfirmationSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.payment_confirmation_sheet, null);
            payInvoice=view.findViewById(R.id.pay_invoice);
            payInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.CASH_RECEIVED,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            mBottomSheetDialog.cancel();
                        }
                    });
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
            //  btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }


class PopUpVehicleFullPhotoSheet{
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    Context context;
    OrderRequest orderRequest;
    Button saveContinue;


        public PopUpVehicleFullPhotoSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.vehicle_full_photo_sheet, null);
            saveContinue=view.findViewById(R.id.save_continue);
            saveContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.VEHICLE_FULL_PHOTO_SAVED,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            mBottomSheetDialog.cancel();
                        }
                    });
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }
        private void show(){
            mBottomSheetDialog.show();
        }

}


    class PopUpTimerSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;
        Button navigationWithGoogleMaps;
        Button callTheUser;
        Context context;
        OrderRequest orderRequest;
        ImageView btnHide;
        ImageView timerPlayPause;
        TextView timerText;
        TextView timerTotal;
        boolean isPlaying=false;
        int seconds=0;
        boolean timerStopeedPermanent=false;


        public PopUpTimerSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.timer_sheet, null);
            navigationWithGoogleMaps = view.findViewById(R.id.navigation_with_google_maps);
            callTheUser = view.findViewById(R.id.call_the_use);
            btnHide=view.findViewById(R.id.btn_hide);

            timerPlayPause=view.findViewById(R.id.timer_play_pause);
            timerText=view.findViewById(R.id.timer_text);
            timerTotal=view.findViewById(R.id.timer_total);

            timerPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!timerStopeedPermanent){
                        if(!isPlaying){
                            timerPlayPause.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                            timerPlayPause.setColorFilter(ContextCompat.getColor(context, R.color.red_600), android.graphics.PorterDuff.Mode.SRC_IN);
                            timerText.setText("Stop timer");
                            isPlaying=true;

                        }else{
                            timerPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_24);
                            timerPlayPause.setColorFilter(ContextCompat.getColor(context, R.color.green_300), android.graphics.PorterDuff.Mode.SRC_IN);
                            timerText.setText("Timer finished");
                            isPlaying=false;
                            Tools.showToast(MainActivity.this,String.valueOf(seconds));
                            timerStopeedPermanent=true;
                        }
                        runTimer();
                    }

                }
            });
            navigationWithGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "http://maps.google.com/maps?saddr=" + lastLocation.latitude + "," + lastLocation.longitude + "&daddr=" + orderRequest.getUserLat() + "," + orderRequest.getUserLong();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }
            });
            callTheUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            btnHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.hide();
                    btnShow.setVisibility(View.VISIBLE);
                }
            });


            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public  void runTimer()
        {
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run()
                {
                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) / 60;
                    int secs = seconds % 60;
                    String time
                            = String
                            .format(Locale.getDefault(),
                                    "%d:%02d:%02d", hours,
                                    minutes, secs);
                    timerTotal.setText(time);
                    if (isPlaying) {
                        seconds++;
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }

        public void show() {
            mBottomSheetDialog.show();
            btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }

    class PopUpOrderAcceptedSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;
        Button navigationWithGoogleMaps;
        Button callTheUser;
        Button btnArrived;
        Context context;
        OrderRequest orderRequest;
        ImageView btnHide;
        EditText paymentType;

        public PopUpOrderAcceptedSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.order_accept_sheet, null);
            navigationWithGoogleMaps = view.findViewById(R.id.navigation_with_google_maps);
            callTheUser = view.findViewById(R.id.call_the_use);
            btnArrived = view.findViewById(R.id.btn_arrived);
            btnHide=view.findViewById(R.id.btn_hide);
            paymentType=view.findViewById(R.id.payment_type);

            paymentType.setText("Payment method"+": "+orderRequest.getPayment_type().toUpperCase());

            navigationWithGoogleMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "http://maps.google.com/maps?saddr=" + lastLocation.latitude + "," + lastLocation.longitude + "&daddr=" + orderRequest.getUserLat() + "," + orderRequest.getUserLong();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }
            });
            callTheUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            btnArrived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.MARK_AS_ARRIVED,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            popUpOrderAcceptedSheet.cancel();
                        }
                    });
                }
            });
            btnHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.hide();
                    btnShow.setVisibility(View.VISIBLE);
                }
            });


            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
            btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }

    class PopUpUserVehicleDetailsSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;

        Context context;
        OrderRequest orderRequest;
        SignaturePad signaturePad;
        Button clearButton;
        Button saveContinue;


        public PopUpUserVehicleDetailsSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.user_vehicle_details_sheet, null);

            signaturePad=view.findViewById(R.id.signature_pad);
            clearButton=view.findViewById(R.id.buttonClear);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signaturePad.clear();
                }
            });

            saveContinue=view.findViewById(R.id.save_continue);
            saveContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseViewModel databaseViewModel=new DatabaseViewModel();
                    databaseViewModel.addTrueFalseInDatabase(DatabaseReferenceName.VEHICLE_DETAILS_SAVED,true,orderRequest.getId());
                    databaseViewModel.successAddTrueFalseInDatabase.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            mBottomSheetDialog.cancel();

                        }
                    });
                }
            });


            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

        }

        public void show() {
            mBottomSheetDialog.show();
            btnShow.setVisibility(View.GONE);
        }

        public void hide() {
            mBottomSheetDialog.hide();
        }

        public void cancel() {
            mBottomSheetDialog.cancel();
        }
    }

    class PopUpOrderRequestSheet {
        private BottomSheetBehavior mBehavior;
        private BottomSheetDialog mBottomSheetDialog;
        private View bottom_sheet;
        EditText serviceName;
        EditText orderType;
        EditText location;
        EditText dropoffLocation;
        EditText paymentType;
        EditText approxMinute;
        EditText approxKm;
        Button acceptBtn;
        Button rejectBtn;
        Context context;
        OrderRequest orderRequest;
        ImageView closeBtn;

        public PopUpOrderRequestSheet(Context context, OrderRequest orderRequest) {
            this.context = context;
            this.orderRequest = orderRequest;
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.order_request_sheet, null);
            mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mBottomSheetDialog.setCancelable(false);

            serviceName = view.findViewById(R.id.service_name);
            orderType = view.findViewById(R.id.order_type);
            location = view.findViewById(R.id.location);
            paymentType = view.findViewById(R.id.payment_type);
            approxMinute = view.findViewById(R.id.approx_minutes);
            approxKm = view.findViewById(R.id.approx_km);
            acceptBtn = view.findViewById(R.id.btn_accept);
            rejectBtn = view.findViewById(R.id.btn_reject);
            closeBtn = view.findViewById(R.id.close_btn);
            dropoffLocation=view.findViewById(R.id.location_dropoff);

        }

        public void show() {
            mBottomSheetDialog.show();
            serviceName.setText(orderRequest.getService_name());
            orderType.setText("Order type: " + orderRequest.getOrder_type());
            location.setText(Tools.getAdressFromLatLong(MainActivity.this, String.valueOf(orderRequest.getUserLat()), String.valueOf(orderRequest.getUserLong())));
            if(orderRequest.getOrder_type()==Config.ORDER_TYPE_DELIVERY){
                dropoffLocation.setText(Tools.getAdressFromLatLong(MainActivity.this, String.valueOf(orderRequest.getDropOfffLat()), String.valueOf(orderRequest.getDropOffLong())));
            }else{
                dropoffLocation.setVisibility(View.GONE);
            }

            paymentType.setText("Payment type: " + orderRequest.getPayment_type());
            approxMinute.setText("Approx minutes: " + orderRequest.getTotalMinutes());
            approxKm.setText("Approx distance: " + orderRequest.getTotalKms() + " Km");
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAcceptOrRejectStatus(orderRequest, DatabaseReferenceName.ACCEPT, true, mBottomSheetDialog);
                }
            });

            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAcceptOrRejectStatus(orderRequest, DatabaseReferenceName.REJECT, true, mBottomSheetDialog);
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.cancel();
                }
            });
        }

    }

    Marker providerMarkerRoute;
    Marker userMarkerRoute;
    List<Marker> markersRoute;
    boolean isDrawArrivalRoute=false;

    private void drawArrivalRoute(ProviderLocation providerLocation,OrderRequest orderRequest) {
        // Getting URL to the Google Directions API
        if(providerLocation==null){
            mOrigin=new LatLng(orderRequest.getProviderLat(),orderRequest.getProviderLong());
        }else {
            mOrigin = new LatLng(providerLocation.getProviderLat(), providerLocation.getProviderLong());
        }

        mDestination=new LatLng(orderRequest.getUserLat(),orderRequest.getUserLong());

        if(providerMarkerRoute!=null){
            providerMarkerRoute.remove();
        }
        if(userMarkerRoute!=null){
            userMarkerRoute.remove();
        }

        providerMarkerRoute=mMap.addMarker(new MarkerOptions().position(mOrigin).title("Marker in current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_car)));
        userMarkerRoute=mMap.addMarker(new MarkerOptions().position(mDestination).title("Marker in User location").icon(BitmapDescriptorFactory.fromResource(R.drawable.user_car)));

        if(!isDrawArrivalRoute){
            if(markersRoute==null){
                markersRoute=new ArrayList<>();
            }

            if(markersRoute.size()>0){
                markersRoute.clear();
            }
            markersRoute.add(providerMarkerRoute);
            markersRoute.add(userMarkerRoute);
            autoZoomMap(markersRoute);


            String url = getDirectionsUrl(mOrigin, mDestination);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            Log.d("MainActivity",url);
            downloadTask.execute(url);
            isDrawArrivalRoute=true;
        }


    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + BuildConfig.MAPS_API_KEY;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
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

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }


    private void autoZoomMap(List<Marker> markers){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        if(markers.size()>0){
            System.out.println(markers.size());
            for (Marker m : markers) {
                b.include(m.getPosition());
            }
            LatLngBounds bounds = b.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,200);
            mMap.animateCamera(cu);
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (!checkPermissions()) {
            requestPermissions();
        }

        double providerLat = Double.valueOf(providerModel.getData().get(0).getLatitude());
        double providerLong = Double.valueOf(providerModel.getData().get(0).getLongitude());
        LatLng storeLocation = new LatLng(providerLat, providerLong);
        previousStoreMarker = mMap.addMarker(new MarkerOptions().position(storeLocation).title("Marker in Provider Store"));
        previousStoreMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(providerLat, providerLong), 12.0f));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                selectedLocation = latLng;
                if (newStoreMarker != null) {
                    newStoreMarker.remove();
                }
                newStoreMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Set Store Location Here"));
                setButtonVisible();
                setButtonInvisible();
            }
        });


    }




    void changeAcceptOrRejectStatus(OrderRequest orderRequest, String type, boolean value, BottomSheetDialog mBottomSheetDialog) {

        ApiInterface apiInterface = ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call = apiInterface.changeAcceptOrRejectStatus(String.valueOf(orderRequest.getId()), type, value);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                if (response.body().getStatus() == Config.API_SUCCESS) {
                    DatabaseViewModel databaseViewModel = new DatabaseViewModel();
                    databaseViewModel.fetchOrderRequest();
                    databaseViewModel.fetchedOrderRequest.observe(MainActivity.this, new Observer<DataSnapshot>() {
                        @Override
                        public void onChanged(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                OrderRequest getRequest = snapshot.getValue(OrderRequest.class);
                                if (getRequest.getId() == orderRequest.getId()) {
                                    databaseViewModel.addAccepOrRejecttInDatabase(type, value, snapshot);
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
                } else {
                    Tools.showToast(MainActivity.this, "Error in changing " + type + " status");
                }
            }

            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                Log.d("MainActivity", t.toString());
                Tools.showToast(MainActivity.this, "Something wrong try again later");

            }
        });


    }

    boolean isActivates = false;

    void initView() {
        initNavigationMenu();
        lytStatus = findViewById(R.id.lyt_status);
        btnUpdateLocation = findViewById(R.id.btn_update_location);
        floatingActionButton = findViewById(R.id.fab);
        txtCircle = findViewById(R.id.txt_circle);
        txtStatus = findViewById(R.id.txt_status);
        lytChat = findViewById(R.id.lyt_chat);
        imgCircle = findViewById(R.id.img_circle);
        providerName = findViewById(R.id.provider_name);
        providerImage = findViewById(R.id.profile_image);
        lytWallet = findViewById(R.id.lyt_wallet);
        lytCurrentOrders = findViewById(R.id.lyt_current_orders);
        lytPreviousOrders = findViewById(R.id.lyt_previous_orders);
        lytMessage = findViewById(R.id.lyt_message);
        lytNotification = findViewById(R.id.lyt_notifications);
        lytRate = findViewById(R.id.lyt_rate);
        lytExit = findViewById(R.id.lyt_exit);
        balance = findViewById(R.id.balance);
        logout = findViewById(R.id.logout);
        lytProfile = findViewById(R.id.lyt_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        btnShow=findViewById(R.id.btn_show);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popUpOrderAcceptedSheet!=null){
                    popUpOrderAcceptedSheet.show();

                }
                if(popUpTimerSheet!=null){
                    popUpTimerSheet.show();

                }
            }
        });

        if (providerModel.getData().get(0).getApplication().equals("review")) {
            isActivated = false;
            txtCircle.setText("In Review");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_yellow);
            txtStatus.setText("Waiting...");
        } else if (providerModel.getData().get(0).getApplication().equals("rejected")) {
            isActivated = false;
            txtCircle.setText("Rejected");
            imgCircle.setImageResource(R.drawable.ic_baseline_lens_red);
            txtStatus.setText("Please Resubmit");
        } else if (providerModel.getData().get(0).getApplication().equals("accepted")) {
            if (providerModel.getData().get(0).getAvailability().equals("0")) {
                txtCircle.setText("Offline");
                imgCircle.setImageResource(R.drawable.ic_baseline_lens_red);
                txtStatus.setText("Go to Online");

            } else if (providerModel.getData().get(0).getAvailability().equals("1")) {
                txtCircle.setText("Online");
                imgCircle.setImageResource(R.drawable.ic_baseline_lens_green);
                txtStatus.setText("Go to Offline");

            }
            isActivated = true;

        }

        providerName.setText(providerModel.getData().get(0).getStoreName());
        Glide
                .with(this)
                .load(Config.BASE_URL + providerModel.getData().get(0).getStorePhoto())
                .centerCrop()
                .placeholder(R.drawable.profile_photo)
                .into(providerImage);


        lytStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isActivated) {
                    Tools.showToast(MainActivity.this, "CLicked");
                    //Do Something
                } else {
                    Tools.showToast(MainActivity.this, "Please wait until admin activate your account");
                }

            }
        });
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousStoreMarker.remove();
                newStoreMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker));
                CommonRequests.updateLocationToServer(MainActivity.this, selectedLocation);
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
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        lytWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WalletActivity.class);
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
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
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
                            lastLocation=new LatLng(location.getLatitude(),location.getLongitude());
                            // Logic to handle location object
                            if(mMap!=null && !locationBtn){
                            updateDeviceInformationToServer(Config.DEVICE_TYPE,Config.USER_TYPE,AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,""),
                                    Config.LANG_CODE,location.getLatitude(),
                                    location.getLongitude(),AppSharedPreferences.read(Config.SHARED_PREF_KEY_FCM,""),
                                    AppSharedPreferences.read(Config.SHARED_PREF_DEVICE_ID,""),FirebaseAuth.getInstance().getCurrentUser().getUid());

                        }else if(locationBtn){
                            LatLng currentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                            currentLocationMarker=mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_car)));
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
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        //floatingActionButton.performClick();
        // Restore the state of the buttons when the activity (re)launches.
       LocationUtils.requestingLocationUpdates(this);
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);



    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();

    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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