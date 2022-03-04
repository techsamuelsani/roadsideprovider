package com.techsamuel.roadsideprovider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class OrderModel {
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("order_status")
    @Expose
    private String order_status;

    @SerializedName("cancelation_reason")
    @Expose
    private String cancelation_reason;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCancelation_reason() {
        return cancelation_reason;
    }

    public void setCancelation_reason(String cancelation_reason) {
        this.cancelation_reason = cancelation_reason;
    }

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_details")
    @Expose
    private List<ServiceDetail> serviceDetails = null;
    @SerializedName("service_images")
    @Expose
    private List<String> serviceImages = null;
    @SerializedName("vehicle_details")
    @Expose
    private List<VehicleDetail> vehicleDetails = null;
    @SerializedName("user_details")
    @Expose
    private List<UserDetail> userDetails = null;
    @SerializedName("provider_details")
    @Expose
    private List<ProviderDetail> providerDetails = null;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ServiceDetail> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<ServiceDetail> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public List<String> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(List<String> serviceImages) {
        this.serviceImages = serviceImages;
    }

    public List<VehicleDetail> getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(List<VehicleDetail> vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public List<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

    public List<ProviderDetail> getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(List<ProviderDetail> providerDetails) {
        this.providerDetails = providerDetails;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("order_type")
        @Expose
        private String orderType;
        @SerializedName("provider_lat")
        @Expose
        private String providerLat;
        @SerializedName("provider_long")
        @Expose
        private String providerLong;
        @SerializedName("user_lat")
        @Expose
        private String userLat;
        @SerializedName("user_long")
        @Expose
        private String userLong;
        @SerializedName("service_fee_per_km")
        @Expose
        private Integer serviceFeePerKm;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("service_fee")
        @Expose
        private String serviceFee;
        @SerializedName("service_charge")
        @Expose
        private String serviceCharge;
        @SerializedName("total_service_cost")
        @Expose
        private String totalServiceCost;
        @SerializedName("total_cost")
        @Expose
        private String totalCost;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("service_description")
        @Expose
        private String serviceDescription;
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("cancelation_details")
        @Expose
        private String cancelationDetails;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_image")
        @Expose
        private String userImage;
        @SerializedName("provider_name")
        @Expose
        private String providerName;
        @SerializedName("provider_image")
        @Expose
        private String providerImage;
        @SerializedName("services_name")
        @Expose
        private List<String> servicesName = null;
        @SerializedName("services_images")
        @Expose
        private List<String> servicesImages = null;
        @SerializedName("vehicle_name")
        @Expose
        private String vehicleName;

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getProviderLat() {
            return providerLat;
        }

        public void setProviderLat(String providerLat) {
            this.providerLat = providerLat;
        }

        public String getProviderLong() {
            return providerLong;
        }

        public void setProviderLong(String providerLong) {
            this.providerLong = providerLong;
        }

        public String getUserLat() {
            return userLat;
        }

        public void setUserLat(String userLat) {
            this.userLat = userLat;
        }

        public String getUserLong() {
            return userLong;
        }

        public void setUserLong(String userLong) {
            this.userLong = userLong;
        }

        public Integer getServiceFeePerKm() {
            return serviceFeePerKm;
        }

        public void setServiceFeePerKm(Integer serviceFeePerKm) {
            this.serviceFeePerKm = serviceFeePerKm;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(String serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public String getTotalServiceCost() {
            return totalServiceCost;
        }

        public void setTotalServiceCost(String totalServiceCost) {
            this.totalServiceCost = totalServiceCost;
        }

        public String getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(String totalCost) {
            this.totalCost = totalCost;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getCancelationDetails() {
            return cancelationDetails;
        }

        public void setCancelationDetails(String cancelationDetails) {
            this.cancelationDetails = cancelationDetails;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getProviderImage() {
            return providerImage;
        }

        public void setProviderImage(String providerImage) {
            this.providerImage = providerImage;
        }

        public List<String> getServicesName() {
            return servicesName;
        }

        public void setServicesName(List<String> servicesName) {
            this.servicesName = servicesName;
        }

        public List<String> getServicesImages() {
            return servicesImages;
        }

        public void setServicesImages(List<String> servicesImages) {
            this.servicesImages = servicesImages;
        }

        public String getVehicleName() {
            return vehicleName;
        }

        public void setVehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
        }

    }
    public class ProviderDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("vmake")
        @Expose
        private String vmake;
        @SerializedName("vmodel")
        @Expose
        private String vmodel;
        @SerializedName("plate_no")
        @Expose
        private String plateNo;
        @SerializedName("vyear")
        @Expose
        private String vyear;
        @SerializedName("vr_exp_date")
        @Expose
        private String vrExpDate;
        @SerializedName("services")
        @Expose
        private String services;
        @SerializedName("services_id")
        @Expose
        private String servicesId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("national_id")
        @Expose
        private String nationalId;
        @SerializedName("firebase_id")
        @Expose
        private String firebaseId;
        @SerializedName("wallet")
        @Expose
        private String wallet;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("android_device_id")
        @Expose
        private String androidDeviceId;
        @SerializedName("ios_device_id")
        @Expose
        private String iosDeviceId;
        @SerializedName("fcm_ios")
        @Expose
        private String fcmIos;
        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("store_details")
        @Expose
        private String storeDetails;
        @SerializedName("store_photo")
        @Expose
        private String storePhoto;
        @SerializedName("fcm")
        @Expose
        private String fcm;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVmake() {
            return vmake;
        }

        public void setVmake(String vmake) {
            this.vmake = vmake;
        }

        public String getVmodel() {
            return vmodel;
        }

        public void setVmodel(String vmodel) {
            this.vmodel = vmodel;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public String getVyear() {
            return vyear;
        }

        public void setVyear(String vyear) {
            this.vyear = vyear;
        }

        public String getVrExpDate() {
            return vrExpDate;
        }

        public void setVrExpDate(String vrExpDate) {
            this.vrExpDate = vrExpDate;
        }

        public String getServices() {
            return services;
        }

        public void setServices(String services) {
            this.services = services;
        }

        public String getServicesId() {
            return servicesId;
        }

        public void setServicesId(String servicesId) {
            this.servicesId = servicesId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNationalId() {
            return nationalId;
        }

        public void setNationalId(String nationalId) {
            this.nationalId = nationalId;
        }

        public String getFirebaseId() {
            return firebaseId;
        }

        public void setFirebaseId(String firebaseId) {
            this.firebaseId = firebaseId;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getAndroidDeviceId() {
            return androidDeviceId;
        }

        public void setAndroidDeviceId(String androidDeviceId) {
            this.androidDeviceId = androidDeviceId;
        }

        public String getIosDeviceId() {
            return iosDeviceId;
        }

        public void setIosDeviceId(String iosDeviceId) {
            this.iosDeviceId = iosDeviceId;
        }

        public String getFcmIos() {
            return fcmIos;
        }

        public void setFcmIos(String fcmIos) {
            this.fcmIos = fcmIos;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreDetails() {
            return storeDetails;
        }

        public void setStoreDetails(String storeDetails) {
            this.storeDetails = storeDetails;
        }

        public String getStorePhoto() {
            return storePhoto;
        }

        public void setStorePhoto(String storePhoto) {
            this.storePhoto = storePhoto;
        }

        public String getFcm() {
            return fcm;
        }

        public void setFcm(String fcm) {
            this.fcm = fcm;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class ServiceDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("details")
        @Expose
        private String details;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public class UserDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("firebase_id")
        @Expose
        private String firebaseId;
        @SerializedName("device_id")
        @Expose
        private String deviceId;
        @SerializedName("wallet")
        @Expose
        private String wallet;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("fcm_android")
        @Expose
        private String fcmAndroid;
        @SerializedName("fcm_ios")
        @Expose
        private String fcmIos;
        @SerializedName("user_photo")
        @Expose
        private String userPhoto;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirebaseId() {
            return firebaseId;
        }

        public void setFirebaseId(String firebaseId) {
            this.firebaseId = firebaseId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getFcmAndroid() {
            return fcmAndroid;
        }

        public void setFcmAndroid(String fcmAndroid) {
            this.fcmAndroid = fcmAndroid;
        }

        public String getFcmIos() {
            return fcmIos;
        }

        public void setFcmIos(String fcmIos) {
            this.fcmIos = fcmIos;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public class VehicleDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("vmake")
        @Expose
        private String vmake;
        @SerializedName("vmodel")
        @Expose
        private String vmodel;
        @SerializedName("plate_no")
        @Expose
        private String plateNo;
        @SerializedName("vyear")
        @Expose
        private String vyear;
        @SerializedName("vr_exp_date")
        @Expose
        private String vrExpDate;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVmake() {
            return vmake;
        }

        public void setVmake(String vmake) {
            this.vmake = vmake;
        }

        public String getVmodel() {
            return vmodel;
        }

        public void setVmodel(String vmodel) {
            this.vmodel = vmodel;
        }

        public String getPlateNo() {
            return plateNo;
        }

        public void setPlateNo(String plateNo) {
            this.plateNo = plateNo;
        }

        public String getVyear() {
            return vyear;
        }

        public void setVyear(String vyear) {
            this.vyear = vyear;
        }

        public String getVrExpDate() {
            return vrExpDate;
        }

        public void setVrExpDate(String vrExpDate) {
            this.vrExpDate = vrExpDate;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

}
