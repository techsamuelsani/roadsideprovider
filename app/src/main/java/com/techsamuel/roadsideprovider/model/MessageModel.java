package com.techsamuel.roadsideprovider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class MessageModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    @SerializedName("data")
    @Expose
    private List<List<Datum>> data = null;

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

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<List<Datum>> getData() {
        return data;
    }

    public void setData(List<List<Datum>> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("message_title")
        @Expose
        private String messageTitle;
        @SerializedName("message_details")
        @Expose
        private String messageDetails;
        @SerializedName("message_image")
        @Expose
        private String messageImage;
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

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getMessageDetails() {
            return messageDetails;
        }

        public void setMessageDetails(String messageDetails) {
            this.messageDetails = messageDetails;
        }

        public String getMessageImage() {
            return messageImage;
        }

        public void setMessageImage(String messageImage) {
            this.messageImage = messageImage;
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

}