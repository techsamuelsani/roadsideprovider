package com.techsamuel.roadsideprovider.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class PageModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("id")
    @Expose
    private Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        @SerializedName("page_name")
        @Expose
        private String pageName;
        @SerializedName("page_url")
        @Expose
        private String pageUrl;
        @SerializedName("page_icon")
        @Expose
        private String pageIcon;
        @SerializedName("page_content")
        @Expose
        private String pageContent;
        @SerializedName("display_on")
        @Expose
        private String displayOn;
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

        public String getPageName() {
            return pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public String getPageIcon() {
            return pageIcon;
        }

        public void setPageIcon(String pageIcon) {
            this.pageIcon = pageIcon;
        }

        public String getPageContent() {
            return pageContent;
        }

        public void setPageContent(String pageContent) {
            this.pageContent = pageContent;
        }

        public String getDisplayOn() {
            return displayOn;
        }

        public void setDisplayOn(String displayOn) {
            this.displayOn = displayOn;
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


