package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.OrderItemClickListener;
import com.techsamuel.roadsideprovider.model.OrdersModel;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OrdersModel ordersModel;
    private Context ctx;
    private final OrderItemClickListener listener;
    SettingsModel settingsModel;


    public OrderAdapter(Context context, OrdersModel ordersModel, OrderItemClickListener listener) {
        this.ordersModel = ordersModel;
        ctx = context;
        this.listener=listener;
        AppSharedPreferences.init(context);
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView orderImage;
        public TextView orderDescription;
        public CircleImageView storeLogo;
        public TextView storeName;
        public TextView orderStatus;
        public TextView orderPrice;
        public TextView orderDate;
        public LinearLayout lytOrder;

        public OriginalViewHolder(View v) {
            super(v);
            orderImage=v.findViewById(R.id.order_image);
            orderDescription=v.findViewById(R.id.order_description);
            storeLogo=v.findViewById(R.id.store_logo);
            storeName=v.findViewById(R.id.store_name);
            orderStatus=v.findViewById(R.id.order_status);
            orderPrice=v.findViewById(R.id.order_price);
            orderDate=v.findViewById(R.id.order_date);
            lytOrder=v.findViewById(R.id.lyt_order);


        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Glide.with(ctx).load(Config.BASE_URL+ordersModel.getOrderDetails().get(position).getServicesImages().get(0)).into(view.orderImage);
            Glide.with(ctx).load(Config.BASE_URL+ordersModel.getOrderDetails().get(0).getUserImage()).into(view.storeLogo);

            view.lytOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(ordersModel.getData().get(position));
                }
            });

            view.orderDescription.setText(ordersModel.getOrderDetails().get(position).getServiceDescription());
            view.orderStatus.setText(ordersModel.getData().get(position).getStatus());
            view.orderDate.setText(ordersModel.getOrderDetails().get(position).getDate());
            view.orderPrice.setText(settingsModel.getData().getCurrencySymbol()+" "+ordersModel.getData().get(position).getPrice());
            view.storeName.setText(ordersModel.getOrderDetails().get(position).getUserName());

        }
    }


    @Override
    public int getItemCount() {
        return ordersModel.getData().size();
    }



}

