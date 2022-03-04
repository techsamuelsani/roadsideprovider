package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.OrderServiceItemClickListener;
import com.techsamuel.roadsideprovider.model.OrderModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

import java.util.List;

public class OrderPageServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<OrderModel.ServiceDetail> serviceModel;
    private Context ctx;
    private final OrderServiceItemClickListener listener;
    SettingsModel settingsModel;


    public OrderPageServiceAdapter(Context context, List<OrderModel.ServiceDetail> serviceModel, OrderServiceItemClickListener listener) {
        this.serviceModel = serviceModel;
        ctx = context;
        this.listener=listener;
        AppSharedPreferences.init(context);
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName;
        public TextView servicePrice;
        public LinearLayout lytService;

        public OriginalViewHolder(View v) {
            super(v);
            serviceName=v.findViewById(R.id.service_name);
            servicePrice=v.findViewById(R.id.service_cost);
            lytService=v.findViewById(R.id.lyt_service);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderpage_service, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.serviceName.setText(serviceModel.get(position).getName());
            view.servicePrice.setText(settingsModel.getData().getCurrencySymbol()+" "+serviceModel.get(position).getPrice());

           view.lytService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Tools.showToast(ctx,serviceModel.getData().get(position).getId());
                        listener.onItemClick(serviceModel.get(position));
                    }
                });

        }
    }

    @Override
    public int getItemCount() {
        return serviceModel.size();
    }



}

