package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtCategoryName;
    public CircleImageView CategoryImage;
    private ItemClickListner itemClickListner;

    public CategoryViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtCategoryName=itemView.findViewById(R.id.recycler_category_name);
        CategoryImage=itemView.findViewById(R.id.recycler_category_imageview);
    }


    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
