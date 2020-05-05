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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Orders_Admin_Details;
import com.example.ecommerce.Models.User_Order_Product_Detail;
import com.example.ecommerce.ViewHolder.User_Order_Ph_DetailsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Admin_Order_Details_Activity extends AppCompatActivity {

    private DatabaseReference OrderUserRef_Recycler;
    private DatabaseReference OrderUserRef_Recycler_User;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String Product_key=null;
    String user_phone=null;
    private TextView Customer_address,Order_dt,Order_Amount,Order_Type;
    private Button confirmed,packed,shipping,complete;
    private TextView cusname;
    private ImageView back;
    FirebaseRecyclerAdapter<User_Order_Product_Detail, User_Order_Ph_DetailsViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__order__details);

        Customer_address=findViewById(R.id.customer_address);
        Order_dt=findViewById(R.id.order_date_time);
        Order_Amount=findViewById(R.id.order_amount);
        Order_Type=findViewById(R.id.order_type);

        confirmed=findViewById(R.id.btn_confirmed);
        packed=findViewById(R.id.btn_packing);
        shipping=findViewById(R.id.btn_shipped);
        complete=findViewById(R.id.btn_complete);

        back=findViewById(R.id.back_arrow_admin_order);


        Product_key=getIntent().getStringExtra("Product_key");
        user_phone=getIntent().getStringExtra("user_phone");
        recyclerView=findViewById(R.id.recycler_order_details);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OrderUserRef_Recycler= FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child(user_phone).child(Product_key);
        OrderUserRef_Recycler_User=FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child(user_phone).child(Product_key);
        cusname=findViewById(R.id.toolbar_name);
        cusname.setText(user_phone);
        fetchData();
        getOrderDetails();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Admin_Order_Details_Activity.this,Admin_Order_ID_Activity.class);
                i.putExtra("key",Product_key);
                startActivity(i);
                finish();
            }
        });
    }

    private void getOrderDetails()
    {
        OrderUserRef_Recycler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Orders_Admin_Details orders=dataSnapshot.getValue(Orders_Admin_Details.class);
                    Customer_address.setText(orders.getAddress());
                    Order_dt.setText(orders.getDate()+" "+orders.getTime());
                    Order_Amount.setText(orders.getAmount());
                    Order_Type.setText(orders.getType());
                    String status=orders.getStatus();
                    if(status.equals("Placed"))
                    {
                        confirmed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderUserRef_Recycler.child("Status").setValue("Confirmed")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                OrderUserRef_Recycler_User.child("Status").setValue("Confirmed")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                    else if(status.equals("Confirmed"))
                    {
                        confirmed.setEnabled(false);
                        packed.setEnabled(true);
                        packed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderUserRef_Recycler.child("Status").setValue("Packed")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               // Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                OrderUserRef_Recycler_User.child("Status").setValue("Packed")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                    else if(status.equals("Packed"))
                    {
                        confirmed.setEnabled(false);
                        packed.setEnabled(false);
                        shipping.setEnabled(true);
                        shipping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderUserRef_Recycler.child("Status").setValue("Shipped")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                              //  Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                OrderUserRef_Recycler_User.child("Status").setValue("Shipped")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });


                    }else if(status.equals("Shipped"))
                    {
                        confirmed.setEnabled(false);
                        shipping.setEnabled(false);
                        complete.setEnabled(true);
                        complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OrderUserRef_Recycler.child("Status").setValue("Delivered")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               // Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                OrderUserRef_Recycler_User.child("Status").setValue("Delivered")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Admin_Order_Details_Activity.this, "Order Status Updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                    else
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
