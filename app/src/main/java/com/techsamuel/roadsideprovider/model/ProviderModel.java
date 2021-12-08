package com.techsamuel.roadsideprovider.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("jsonschema2pojo")
public class ProviderModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("services")
    @Expose
    private List<String> services = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private Integer size;
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

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

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

}


