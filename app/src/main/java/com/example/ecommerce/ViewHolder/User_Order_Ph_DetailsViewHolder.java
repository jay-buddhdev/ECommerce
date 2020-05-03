package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class User_Order_Ph_DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    public ImageView productImage;
    private ItemClickListner itemClickListner;

    public User_Order_Ph_DetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.recycler_order_product_name);
        txtProductPrice=itemView.findViewById(R.id.recycler_order_product_price);
        productImage=itemView.findViewById(R.id.recycler_order_imageview);
        txtProductQuantity=itemView.findViewById(R.id.recycler_order_product_quantity);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
