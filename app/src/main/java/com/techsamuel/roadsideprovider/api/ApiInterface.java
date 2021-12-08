package com.techsamuel.roadsideprovider.api;

import com.techsamuel.roadsideprovider.model.AdminUser;
import com.techsamuel.roadsideprovider.model.DataSavedModel;
import com.techsamuel.roadsideprovider.model.ProviderModel;
import com.techsamuel.roadsideprovider.model.ServiceModel;

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
    @POST("getProviderById")
    Call<ProviderModel> getProviderById(@Field("provider_id") String provider_id);

    @FormUrlEncoded
    @POST("getAllServices")
    Call<ServiceModel> getAllServices(@Field("device_type") String device_type, @Field("lang_code") String lang_code);


}
