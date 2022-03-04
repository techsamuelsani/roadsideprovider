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
import com.techsamuel.roadsideprovider.listener.PageItemClickListener;
import com.techsamuel.roadsideprovider.model.PageModel;

public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    PageModel pageModel;
    private Context ctx;
    private final PageItemClickListener listener;


    public PageAdapter(Context context, PageModel pageModel, PageItemClickListener listener) {
        this.pageModel = pageModel;
        ctx = context;
        this.listener=listener;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView pageIcon;
        public TextView pageTitle;
        public LinearLayout lytPage;


        public OriginalViewHolder(View v) {
            super(v);
            pageIcon=v.findViewById(R.id.page_icon);
            pageTitle=v.findViewById(R.id.page_title);
            lytPage=v.findViewById(R.id.lyt_page);


        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.pageTitle.setText(pageModel.getData().get(0).get(position).getPageName());

            String imageUrl=Config.BASE_URL+pageModel.getData().get(0).get(position).getPageIcon();
            Glide.with(ctx).load(imageUrl).into(view.pageIcon);

            view.lytPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pageModel.getData().get(0).get(position));
                }
            });



        }
    }


    @Override
    public int getItemCount() {
        return pageModel.getData().get(0).size();
    }



}

