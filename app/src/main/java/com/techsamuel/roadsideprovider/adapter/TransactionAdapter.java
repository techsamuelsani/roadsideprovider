package com.techsamuel.roadsideprovider.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techsamuel.roadsideprovider.Config;
import com.techsamuel.roadsideprovider.R;
import com.techsamuel.roadsideprovider.model.SettingsModel;
import com.techsamuel.roadsideprovider.model.TransactionModel;
import com.techsamuel.roadsideprovider.tools.AppSharedPreferences;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    TransactionModel transactionModel;
    SettingsModel settingsModel;

    private Context ctx;


    public TransactionAdapter(Context context, TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
        ctx = context;
        AppSharedPreferences.init(context);
        settingsModel=AppSharedPreferences.readSettingsModel(Config.SHARED_PREF_SETTINGS_MODEL,"");


    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView creditImage;
        public ImageView debitImage;
        public TextView date;
        public TextView paymentType;
        public CardView lytPayment;
        TextView amount;

        public OriginalViewHolder(View v) {
            super(v);
            creditImage=v.findViewById(R.id.creditImage);
            debitImage=v.findViewById(R.id.debitImage);
            date=v.findViewById(R.id.date);
            paymentType=v.findViewById(R.id.payment_type);
            lytPayment=v.findViewById(R.id.lyt_payment);
            amount=v.findViewById(R.id.amount);


        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            view.date.setText(transactionModel.getData().get(position).getDate());
            view.paymentType.setText(transactionModel.getData().get(position).getPaymentType());
            view.amount.setText(transactionModel.getData().get(position).getPrice()+" "+settingsModel.getData().getAppCurrency());

            if(transactionModel.getData().get(position).getPriceType().equals("credit")){
                view.amount.setTextColor(ctx.getResources().getColor(R.color.green_700));
                view.debitImage.setVisibility(View.GONE);
                view.creditImage.setVisibility(View.VISIBLE);
            }else{
                view.amount.setTextColor(ctx.getResources().getColor(R.color.red_700));
                view.debitImage.setVisibility(View.VISIBLE);
                view.creditImage.setVisibility(View.GONE);
            }




        }
    }

    @Override
    public int getItemCount() {
        return transactionModel.getData().size();
    }



}

