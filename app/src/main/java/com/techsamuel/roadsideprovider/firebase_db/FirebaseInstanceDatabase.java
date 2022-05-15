package com.techsamuel.roadsideprovider.firebase_db;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techsamuel.roadsideprovider.model.ProviderLocation;
import com.techsamuel.roadsideprovider.model.OrderRequest;

import java.util.HashMap;

public class FirebaseInstanceDatabase {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();


    public MutableLiveData<Boolean> addOrderRequest(OrderRequest orderRequest){
        final MutableLiveData<Boolean> successOrderRequest = new MutableLiveData<>();

        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).push().setValue(orderRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successOrderRequest.setValue(true);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successOrderRequest.setValue(false);
            }
        });

        return successOrderRequest;
    }
    public MutableLiveData<Boolean> addProviderLocation(int orderId,ProviderLocation providerLocation){
        final MutableLiveData<Boolean> successAddProviderLocation = new MutableLiveData<>();

        firebaseDatabase.getReference(DatabaseReferenceName.PROVIDER_LOCATION).child(String.valueOf(orderId)).setValue(providerLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                successAddProviderLocation.setValue(true);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successAddProviderLocation.setValue(false);
            }
        });

        return successAddProviderLocation;
    }

    public MutableLiveData<DataSnapshot> fetchOrderRequest() {
        final MutableLiveData<DataSnapshot> fetchOrderRequest = new MutableLiveData<>();
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchOrderRequest.setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return fetchOrderRequest;
    }


    public LiveData<Boolean> addAccepOrRejecttInDatabase(String type,boolean accepted, DataSnapshot dataSnapshot) {
        final MutableLiveData<Boolean> successAddAcceptedOrRejected = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put(type, accepted);
        dataSnapshot.getRef().updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successAddAcceptedOrRejected.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successAddAcceptedOrRejected.setValue(false);
            }
        });

        return successAddAcceptedOrRejected;
    }
    public LiveData<Boolean> addTrueFalseInDatabase(String type,boolean accepted, int orderId) {
        final MutableLiveData<Boolean> successAddTrueFalseInDatabase = new MutableLiveData<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put(type, accepted);
        firebaseDatabase.getReference(DatabaseReferenceName.ORDER_REQUESTS).child(String.valueOf(orderId)).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successAddTrueFalseInDatabase.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successAddTrueFalseInDatabase.setValue(false);
            }
        });

        return successAddTrueFalseInDatabase;
    }

    public LiveData<DataSnapshot> fetchProviderLocation(int orderId) {
        final MutableLiveData<DataSnapshot> fetchProviderLocation = new MutableLiveData<>();
        firebaseDatabase.getReference(DatabaseReferenceName.PROVIDER_LOCATION).child(String.valueOf(orderId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fetchProviderLocation.setValue(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return fetchProviderLocation;
    }
}
