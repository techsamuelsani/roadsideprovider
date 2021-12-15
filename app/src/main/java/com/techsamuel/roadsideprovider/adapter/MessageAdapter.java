package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.listener.MessageItemClickListener;
import com.techsamuel.roadsideprovider.model.MessageModel;
import com.techsamuel.roadsideprovider.tools.Tools;
import com.techsamuel.roadsideprovider.tools.ViewAnimation;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    MessageModel messageModel;
    private Context ctx;
    private final MessageItemClickListener listener;


    public MessageAdapter(Context context, MessageModel messageModel, MessageItemClickListener listener) {
        this.messageModel = messageModel;
        ctx = context;
        this.listener=listener;

    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView messageImage;
        public TextView messageTitle;
        public TextView messageDetails;
        private NestedScrollView nested_scroll_view;
        private ImageButton bt_toggle_text, bt_toggle_input;
        private Button bt_hide_text, bt_save_input, bt_hide_input;
        private View lyt_expand_text, lyt_expand_input;

        public OriginalViewHolder(View v) {
            super(v);
            messageImage=v.findViewById(R.id.message_image);
            messageTitle=v.findViewById(R.id.message_title);
            messageDetails=v.findViewById(R.id.message_body);

            nested_scroll_view=v.findViewById(R.id.nested_scroll_view);
            bt_toggle_text=v.findViewById(R.id.bt_toggle_text);
            //bt_toggle_input=v.findViewById(R.id.bt_toggle_input);
            bt_hide_text=v.findViewById(R.id.bt_hide_text);
            //bt_save_input=v.findViewById(R.id.bt_save_input);
           // bt_hide_input=v.findViewById(R.id.bt_hide_input);
            lyt_expand_text=v.findViewById(R.id.lyt_expand_text);
            //lyt_expand_input=v.findViewById(R.id.lyt_expand_input);


        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.messageTitle.setText(messageModel.getData().get(0).get(position).getMessageTitle());
            view.messageDetails.setText(messageModel.getData().get(0).get(position).getMessageDetails());

            view.lyt_expand_text.setVisibility(View.GONE);

           view.bt_hide_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleSectionText(view,holder);

                    }
                });
           view.bt_toggle_text.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   toggleSectionText(v,holder);

               }
           });


            String imageUrl=Config.BASE_URL+messageModel.getData().get(0).get(position).getMessageImage();
            Glide.with(ctx).load(imageUrl).into(view.messageImage);



        }
    }

    private void toggleSectionText(View view,RecyclerView.ViewHolder holder) {
        OriginalViewHolder v = (OriginalViewHolder) holder;
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(v.lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(v.nested_scroll_view, v.lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(v.lyt_expand_text);
        }
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return messageModel.getData().get(0).size();
    }



}

