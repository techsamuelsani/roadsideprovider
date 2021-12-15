package com.techsamuel.roadsideprovider.api;

import com.techsamuel.roadsideprovider.model.AdminUser;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.OrdersModel;
import com.techsamuel.roadsideprovider.model.PageModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ReviewReasonModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("registerProviderStep1")
    Call<DataSavedModel> registerProviderStep1(@Field("vmake") String vmake, @Field("vmodel") String vmodel,@Field("plate_no") String plate_no,
                    @Field("vyear") String vyear, @Field("vr_exp_date") String vr_exp_date, @Field("name") String name,
         @Field("email") String email, @Field("password") String password,@Field("confirm_password") String confirmPassword, @Field("national_id") String national_id,
         @Field("firebase_id") String firebase_id, @Field("phone") String phone
    );

    @Multipart
    @POST("registerProviderStep2")
    Call<DataSavedModel> registerProviderStep2(@Part("provider_id") String provider_id, @Part("provider_phone") String provider_phone,
                                               @Part MultipartBody.Part nid_front, @Part MultipartBody.Part nid_back,
                                               @Part List<MultipartBody.Part> toolbox_photos, @Part MultipartBody.Part driving_license,
                                               @Part MultipartBody.Part owner_photo);
    @Multipart
    @POST("registerProviderStep3")
    Call<DataSavedModel> registerProviderStep3(@Part MultipartBody.Part device_type,@Part MultipartBody.Part lang_code,@Part MultipartBody.Part provider_id,@Part MultipartBody.Part provider_phone,@Part MultipartBody.Part store_name,@Part MultipartBody.Part store_details,@Part MultipartBody.Part storeLogoFile);

    @FormUrlEncoded
    @POST("checkRegistrationStep")
    Call<DataSavedModel> checkRegistrationStep(@Field("provider_phone") String provider_phone);

    @FormUrlEncoded
    @POST("updateProviderServices")
    Call<DataSavedModel> updateProviderServices(@Field("provider_id") String provider_id,@Field("provider_phone") String provider_phone,@Field("provider_services[]") ArrayList<String> provider_services,
                                                @Field("services_id[]") ArrayList<String> services_id);

    @FormUrlEncoded
    @POST("updateLocationToServer")
    Call<DataSavedModel> updateLocationToServer(@Field("provider_id") String provider_id,@Field("latitude") String latitude,@Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("updateFcmToServer")
    Call<DataSavedModel> updateFcmToServer(@Field("provider_id") String provider_id,@Field("fcm") String fcm);


    @FormUrlEncoded
    @POST("getIdByPhone")
    Call<DataSavedModel> getIdByPhone(@Field("provider_phone") String provider_phone);

    @FormUrlEncoded
    @POST("getServicesById")
    Call<DataSavedModel> getServicesById(@Field("provider_id") String provider_id);

    @FormUrlEncoded
    @POST("getProviderByPhone")
    Call<ProviderModel> getProviderByPhone(@Field("provider_phone") String provider_phone);

    @FormUrlEncoded
    @POST("getAllServices")
    Call<ServiceModel> getAllServices(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("updateDeviceInformationToServer")
    Call<DataSavedModel> updateDeviceInformationToServer(@Field("device_type") String device_type,@Field("user_type") String user_type,@Field("user_id") String user_id, @Field("lang_code") String lang_code,
                                                         @Field("latitude") String latitude, @Field("longitude") String longitude,@Field("fcm") String fcm,
                                                         @Field("device_id") String device_id,@Field("firebase_id") String firebase_id);
    @FormUrlEncoded
    @POST("getAllSettings")
    Call<SettingsModel> getAllSettings(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("changeOrderStatusById")
    Call<DataSavedModel> changeOrderStatusById(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                               @Field("order_id") String order_id,@Field("input_status") String input_status);

    @FormUrlEncoded
    @POST("getOrderDetailsById")
    Call<OrderModel> getOrderDetailsById(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                         @Field("order_id") String order_id);
    @FormUrlEncoded
    @POST("getReviewAndReason")
    Call<ReviewReasonModel> getReviewAndReason(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                               @Field("order_id") String order_id);
    @FormUrlEncoded
    @POST("orderActivityRequest")
    Call<DataSavedModel> orderActivityRequest(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                              @Field("user_type") String user_type,@Field("user_id") String user_id,
                                              @Field("order_id") String order_id,@Field("details") String details,@Field("type") String type,@Field("ratings") String ratings);

    @FormUrlEncoded
    @POST("getAllOrders")
    Call<OrdersModel> getAllOrders(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                   @Field("user_type") String user_type, @Field("user_id") String user_id, @Field("order_status_type") String order_status_type);
    @FormUrlEncoded
    @POST("getPagesByDevicyType")
    Call<PageModel> getPagesByDevicyType(@Field("device_type") String device_type, @Field("lang_code") String lang_code);

    @FormUrlEncoded
    @POST("getMessageByTypeAndId")
    Call<MessageModel> getMessageByTypeAndId(@Field("device_type") String device_type, @Field("lang_code") String lang_code,
                                             @Field("user_type") String user_type, @Field("user_id") String user_id);


}
