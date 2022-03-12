package com.techsamuel.roadsideprovider.firebase_db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.techsamuel.roadsideprovider.model.OrderRequest;

public class DatabaseViewModel extends ViewModel {
    private FirebaseInstanceDatabase instance;
    public LiveData<Boolean> successAddOrderRequest;
    public LiveData<DataSnapshot> fetchedOrderRequest;
    public LiveData<Boolean> successAddAcceptOrReject;

    public DatabaseViewModel() {
        instance = new FirebaseInstanceDatabase();
    }

    public void addUserDatabase(OrderRequest orderRequest) {
        successAddOrderRequest = instance.addOrderRequest(orderRequest);
    }
    public void fetchOrderRequest() {
        fetchedOrderRequest = instance.fetchOrderRequest();
    }

    public void addAccepOrRejecttInDatabase(String type,boolean accepted,DataSnapshot dataSnapshot){
        successAddAcceptOrReject = instance.addAccepOrRejecttInDatabase(type,accepted,dataSnapshot);
    }


}
