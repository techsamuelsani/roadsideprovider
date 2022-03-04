package com.techsamuel.roadsideprovider.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
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
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.adapter.OrderPagePhotoGridAdapter;
import com.techsamuel.roadsideprovider.adapter.OrderPageServiceAdapter;
import com.techsamuel.roadsideprovider.api.ApiInterface;
import com.techsamuel.roadsideprovider.api.ApiServiceGenerator;
import com.techsamuel.roadsideprovider.fragment.ImageViewFragment;
import com.techsamuel.roadsideprovider.helper.AllOrderStatus;
import com.techsamuel.roadsideprovider.helper.Status;
import com.techsamuel.roadsideprovider.listener.OrderPhotoItemClickListener;
import com.techsamuel.roadsideprovider.listener.OrderServiceItemClickListener;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ReviewReasonModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.UserModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;
import com.techsamuel.roadsideprovider.tools.SpacingItemDecoration;
import com.techsamuel.roadsideprovider.tools.Tools;
import com.techsamuel.roadsideprovider.tools.ViewAnimation;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {


    OrderModel orderModel;
    SettingsModel settingsModel;
    ProviderModel providerModel;
    MapView mapView;
    MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    String userId;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
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

    private LinearLayout lytReview;
    private TextView conclusionDetails;
    private ImageView ratingUserImage;
    private AppCompatRatingBar ratingBar;
    private TextView conclusionReview;

    private TextView carDetails,expDate,plateNo;

    private ImageButton bt_toggle_reviews, bt_toggle_warranty, bt_toggle_description,bt_toggle_conclusion,bt_toggle_vehicles;
    private View lyt_expand_reviews, lyt_expand_warranty, lyt_expand_description,lyt_expand_conclusion,lyt_expand_vehicles;
    private NestedScrollView nested_scroll_view;
    NavigationMapRoute navigationMapRoute;
    LocationEngineCallback<LocationEngineResult> locationEngineCallback;
    LatLng deviceLocation=null;


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
        initDeviceLocation();

    }

    private void initAllModelsAndUserOrderLatLng(){
        AppSharedPreferences.init(this);
        orderModel= AppSharedPreferences.readOrderModel(Config.SHARED_PREF_ORDER_MODEL,"");
        providerModel=AppSharedPreferences.readProviderModel(Config.SHARED_PREF_PROVIDER_MODEL,"");
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");
        if(orderModel.equals("")||providerModel.equals("")){
            OrderDetailsActivity.this.finish();
        }
        if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_PICKUP)){
            userOrderLat=Double.valueOf(orderModel.getUserDetails().get(0).getLatitude());
            userOrderLong=Double.valueOf(orderModel.getUserDetails().get(0).getLongitude());
        }else if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_DELIVERY)){
            userOrderLat=Double.valueOf(orderModel.getData().get(0).getUserLat());
            userOrderLong=Double.valueOf(orderModel.getData().get(0).getUserLong());
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

        bt_toggle_vehicles = (ImageButton) findViewById(R.id.bt_toggle_vehicles);
        lyt_expand_vehicles = (View) findViewById(R.id.lyt_expand_vehicles);
        bt_toggle_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_vehicles);
            }
        });

        bt_toggle_conclusion = (ImageButton) findViewById(R.id.bt_toggle_conclusion);
        lyt_expand_conclusion = (View) findViewById(R.id.lyt_expand_conclusion);
        bt_toggle_conclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_conclusion);
            }
        });

        // expand first description
        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.pending))){
            toggleArrow(bt_toggle_reviews);
            lyt_expand_reviews.setVisibility(View.VISIBLE);
        }else if((orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.active)))){
            toggleArrow(bt_toggle_description);
            lyt_expand_description.setVisibility(View.VISIBLE);
        }else if((orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.completed)))){
            toggleArrow(bt_toggle_conclusion);
            lyt_expand_conclusion.setVisibility(View.VISIBLE);
        }
        else if((orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.rated)))){
            toggleArrow(bt_toggle_conclusion);
            lyt_expand_conclusion.setVisibility(View.VISIBLE);
            lytReview.setVisibility(View.VISIBLE);
        }
        else if((orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.cancelled)))){
            toggleArrow(bt_toggle_conclusion);
            lyt_expand_conclusion.setVisibility(View.VISIBLE);
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

    private void setVehicleDetails(){
        carDetails=findViewById(R.id.car_details);
        expDate=findViewById(R.id.exp_date);
        plateNo=findViewById(R.id.plate_no);

        carDetails.setText(carDetails.getText()+orderModel.getVehicleDetails().get(0).getVmake()+
                " "+orderModel.getVehicleDetails().get(0).getVmodel()+" "+orderModel.getVehicleDetails().get(0).getVyear());
        expDate.setText(expDate.getText()+orderModel.getVehicleDetails().get(0).getVyear());
        plateNo.setText(plateNo.getText()+orderModel.getVehicleDetails().get(0).getPlateNo());

    }


    private void init(){
        floatingActionButton=findViewById(R.id.fab);
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

        getReviewAndReason();
        setVehicleDetails();


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
        storeName.setText(orderModel.getUserDetails().get(0).getName());
        servicePrice.setText(settingsModel.getData().getCurrencySymbol()+" "+orderModel.getData().get(0).getTotalCost());


        String orderDetailsText="This order was placed on "+orderModel.getData().get(0).getDate()+"\n"+"The order type is "+
                orderModel.getData().get(0).getOrderType()+"\n"+"\n"+"Description:"+"\n"+orderModel.getData().get(0).getServiceDescription();
        orderDescription.setText(orderDetailsText);
        allOrderStatusAndTypeRelatedMethods();

    }

    private void getReviewAndReason(){
        lytReview=findViewById(R.id.lyt_review);
        conclusionDetails=findViewById(R.id.conclusion_details);
        ratingUserImage=findViewById(R.id.rating_user_image);
        ratingBar=findViewById(R.id.rating_bar);
        conclusionReview=findViewById(R.id.conclusion_review);

        Glide.with(OrderDetailsActivity.this).load(Config.BASE_URL+orderModel.getUserDetails().get(0).getUserPhoto()).into(ratingUserImage);

        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<ReviewReasonModel> call=apiInterface.getReviewAndReason(Config.DEVICE_TYPE,Config.LANG_CODE,orderModel.getId().toString());
        call.enqueue(new Callback<ReviewReasonModel>() {
            @Override
            public void onResponse(Call<ReviewReasonModel> call, Response<ReviewReasonModel> response) {
                Log.d("CurrentOrdersActivity",response.body().getMessage().toString());
                if(response.body().getStatus()== Config.API_SUCCESS){
                    if(response.body().getData().get(0).getType().equals("review")){
                        lytReview.setVisibility(View.VISIBLE);
                        ratingBar.setRating(Float.parseFloat(response.body().getData().get(0).getRatings()));
                        conclusionReview.setText(response.body().getData().get(0).getReview());
                        conclusionDetails.setText(response.body().getData().get(0).getDetails());

                    }
                    if(response.body().getData().get(0).getType().equals("reason")){
                        conclusionDetails.setText(response.body().getData().get(0).getDetails());
                        lytReview.setVisibility(View.GONE);

                    }else if(response.body().getData().get(0).getType().equals("empty")){
                        lytReview.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ReviewReasonModel> call, Throwable t) {
                Log.d("CurrentOrdersActivity",t.getMessage().toString());

            }
        });
    }


//    private void orderPendingOrActive(String status){
//        btnCancelOrder.setEnabled(true);
//        btnAcceptOrder.setVisibility(View.GONE);
//        btnCancelOrder.setVisibility(View.VISIBLE);
//        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openCancelDialog(status);
//            }
//        });
//
//    }
    private void orderIsActive(){
        floatingActionButton.setVisibility(View.VISIBLE);
        conclusionDetails.setText("Order is active");
        btnCancelOrder.setEnabled(false);
        btnAcceptOrder.setText("Mark As Completed");
        btnAcceptOrder.setVisibility(View.VISIBLE);
        btnCancelOrder.setVisibility(View.GONE);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCompleteDialog("completed_request");
            }
        });
    }
    private void orderIsPending(){
        btnAcceptOrder.setVisibility(View.GONE);
        btnCancelOrder.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order is pending");
    }

    private void orderIsCancelled(){
        btnCancelOrder.setText("This order is cancelled");
        btnCancelOrder.setEnabled(false);
        btnCancelOrder.setVisibility(View.VISIBLE);
        btnAcceptOrder.setVisibility(View.GONE);


        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order is cancelled");
    }
    private void orderCancelationRequest(){
        btnCancelOrder.setText("Appect Order Cancelation Request");
        btnCancelOrder.setEnabled(false);
        btnAcceptOrder.setText("Cancel the request");
        btnAcceptOrder.setVisibility(View.VISIBLE);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderActivityRequest("active","Order is active again","");
            }
        });
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderActivityRequest("cancelled","Order is cancelled mutually","");
            }
        });


        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order cancelation request received");
    }

    private void orderIsCompleted(String status){
        btnAcceptOrder.setText("Waiting for Rating");
        btnAcceptOrder.setVisibility(View.VISIBLE);
        btnCancelOrder.setVisibility(View.GONE);
        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rate the order
                // changeOrderStatus(new AllOrderStatus().Status(Status.rated));

            }
        });


        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order is completed");
    }



    private void orderIsRated(){
        btnAcceptOrder.setText("This Order is Completed and Rated");
        btnAcceptOrder.setVisibility(View.VISIBLE);
        btnCancelOrder.setVisibility(View.GONE);

        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order is rated");

    }
    private void orderCompletedRequest(){
        btnAcceptOrder.setVisibility(View.VISIBLE);
        btnCancelOrder.setVisibility(View.GONE);
        btnAcceptOrder.setText("Order Completion Request Sent");
        btnCancelOrder.setText("Cancel the request");
//        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeOrderStatus(new AllOrderStatus().Status(Status.completed));
//            }
//        });
//        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeOrderStatus(new AllOrderStatus().Status(Status.active));
//            }
//        });

        floatingActionButton.setVisibility(View.GONE);
        conclusionDetails.setText("Order completion request sent");
    }



    private void allOrderStatusAndTypeRelatedMethods(){
        //{"pending","active","cancelled","cancelation_request","completed_request","completed","rated"};
        //btnCancelOrder,btnAcceptOrder;
        switch(orderModel.getOrder_status()) {
            case "pending":
                // code block
                //orderPendingOrActive("cancelled");
                orderIsPending();
                break;
            case "active":
                // code block
               // orderPendingOrActive("cancelation_request");
                orderIsActive();
                break;
            case "cancelled":
                // code block
                orderIsCancelled();

                break;
            case "cancelation_request":
                // code block
                orderCancelationRequest();
                break;
            case "completed_request":
                // code block
                orderCompletedRequest();
                break;
            case "completed":
                // code block
                orderIsCompleted("rated");
                break;
            case "rated":
                orderIsRated();
                // code block
                break;
            default:
                // code block
        }


        if(orderModel.getOrder_status().equals(new AllOrderStatus().Status(Status.active))){
            if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_PICKUP)){
                providerStoreLocation=Tools.getAdressFromLatLong(OrderDetailsActivity.this,
                        orderModel.getData().get(0).getUserLat(),orderModel.getData().get(0).getUserLong()
                );
                storeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigateToProviderLocation();
                    }
                });
            }else{
                providerStoreLocation=orderModel.getUserDetails().get(0).getPhone();
                storeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callToProvider(providerStoreLocation);
                    }
                });
            }
        }else{
            providerStoreLocation=Tools.getAdressFromLatLong(OrderDetailsActivity.this,
                    orderModel.getData().get(0).getUserLat(),orderModel.getData().get(0).getUserLong()
            );

        }
        storeLocation.setText(providerStoreLocation);

        if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_PICKUP)){
            floatingActionButton.setImageResource(R.drawable.ic_baseline_phone_24);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callToProvider(orderModel.getProviderDetails().get(0).getPhone());
                }
            });
        }else if(orderModel.getData().get(0).getOrderType().equals(Config.ORDER_TYPE_DELIVERY)){
            floatingActionButton.setImageResource(R.drawable.ic_baseline_navigation_24);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToProviderLocation();
                }
            });
        }

    }

    private String selectedDetails="";
    private void openCompleteDialog(String type){
        final boolean[] isOthers = {false};
        String[] DETAILS = new String[]{
                "Order is done", "Order is finsihed", "Done as per requirements", "Other"
        };
        selectedDetails = DETAILS[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please selecet a completion details");
        final EditText input = new EditText(this);
        builder.setView(input);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter Other details");
        input.setVisibility(View.GONE);

        builder.setSingleChoiceItems(DETAILS, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==DETAILS.length-1){
                    input.setVisibility(View.VISIBLE);
                    isOthers[0] =true;
                }else{
                    isOthers[0] =false;
                    input.setVisibility(View.GONE);

                }
                selectedDetails = DETAILS[i];

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(isOthers[0]){
                    orderActivityRequest(type,input.getText().toString(),"");
                    // Tools.showToast(OrderDetailsActivity.this,input.getText().toString());
                }else{
                    orderActivityRequest(type,selectedDetails,"");
                    //Tools.showToast(OrderDetailsActivity.this,selectedDetails);
                }

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    private void navigateToProviderLocation() {
        if(deviceLocation!=null){
            navigateUsingMapbox();
        }else{
            Tools.showToast(OrderDetailsActivity.this,"Please turn on your gps");
        }


    }
    private void navigateUsingMapbox(){
        Tools.showToast(OrderDetailsActivity.this,"Searching... best route for you");

        //Point providerPoint=Point.fromLngLat(providerLong,providerLat);
        //Point userPoint=Point.fromLngLat(userOrderLong,userOrderLat);

        Point devicePoint=Point.fromLngLat(deviceLocation.getLongitude(),deviceLocation.getLatitude());
        Point destinationPoint=Point.fromLngLat(userOrderLong,userOrderLat);

        NavigationRoute.builder(OrderDetailsActivity.this).accessToken(
                getResources().getString(R.string.mapbox_access_token)
        ).origin(devicePoint).destination(destinationPoint).build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if(response.body()==null){
                    Tools.showToast(OrderDetailsActivity.this,"No route found");
                    return;
                }else if(response.body().routes().size()==0){
                    Tools.showToast(OrderDetailsActivity.this,"No route found");
                    return;
                }
                DirectionsRoute directionsRoute=response.body().routes().get(0);
                if(navigationMapRoute!=null){
                    navigationMapRoute.removeRoute();
                }else{
                    navigationMapRoute=new NavigationMapRoute(null,mapView,mapboxMap);
                }
                navigationMapRoute.addRoute(directionsRoute);

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavigationLauncherOptions navigationLauncherOptions= NavigationLauncherOptions.builder().directionsRoute(directionsRoute).build();
                        NavigationLauncher.startNavigation(OrderDetailsActivity.this,navigationLauncherOptions);
                    }
                },2000);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("MapBoxNavigation",t.getMessage());

            }
        });
    }
    private void navigateUsingGoogleMaps(){
        Tools.showToast(OrderDetailsActivity.this,"Navigating to provider location");
        String url = "https://www.google.com/maps/dir/?api=1&destination=" + providerLat + "," + providerLong + "&travelmode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }




    private void callToProvider(String providerStoreLocation){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + providerStoreLocation));
        startActivity(intent);
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

    private void orderActivityRequest(String type,String details,String ratings) {
        beautifulProgressDialog.show();
        ApiInterface apiInterface= ApiServiceGenerator.createService(ApiInterface.class);
        Call<DataSavedModel> call=apiInterface.orderActivityRequest(Config.DEVICE_TYPE,Config.LANG_CODE,Config.USER_TYPE,
                userId,orderModel.getId().toString(),details,type,ratings);
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
                   // Config.balance= Double.valueOf(orderModel.getUserDetails().get(0).getWallet());
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
        userId=AppSharedPreferences.read(Config.SHARED_PREF_PROVIDER_ID,"");
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

    private void initDeviceLocation(){
        locationEngineCallback=new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult locationEngineResult) {
                deviceLocation=new LatLng(locationEngineResult.getLastLocation().getLatitude(),locationEngineResult.getLastLocation().getLongitude());
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                deviceLocation=null;
            }
        };
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