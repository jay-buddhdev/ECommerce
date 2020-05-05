package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.Models.Orders_View;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Order_View_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Orders_View, OrderViewHolder> adapter;
    private DatabaseReference Orderef;
    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");
    Query query;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__view);
        recyclerView = findViewById(R.id.recycler_order);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        back=findViewById(R.id.back_arrow_orders_View);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Order_View_Activity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        fetchData();
    }

    private void fetchData()
    {
        FirebaseRecyclerOptions<Orders_View> options=new FirebaseRecyclerOptions.Builder<Orders_View>().setQuery(cartListRef
                .child("User View").child(Prevalent.currentonlineusers.getPhone()),Orders_View.class).build();

        adapter=new FirebaseRecyclerAdapter<Orders_View, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull Orders_View model)
            {
                holder.txtDate.setText(model.getDate());
                holder.txtOrderAmount.setText("Rs."+model.getAmount());
                holder.txtOrderid.setText("Order Id:"+adapter.getRef(position).getKey());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(Order_View_Activity.this,Order_Track_Activity.class);
                        i.putExtra("key",adapter.getRef(position).getKey());
                        startActivity(i);

                    }
                });
              //  holder.status.setText(model.getStatus());

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view_layout,parent,false);
                OrderViewHolder holder=new OrderViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
