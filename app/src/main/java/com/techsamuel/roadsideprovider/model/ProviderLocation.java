package com.techsamuel.roadsideprovider.model;

public class ProviderLocation {
    double providerLat;
    double providerLong;

    public ProviderLocation(){

    }

    public ProviderLocation(double providerLat,double providerLong) {
        this.providerLat = providerLat;
        this.providerLong = providerLong;
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
}
