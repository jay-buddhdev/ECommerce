package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommerce.Models.User_Order_Ph_Order_Id;
import com.example.ecommerce.ViewHolder.User_Order_Ph_Order_IdViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Order_ID_Activity extends AppCompatActivity {
    private DatabaseReference OrderUserRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView cusname;
    FirebaseRecyclerAdapter<User_Order_Ph_Order_Id, User_Order_Ph_Order_IdViewHolder> adapter;
    String Key=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__order__i_d);

        recyclerView=findViewById(R.id.recycler_User_Order_Id);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        cusname=findViewById(R.id.toolbar_name);

        Key=getIntent().getStringExtra("key");
        cusname.setText(Key);
        OrderUserRef= FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child(Key);

        fetchData(Key);


    }

    private void fetchData(final String key)
    {
        FirebaseRecyclerOptions<User_Order_Ph_Order_Id> options=new FirebaseRecyclerOptions.Builder<User_Order_Ph_Order_Id>().setQuery(OrderUserRef,User_Order_Ph_Order_Id.class).build();

        adapter=new FirebaseRecyclerAdapter<User_Order_Ph_Order_Id, User_Order_Ph_Order_IdViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull User_Order_Ph_Order_IdViewHolder holder, final int position, @NonNull User_Order_Ph_Order_Id model) {
                holder.txtphoneNumber.setText(adapter.getRef(position).getKey());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(Admin_Order_ID_Activity.this,Admin_Order_Details_Activity.class);
                        i.putExtra("Product_key",adapter.getRef(position).getKey());
                        i.putExtra("user_phone",key);
                        startActivity(i);
                        finish();
                    }
                });

            }

            @NonNull
            @Override
            public User_Order_Ph_Order_IdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ph_all_orders_layout,parent,false);
                User_Order_Ph_Order_IdViewHolder holder= new User_Order_Ph_Order_IdViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}
