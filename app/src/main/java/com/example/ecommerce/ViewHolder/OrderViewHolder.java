package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtOrderid,txtOrderAmount,txtDate;
    public Button status;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderid=itemView.findViewById(R.id.order_id);
        txtDate=itemView.findViewById(R.id.order_date);
        txtOrderAmount=itemView.findViewById(R.id.order_total);
        status=itemView.findViewById(R.id.btn_status);
    }


    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
