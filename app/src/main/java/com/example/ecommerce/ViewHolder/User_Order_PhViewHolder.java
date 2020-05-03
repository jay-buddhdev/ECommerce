package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class User_Order_PhViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtphoneNumber;
    private ItemClickListner itemClickListner;

    public User_Order_PhViewHolder(@NonNull View itemView) {
        super(itemView);
        txtphoneNumber=itemView.findViewById(R.id.recycler_ph_user_no);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
