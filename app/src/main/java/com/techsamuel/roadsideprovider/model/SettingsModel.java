
package com.techsamuel.roadsideprovider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SettingsModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Generated("jsonschema2pojo")
    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("app_name")
        @Expose
        private String appName;
        @SerializedName("app_currency")
        @Expose
        private String appCurrency;
        @SerializedName("currency_symbol")
        @Expose
        private String currencySymbol;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("commission")
        @Expose
        private String commission;
        @SerializedName("service_fee_km")
        @Expose
        private String serviceFeeKm;
        @SerializedName("charge")
        @Expose
        private String charge;
        @SerializedName("radius")
        @Expose
        private String radius;
        @SerializedName("max_result")
        @Expose
        private String maxResult;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppCurrency() {
            return appCurrency;
        }

        public void setAppCurrency(String appCurrency) {
            this.appCurrency = appCurrency;
        }

        public String getCurrencySymbol() {
            return currencySymbol;
        }

        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getServiceFeeKm() {
            return serviceFeeKm;
        }

        public void setServiceFeeKm(String serviceFeeKm) {
            this.serviceFeeKm = serviceFeeKm;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(String maxResult) {
            this.maxResult = maxResult;
        }

    }




}

