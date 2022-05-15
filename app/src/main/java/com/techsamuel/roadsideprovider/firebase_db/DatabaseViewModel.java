package com.techsamuel.roadsideprovider.firebase_db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.model.ProviderLocation;
import com.techsamuel.roadsideprovider.model.OrderRequest;

public class DatabaseViewModel extends ViewModel {
    private FirebaseInstanceDatabase instance;
    public LiveData<Boolean> successAddOrderRequest;
    public LiveData<Boolean> successAddProviderLocation;
    public LiveData<DataSnapshot> fetchedOrderRequest;
    public LiveData<DataSnapshot> fetchedProviderLocation;
    public LiveData<Boolean> successAddAcceptOrReject;
    public LiveData<Boolean> successAddTrueFalseInDatabase;

    public DatabaseViewModel() {
        instance = new FirebaseInstanceDatabase();
    }

    public void addOrderRequestInDatabase(OrderRequest orderRequest) {
        successAddOrderRequest = instance.addOrderRequest(orderRequest);
    }
    public void addProviderLocationInDatabase(int orderId,ProviderLocation providerLocation) {
        successAddProviderLocation = instance.addProviderLocation(orderId,providerLocation);
    }
    public void fetchProviderLocation(int orderId){
        fetchedProviderLocation=instance.fetchProviderLocation(orderId);
    }
    public void fetchOrderRequest() {
        fetchedOrderRequest = instance.fetchOrderRequest();
    }

    public void addAccepOrRejecttInDatabase(String type,boolean accepted,DataSnapshot dataSnapshot){
        successAddAcceptOrReject = instance.addAccepOrRejecttInDatabase(type,accepted,dataSnapshot);
    }


    public void addTrueFalseInDatabase(String type, boolean b, int id) {
        successAddTrueFalseInDatabase=instance.addTrueFalseInDatabase(type,b,id);
    }
}
