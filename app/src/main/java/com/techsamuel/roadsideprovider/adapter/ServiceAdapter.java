package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.model.ServiceModel;
import com.techsamuel.roadsideprovider.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ServiceModel serviceModel;

    private Context ctx;


    public ServiceAdapter(Context context, ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
        ctx = context;
        Config.ServiceModelData.clear();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            checkBox = (CheckBox) v.findViewById(R.id.service);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.checkBox.setText(serviceModel.getData().get(position).getName());
            view.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox)view;
                    if(checkBox.isChecked()){
                        Config.ServiceModelData.add(serviceModel.getData().get(position));

                    }else{
                        Config.ServiceModelData.remove(serviceModel.getData().get(position));

                    }
                   // Tools.showToast(ctx,String.valueOf(position));
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return serviceModel.getData().size();
    }



}

