package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

public class RelatedProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public RelatedProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=(ImageView) itemView.findViewById(R.id.related_products_image);
        txtProductName=(TextView) itemView.findViewById(R.id.related_products_name);
        txtProductPrice=(TextView) itemView.findViewById(R.id.related_products_price);
    }
    public   void  setItemClickListener(ItemClickListner listener)
    {
        this.listner=listener;
    }

    @Override
    public void onClick(View v)
    {
        listner.onClick(v,getAdapterPosition(),false);

    }
}
