package com.techsamuel.roadsideprovider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.OrderPhotoItemClickListener;

import java.util.List;

public class OrderPagePhotoGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> serviceImage;

    private Context ctx;
    private OrderPhotoItemClickListener listener;


    public OrderPagePhotoGridAdapter(Context context, List<String> serviceImage, OrderPhotoItemClickListener listener) {
        this.serviceImage = serviceImage;
        ctx = context;
        this.listener=listener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            String serviceUrl=Config.BASE_URL+serviceImage.get(position);
            Glide.with(ctx).load(serviceUrl).into(view.image);
            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(serviceUrl);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return serviceImage.size();
    }



}

