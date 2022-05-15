package com.techsamuel.roadsideprovider.model;

import java.util.Date;

public class OrderRequest {
    private int id;
    private int user_id;
    private  int provider_id;
    private int service_id;
    private String service_name;
    private boolean accepted;
    private boolean rejected;
    private String date;
    private String timeZone;

    double base_price;
    double price_per_minute;
    double price_per_km;
    double totalMinutes;
    double totalKms;
    double approx_cost1;
    double approx_cost2;
    double final_cost;
    double userLat;
    double userLong;
    double providerLat;
    double providerLong;
    double providerStoreLat;
    double providerStoreLong;
    double dropOfffLat;
    double dropOffLong;

    String order_type;
    String payment_type;
    String notes;

    String providerPhone;
    String userPhone;
    boolean markAsArrived;
    boolean markAsCompleted;
    boolean vehicleDetailsSaved;
    long timeInSeconds;
    boolean timerSaved;
    boolean vehicleFullPhotoSaved;
    boolean cashReceived;
    boolean orderPaid;
    String ratings;
    String reviewsNotes;



    public boolean isMarkAsCompleted() {
        return markAsCompleted;
    }

    public void setMarkAsCompleted(boolean markAsCompleted) {
        this.markAsCompleted = markAsCompleted;
    }

    public OrderRequest(int id, int user_id, int provider_id, int service_id, String service_name, boolean accepted, boolean rejected, String date, String timeZone, double base_price, double price_per_minute,
                        double price_per_km, double totalMinutes, double totalKms, double approx_cost1, double approx_cost2,double final_cost, double userLat, double userLong, double providerLat, double providerLong,
                        double providerStoreLat,double providerStoreLong,double dropOfffLat, double dropOffLong,
                        String order_type, String payment_type, String notes, String providerPhone,String userPhone, boolean markAsArrived, boolean markAsCompleted,boolean
                                vehicleDetailsSaved,long timeInSeconds,boolean timerSaved,boolean vehicleFullPhotoSaved,boolean cashReceived,boolean orderPaid,
                        String ratings,String reviewsNotes) {
        this.id = id;
        this.user_id = user_id;
        this.provider_id = provider_id;
        this.service_id = service_id;
        this.service_name = service_name;
        this.accepted = accepted;
        this.rejected=rejected;
        this.date = date;
        this.timeZone = timeZone;
        this.base_price = base_price;
        this.price_per_minute = price_per_minute;
        this.price_per_km = price_per_km;
        this.totalMinutes = totalMinutes;
        this.totalKms = totalKms;
        this.approx_cost1 = approx_cost1;
        this.approx_cost2 = approx_cost2;
        this.final_cost=final_cost;
        this.userLat = userLat;
        this.userLong = userLong;
        this.providerLat = providerLat;
        this.providerLong = providerLong;
        this.providerStoreLat=providerStoreLat;
        this.providerStoreLong=providerStoreLong;
        this.order_type = order_type;
        this.payment_type = payment_type;
        this.notes = notes;
        this.dropOfffLat=dropOfffLat;
        this.dropOffLong=dropOffLong;
        this.providerPhone=providerPhone;
        this.userPhone=userPhone;
        this.markAsArrived=markAsArrived;
        this.markAsCompleted=markAsCompleted;
        this.vehicleDetailsSaved=vehicleDetailsSaved;
        this.timeInSeconds=timeInSeconds;
        this.timerSaved=timerSaved;
        this.vehicleFullPhotoSaved=vehicleFullPhotoSaved;
        this.cashReceived=cashReceived;
        this.orderPaid=orderPaid;
        this.ratings=ratings;
        this.reviewsNotes=reviewsNotes;
    }

    public double getFinal_cost() {
        return final_cost;
    }

    public void setFinal_cost(double final_cost) {
        this.final_cost = final_cost;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviewsNotes() {
        return reviewsNotes;
    }

    public void setReviewsNotes(String reviewsNotes) {
        this.reviewsNotes = reviewsNotes;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public boolean isTimerSaved() {
        return timerSaved;
    }

    public void setTimerSaved(boolean timerSaved) {
        this.timerSaved = timerSaved;
    }

    public boolean isVehicleFullPhotoSaved() {
        return vehicleFullPhotoSaved;
    }

    public void setVehicleFullPhotoSaved(boolean vehicleFullPhotoSaved) {
        this.vehicleFullPhotoSaved = vehicleFullPhotoSaved;
    }

    public boolean isCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(boolean cashReceived) {
        this.cashReceived = cashReceived;
    }

    public boolean isOrderPaid() {
        return orderPaid;
    }

    public void setOrderPaid(boolean orderPaid) {
        this.orderPaid = orderPaid;
    }

    public boolean isVehicleDetailsSaved() {
        return vehicleDetailsSaved;
    }

    public void setVehicleDetailsSaved(boolean vehicleDetailsSaved) {
        this.vehicleDetailsSaved = vehicleDetailsSaved;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public double getProviderStoreLat() {
        return providerStoreLat;
    }

    public void setProviderStoreLat(double providerStoreLat) {
        this.providerStoreLat = providerStoreLat;
    }

    public double getProviderStoreLong() {
        return providerStoreLong;
    }

    public void setProviderStoreLong(double providerStoreLong) {
        this.providerStoreLong = providerStoreLong;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public boolean isMarkAsArrived() {
        return markAsArrived;
    }

    public void setMarkAsArrived(boolean markAsArrived) {
        this.markAsArrived = markAsArrived;
    }

    public OrderRequest(){

    }

    public double getDropOfffLat() {
        return dropOfffLat;
    }

    public void setDropOfffLat(double dropOfffLat) {
        this.dropOfffLat = dropOfffLat;
    }

    public double getDropOffLong() {
        return dropOffLong;
    }

    public void setDropOffLong(double dropOffLong) {
        this.dropOffLong = dropOffLong;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBase_price() {
        return base_price;
    }

    public void setBase_price(double base_price) {
        this.base_price = base_price;
    }

    public double getPrice_per_minute() {
        return price_per_minute;
    }

    public void setPrice_per_minute(double price_per_minute) {
        this.price_per_minute = price_per_minute;
    }

    public double getPrice_per_km() {
        return price_per_km;
    }

    public void setPrice_per_km(double price_per_km) {
        this.price_per_km = price_per_km;
    }

    public double getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(double totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public double getTotalKms() {
        return totalKms;
    }

    public void setTotalKms(double totalKms) {
        this.totalKms = totalKms;
    }

    public double getApprox_cost1() {
        return approx_cost1;
    }

    public void setApprox_cost1(double approx_cost1) {
        this.approx_cost1 = approx_cost1;
    }

    public double getApprox_cost2() {
        return approx_cost2;
    }

    public void setApprox_cost2(double approx_cost2) {
        this.approx_cost2 = approx_cost2;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public double getProviderLat() {
        return providerLat;
    }

    public void setProviderLat(double providerLat) {
        this.providerLat = providerLat;
    }

    public double getProviderLong() {
        return providerLong;
    }

    public void setProviderLong(double providerLong) {
        this.providerLong = providerLong;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
