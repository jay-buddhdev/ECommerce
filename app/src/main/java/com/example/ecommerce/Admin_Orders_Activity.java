package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Models.Category;
import com.example.ecommerce.Models.User_Order_Ph;
import com.example.ecommerce.ViewHolder.CategoryViewHolder;
import com.example.ecommerce.ViewHolder.User_Order_PhViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Orders_Activity extends AppCompatActivity {

    private DatabaseReference OrderUserRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<User_Order_Ph, User_Order_PhViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__orders);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navgation);
        bottomNavigationView.setSelectedItemId(R.id.Orders);

        recyclerView=findViewById(R.id.recycler_admin_user_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.home_dashboard:
                        startActivity(new Intent(getApplicationContext(),Admin_Home_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.view_Products:
                        startActivity(new Intent(getApplicationContext(),Admin_edit_product_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Orders:
                        return true;
                }


                return false;
            }
        });

        OrderUserRef= FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View");

        fetchData();
    }

    private void fetchData()
    {
        FirebaseRecyclerOptions<User_Order_Ph> options=new FirebaseRecyclerOptions.Builder<User_Order_Ph>().setQuery(OrderUserRef,User_Order_Ph.class).build();

        adapter=new FirebaseRecyclerAdapter<User_Order_Ph, User_Order_PhViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull User_Order_PhViewHolder holder, final int position, @NonNull User_Order_Ph model) {
                holder.txtphoneNumber.setText(adapter.getRef(position).getKey());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(Admin_Orders_Activity.this,Admin_Order_ID_Activity.class);
                        i.putExtra("key",adapter.getRef(position).getKey());
                        startActivity(i);
                        finish();
                    }
                });
            }

            @NonNull
            @Override
            public User_Order_PhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ph_orders_layout,parent,false);
                User_Order_PhViewHolder holder=new User_Order_PhViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
