package com.techsamuel.roadsideprovider.listener;

import com.techsamuel.roadsideprovider.model.OrderModel;

public interface OrderServiceItemClickListener {
    void onItemClick(OrderModel.ServiceDetail serviceDetail);
}
