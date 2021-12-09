package com.techsamuel.roadsideprovider.helper;

public class AllOrderStatus {
    //public static final String ALL_ORDER_STATUS[] = {"pending","active","cancelled","rejected","completed","rated"};
    //public static final String ALL_ORDER_STATUS[] = {"pending","active","cancelled","cancelation_request","completed_request","completed","rated"};
    public String Status(Status status){
        String result="";
        switch (status){
            case pending:
                result= "pending";
                break;
            case active:
                result= "active";
                break;
            case cancelled:
                result= "cancelled";
                break;
            case cancelation_request:
                result= "cancelation_request";
                break;
            case completed_request:
                result= "completed_request";
                break;
            case completed:
                result= "completed";
                break;
            case rated:
                result= "rated";
                break;
        }
        return result;
    }
}
