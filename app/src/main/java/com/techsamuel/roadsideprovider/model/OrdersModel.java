package com.techsamuel.roadsideprovider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class OrdersModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
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
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("lang_code")
        @Expose
        private String langCode;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("provider_id")
        @Expose
        private String providerId;
        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("vehicle_id")
        @Expose
        private String vehicleId;
        @SerializedName("vehicle_name")
        @Expose
        private String vehicleName;
        @SerializedName("order_type")
        @Expose
        private String orderType;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("service_description")
        @Expose
        private String serviceDescription;
        @SerializedName("service_images")
        @Expose
        private String serviceImages;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("details")
        @Expose
        private String details;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("cancelation_details")
        @Expose
        private String cancelationDetails;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getLangCode() {
            return langCode;
        }

        public void setLangCode(String langCode) {
            this.langCode = langCode;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getVehicleName() {
            return vehicleName;
        }

        public void setVehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public String getServiceImages() {
            return serviceImages;
        }

        public void setServiceImages(String serviceImages) {
            this.serviceImages = serviceImages;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCancelationDetails() {
            return cancelationDetails;
        }

        public void setCancelationDetails(String cancelationDetails) {
            this.cancelationDetails = cancelationDetails;
        }

    }
    public class OrderDetail {

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

}
