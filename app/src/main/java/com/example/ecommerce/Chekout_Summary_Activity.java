package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Chekout_Summary_Activity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    private TextView address;
    private Button cod,pay;
    private DatabaseReference Orderef;
    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chekout__summary);
        recyclerView = findViewById(R.id.recycler_summary);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        address=findViewById(R.id.address_summary);
        cod=findViewById(R.id.cod_btn_summary);
        pay=findViewById(R.id.pay_btn_summary);
        Orderef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineusers.getPhone()).push();
        address.setText(getIntent().getStringExtra("address"));
        datafetch();
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveCurrentTime,saveCurrentDate;
                Calendar calforDate= Calendar.getInstance();
                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
                saveCurrentDate=currentDate.format(calforDate.getTime());

                SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm::ss a");
                saveCurrentTime=currentTime.format(calforDate.getTime());
                HashMap<String,Object> OrderMap=new HashMap<>();
                OrderMap.put("date",saveCurrentDate);
                OrderMap.put("time",saveCurrentTime);
                OrderMap.put("address",address.getText().toString());
                Orderef.updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            String key=Orderef.getKey();
                            DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
                            final DatabaseReference toOrderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineusers.getPhone()).child(key).child("Products");
                            cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for(DataSnapshot cartListRef:dataSnapshot.getChildren())
                                        {
                                            String cartkey=cartListRef.getKey();
                                            String pname=cartListRef.child("pname").getValue(String.class);
                                            String price=cartListRef.child("price").getValue(String.class);
                                            String quantity=cartListRef.child("quantity").getValue(String.class);
                                            String image=cartListRef.child("image").getValue(String.class);
                                            toOrderRef.child(cartkey).child("pname").setValue(pname);
                                            toOrderRef.child(cartkey).child("price").setValue(price);
                                            toOrderRef.child(cartkey).child("quantity").setValue(quantity);
                                            toOrderRef.child(cartkey).child("image").setValue(image);




                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });


            }
        });
    }

    private void datafetch()
    {
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"), Cart.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                Picasso.get().load(model.getImage()).into(holder.productImage);

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
