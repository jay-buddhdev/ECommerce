package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Models.User_Order_Product_Detail;
import com.example.ecommerce.ViewHolder.User_Order_Ph_DetailsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Admin_Order_Details_Activity extends AppCompatActivity {

    private DatabaseReference OrderUserRef_Recycler;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String Product_key=null;
    String user_phone=null;
    FirebaseRecyclerAdapter<User_Order_Product_Detail, User_Order_Ph_DetailsViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__order__details);
        Product_key=getIntent().getStringExtra("Product_key");
        user_phone=getIntent().getStringExtra("user_phone");
        recyclerView=findViewById(R.id.recycler_order_details);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OrderUserRef_Recycler= FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child(user_phone).child(Product_key);

        fetchData();
    }

    private void fetchData()
    {
        FirebaseRecyclerOptions<User_Order_Product_Detail> options=new FirebaseRecyclerOptions.Builder<User_Order_Product_Detail>()
                .setQuery(OrderUserRef_Recycler.child("Products"),User_Order_Product_Detail.class).build();

        adapter=new FirebaseRecyclerAdapter<User_Order_Product_Detail, User_Order_Ph_DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull User_Order_Ph_DetailsViewHolder holder, int position, @NonNull User_Order_Product_Detail model)
            {
                holder.txtProductQuantity.setText(model.getQuantity());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText(model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.productImage);

            }

            @NonNull
            @Override
            public User_Order_Ph_DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_admin_layout,parent,false);
                User_Order_Ph_DetailsViewHolder holder=new User_Order_Ph_DetailsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
