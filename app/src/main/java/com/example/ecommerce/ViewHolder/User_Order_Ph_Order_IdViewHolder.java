package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class User_Order_Ph_Order_IdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtphoneNumber;
    private ItemClickListner itemClickListner;

    public User_Order_Ph_Order_IdViewHolder(@NonNull View itemView) {
        super(itemView);
        txtphoneNumber=itemView.findViewById(R.id.recycler_ph_order_id);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
