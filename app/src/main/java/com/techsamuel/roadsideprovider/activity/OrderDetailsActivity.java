package com.techsamuel.roadsideprovider.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.fragment.ImageViewFragment;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.OrderPagePhotoGridAdapter;
import com.techsamuel.roadsideprovider.adapter.OrderPageServiceAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.helper.AllOrderStatus;
import com.techsamuel.roadsideprovider.helper.Status;
import com.techsamuel.roadsideprovider.listener.OrderPhotoItemClickListener;
import com.techsamuel.roadsideprovider.listener.OrderServiceItemClickListener;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
//import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.SpacingItemDecoration;
import com.techsamuel.roadsideprovider.tools.Tools;
import com.techsamuel.roadsideprovider.tools.ViewAnimation;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {


    OrderModel orderModel;
    SettingsModel settingsModel;
    //UserModel userModel;
    MapView mapView;
    MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
   // String userId;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    Button paymentFab;
    TextView serviceName;
    TextView storeName;
    TextView servicePrice;
    TextView storeLocation;
    TextView orderDescription;
    TextView distanceCost;
    TextView distanceCostAmount;
    TextView serviceCharge;
    TextView serviceChargeAmount;
    TextView totalCost;
    TextView totalCostAmount;
    TextView totalServiceCostAmount;
    RecyclerView recyclerService;
    TextView orderStatus;
    RecyclerView recyclerView;
    BeautifulProgressDialog beautifulProgressDialog;
    Button btnCancelOrder;
    Button btnAcceptOrder;

    double userBalance;
    double orderAmount;
    String providerStoreLocation="";
    double userOrderLat;
    double userOrderLong;
    double providerLat;
    double providerLong;

    private ImageButton bt_toggle_reviews, bt_toggle_warranty, bt_toggle_description;
    private View lyt_expand_reviews, lyt_expand_warranty, lyt_expand_description;
    private NestedScrollView nested_scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null ? savedInstanceState : getIntent().getBundleExtra("saved_state"));
        initAllModelsAndUserOrderLatLng();
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_order_details);
        mapView=(MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        init();
        initToolbar();
        initComponent();
        initAllServicesDetails();
        initAllOrderImages();

    }
    private void initAllModelsAndUserOrderLatLng(){
        AppSharedPreferences.init(this);
        orderModel= AppSharedPreferences.readOrderModel(Config.SHARED_PREF_ORDER_MODEL,"");
        //userModel=AppSharedPreferences.readUserModel(Config.SHARED_PREF_USER_MODEL,"");
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        //||userModel.equals("")
        if(orderModel.equals("")){
            OrderDetailsActivity.this.finish();
        }
        if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_DELIVERY)){
            userOrderLat=Double.valueOf(orderModel.getData().get(0).getUserLat());
            userOrderLong=Double.valueOf(orderModel.getData().get(0).getUserLong());
        }else if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_PICKUP)){
            userOrderLat=Double.valueOf(orderModel.getUserDetails().get(0).getLatitude());
            userOrderLong=Double.valueOf(orderModel.getUserDetails().get(0).getLongitude());
        }
        providerLat=Double.valueOf(orderModel.getProviderDetails().get(0).getLatitude());
        providerLong=Double.valueOf(orderModel.getProviderDetails().get(0).getLongitude());
    }

    private void initAllOrderImages(){
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 2), true));
        recyclerView.setHasFixedSize(true);
        OrderPagePhotoGridAdapter photoGridAdapter=new OrderPagePhotoGridAdapter(this, orderModel.getServiceImages(), new OrderPhotoItemClickListener() {
            @Override
            public void onItemClick(String s) {
                //Tools.showToast(OrderDetailsActivity.this,s);
                showOrderImageFullScreen(s);
            }
        });
        recyclerView.setAdapter(photoGridAdapter);
    }
    private void showOrderImageFullScreen(String imageUrl){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ImageViewFragment newFragment = new ImageViewFragment(this,imageUrl);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private void initComponent() {
        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);

        // section reviews
        bt_toggle_reviews = (ImageButton) findViewById(R.id.bt_toggle_reviews);
        lyt_expand_reviews = (View) findViewById(R.id.lyt_expand_reviews);
        bt_toggle_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_reviews);
            }
        });

        // section warranty
        bt_toggle_warranty = (ImageButton) findViewById(R.id.bt_toggle_warranty);
        lyt_expand_warranty = (View) findViewById(R.id.lyt_expand_warranty);
        bt_toggle_warranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_warranty);
            }
        });

        // section description
        bt_toggle_description = (ImageButton) findViewById(R.id.bt_toggle_description);
        lyt_expand_description = (View) findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        // expand first description
        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.pending))){
            toggleArrow(bt_toggle_reviews);
            lyt_expand_reviews.setVisibility(View.VISIBLE);
        }else{
            toggleArrow(bt_toggle_description);
            lyt_expand_description.setVisibility(View.VISIBLE);
        }
        beautifulProgressDialog = new BeautifulProgressDialog(this, BeautifulProgressDialog.withLottie, null);
        beautifulProgressDialog.setLottieLocation("service.json");
        beautifulProgressDialog.setLottieLoop(true);

    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private void initAllServicesDetails(){
        recyclerService=findViewById(R.id.recycler_service);
        OrderPageServiceAdapter serviceAdapter =new OrderPageServiceAdapter(this, orderModel.getServiceDetails(), new OrderServiceItemClickListener() {
            @Override
            public void onItemClick(OrderModel.ServiceDetail serviceDetail) {
                Tools.showToast(OrderDetailsActivity.this,serviceDetail.getId());
            }
        });
        recyclerService.setLayoutManager(new LinearLayoutManager(this));
        recyclerService.setAdapter(serviceAdapter);
    }


    private void init(){
        floatingActionButton=findViewById(R.id.fab);
        paymentFab=findViewById(R.id.payment_fab);
        serviceName=findViewById(R.id.service_name);
        storeName=findViewById(R.id.store_name);
        servicePrice=findViewById(R.id.service_price);
        storeLocation=findViewById(R.id.store_location);
        orderDescription=findViewById(R.id.order_description);
        distanceCost=findViewById(R.id.distance_cost);
        distanceCostAmount=findViewById(R.id.distance_cost_amount);
        serviceCharge=findViewById(R.id.service_charge);
        serviceChargeAmount=findViewById(R.id.service_charge_amount);
        totalCost=findViewById(R.id.total_cost);
        totalCostAmount=findViewById(R.id.total_cost_amount);
        totalServiceCostAmount=findViewById(R.id.total_service_cost_amount);
        orderStatus=findViewById(R.id.order_status);
        btnCancelOrder=findViewById(R.id.cancel_order);
        btnAcceptOrder=findViewById(R.id.accept_order);

       // userBalance= Double.parseDouble(userModel.getData().getWallet());
        orderAmount= Double.parseDouble(orderModel.getData().get(0).getTotalCost());

        orderStatus.setText(orderModel.getOrder_status());


        String distanceCostText="Distance Cost "+"( "+orderModel.getData().get(0).getDistance()+" KM"+" X "+settingsModel.getData().getCurrencySymbol()+" "+settingsModel.getData().getServiceFeeKm()+" )";
        distanceCost.setText(distanceCostText);
        distanceCostAmount.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getServiceFee());

        totalServiceCostAmount.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getTotalServiceCost());

        String serviceChargeText="Service Charge "+"( "+settingsModel.getData().getCharge()+"% "+"Of Total Service Amount"+" )";
        serviceCharge.setText(serviceChargeText);
        serviceChargeAmount.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getServiceCharge());

        totalCostAmount.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getTotalCost());

        String servicesName=orderModel.getData().get(0).getServicesName().toString();
        servicesName=servicesName.replace("[","");
        servicesName=servicesName.replace("]","");

        serviceName.setText(servicesName);
        storeName.setText(orderModel.getProviderDetails().get(0).getStoreName());
        servicePrice.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getTotalCost());


        String orderDetailsText="This order was placed on "+orderModel.getData().get(0).getDate()+"\n"+"The order type is "+
                orderModel.getData().get(0).getOrderType()+"\n"+"\n"+"Description:"+"\n"+orderModel.getData().get(0).getServiceDescription();
        orderDescription.setText(orderDetailsText);
        allOrderStatusAndTypeRelatedMethods();

    }
    private void allOrderStatusAndTypeRelatedMethods(){
        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.completed_request))){
            btnAcceptOrder.setVisibility(View.VISIBLE);
            btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeOrderStatus(new AllOrderStatus().Status(Status.completed));
                }
            });
        }else{
            btnAcceptOrder.setVisibility(View.GONE);
        }

        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.completed))){
            btnAcceptOrder.setText("Rate the order");
            btnAcceptOrder.setVisibility(View.VISIBLE);
            btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Rate the order
                    // changeOrderStatus(new AllOrderStatus().Status(Status.rated));
                }
            });


        }




        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.pending))
                ||orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.active))
        ){
            btnCancelOrder.setEnabled(true);
            btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeOrderStatus(new AllOrderStatus().Status(Status.cancelled));
                }
            });
        }else if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.cancelled))){
            btnCancelOrder.setText("This order is cancelled");
            btnCancelOrder.setEnabled(false);

        }

        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.active))){
            if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_DELIVERY)){
                providerStoreLocation=Tools.getAdressFromLatLong(OrderDetailsActivity.this,
                        orderModel.getProviderDetails().get(0).getLatitude(),orderModel.getProviderDetails().get(0).getLongitude()
                );
                storeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigateToProviderLocation();
                    }
                });
            }else{
                providerStoreLocation=orderModel.getProviderDetails().get(0).getPhone();
                storeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callToProvider(providerStoreLocation);
                    }
                });
            }
        }else{
            providerStoreLocation=Tools.getAdressFromLatLong(OrderDetailsActivity.this,
                    orderModel.getProviderDetails().get(0).getLatitude(),orderModel.getProviderDetails().get(0).getLongitude()
            );

        }
        storeLocation.setText(providerStoreLocation);

        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.pending))){
            paymentFab.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.GONE);
            paymentFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(userBalance>orderAmount){
                        showConfirmDialog(true);
                    }else {
                        showConfirmDialog(false);
                    }
                }
            });
        }else{
            paymentFab.setVisibility(View.GONE);
        }

        if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_DELIVERY)){
            floatingActionButton.setImageResource(R.drawable.ic_baseline_phone_24);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callToProvider(orderModel.getProviderDetails().get(0).getPhone());
                }
            });
        }else if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_PICKUP)){
            floatingActionButton.setImageResource(R.drawable.ic_baseline_navigation_24);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToProviderLocation();
                }
            });
        }

    }

    private void navigateToProviderLocation() {

        Tools.showToast(OrderDetailsActivity.this,"Navigating to provider location");
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + providerLat + "," + providerLong + "&travelmode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }
    private void callToProvider(String providerStoreLocation){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + providerStoreLocation));
        startActivity(intent);
    }

    private void showConfirmDialog(boolean enoughBalance) {
        String title = "";
        String description="";
        String okText="";
        if(enoughBalance){
            title="Do you want to purchase this service?";
            description="You will be charged about "+settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getTotalCost()+" for this service.";
            okText="PURCHASE";
        }else{
            title="Do you want to recharge your wallet";
            description="You don't have enough balance in your wallet please recharge your wallet ang try again.";
            okText="RECHARGE WALLET";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(enoughBalance){
                    changeOrderStatus(new AllOrderStatus().Status(Status.active));
                }else{

                }

            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    private void changeOrderStatus(String inputStatus) {
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.changeOrderStatusById(Config.DEVICE_TYPE,Config.LANG_CODE,orderModel.getId().toString(),inputStatus);
        call.enqueue(new Callback<DataSavedModel>() {
            @Override
            public void onResponse(Call<DataSavedModel> call, Response<DataSavedModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    Tools.showToast(OrderDetailsActivity.this,response.body().getMessage().toString());
                    refreshOrderPage();
                }
            }
            @Override
            public void onFailure(Call<DataSavedModel> call, Throwable t) {
                beautifulProgressDialog.dismiss();
                Log.d("CurrentOrdersActivity",t.getMessage().toString());

            }
        });
    }

    private void refreshOrderPage(){
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<OrderModel> call=apiInterface.getOrderDetailsById(Config.DEVICE_TYPE,Config.LANG_CODE,orderModel.getId().toString());
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                beautifulProgressDialog.dismiss();
                if(response.body().getStatus()== Config.API_SUCCESS){
                    AppSharedPreferences.writeOrderModel(Config.SHARED_PREF_ORDER_MODEL,response.body());
                    transitionRecreate();

                }
            }
            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                beautifulProgressDialog.dismiss();
                Log.d("CurrentOrdersActivity",t.getMessage().toString());

            }
        });

    }
    protected void transitionRecreate(){
        Bundle bundle = new Bundle();
        onSaveInstanceState(bundle);
        Intent intent = new Intent(this, getClass());
        intent.putExtra("saved_state", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void initToolbar() {
       // userId=AppSharedPreferences.read(Config.SHARED_PREF_USER_ID,"");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Order Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        OrderDetailsActivity.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                double providerLat=Double.valueOf(orderModel.getProviderDetails().get(0).getLatitude());
                double providerLong=Double.valueOf(orderModel.getProviderDetails().get(0).getLongitude());
                String storeName=orderModel.getProviderDetails().get(0).getStoreName();
                String storeId=orderModel.getProviderDetails().get(0).getId();
                LatLng provideLatLng=new LatLng(providerLat,providerLong);

                String userName=orderModel.getUserDetails().get(0).getName();
                String userId=orderModel.getUserDetails().get(0).getId();
                LatLng userOrderLatLng=new LatLng(userOrderLat,userOrderLong);

                Bitmap bitmap= Tools.drawableToBitmap(OrderDetailsActivity.this.getDrawable(R.drawable.provider_marker));
                IconFactory iconFactory = IconFactory.getInstance(OrderDetailsActivity.this);
                Icon icon=iconFactory.fromBitmap(bitmap);
                MarkerOptions providerMarker=new MarkerOptions()
                        .position(new LatLng(providerLat,providerLong))
                        .setTitle(storeName).setIcon(icon)
                        .setSnippet("Store ID: "+storeId);
                mapboxMap.addMarker(providerMarker);

                Bitmap bitmap2= Tools.drawableToBitmap(OrderDetailsActivity.this.getDrawable(R.drawable.user_marker));
                IconFactory iconFactory2 = IconFactory.getInstance(OrderDetailsActivity.this);
                Icon icon2=iconFactory2.fromBitmap(bitmap2);
                MarkerOptions userMarker=new MarkerOptions()
                        .position(userOrderLatLng)
                        .setTitle(userName).setIcon(icon2)
                        .setSnippet("User ID: "+userId);
                mapboxMap.addMarker(userMarker);

                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(provideLatLng)
                        .include(userOrderLatLng)
                        .build();
                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 5000);
                //  mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));

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
        OrderDetailsActivity.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}