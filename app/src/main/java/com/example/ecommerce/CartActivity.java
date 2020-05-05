package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkout;
    private TextView total_price_checkout;
    int OverralTotalPrice = 0;
    String totalprice = "";
    int oneTypeProductTPrice = 0;
    private ImageView empty;
    private RelativeLayout checkarea;
    private Button start_shopping;
    ArrayList<String> keys=new ArrayList<>();
    ArrayList<String> category=new ArrayList<>();
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        empty = findViewById(R.id.empty_cart);
        checkarea = findViewById(R.id.cart_relative);
        checkout = findViewById(R.id.cart_checkout);
        start_shopping = findViewById(R.id.cart_start_shopping);
        total_price_checkout = findViewById(R.id.cart_total_price);
        back=findViewById(R.id.back_arrow_cart_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CartActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        datafetch();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CartActivity.this,Chekout_Activity.class);
                i.putExtra("Total_amount",totalprice);
                startActivity(i);
                finish();
            }
        });

    }

    private void datafetch() {



        DatabaseReference cart = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"), Cart.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price=" +"Rs."+ model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.productImage);
                oneTypeProductTPrice = (Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity()));
                OverralTotalPrice += oneTypeProductTPrice;
                keys.add(position,model.getKey());
                category.add(position,model.getCategory());

                if (position == adapter.getItemCount()-1) {
                    totalprice = String.valueOf(OverralTotalPrice);
                    total_price_checkout.setText("Rs."+totalprice);
                }

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }


        };

        cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.INVISIBLE);


                } else {
                    checkarea.setVisibility(View.INVISIBLE);
                    start_shopping.setVisibility(View.VISIBLE);
                    start_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(CartActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            switch (direction)
            {
                case ItemTouchHelper.LEFT:
                    adapter.notifyItemRemoved(viewHolder.getPosition());
                    Intent intent = new Intent(CartActivity.this, Cart_Edit_Activity.class);
                    intent.putExtra("key", keys.get(viewHolder.getPosition()));
                    intent.putExtra("pCategory", category.get(viewHolder.getPosition()));
                    startActivity(intent);
                    finish();
                    break;

                case ItemTouchHelper.RIGHT:
                    adapter.notifyItemRemoved(viewHolder.getPosition());
                    final String keyad=keys.get(viewHolder.getPosition());
                    cartListRef.child("User View")
                            .child(Prevalent.currentonlineusers.getPhone())
                            .child("Products")
                            .child( keys.get(viewHolder.getPosition()))
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        cartListRef.child("Admin View")
                                                .child(Prevalent.currentonlineusers.getPhone())
                                                .child("Products")
                                                .child(keyad)
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(CartActivity.this, "Item Removed From Cart", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                    }

                                }
                            });
                    break;
            }

        }
        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
        {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this,R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.edit_item)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(CartActivity.this,R.color.Red))
                    .addSwipeRightActionIcon(R.drawable.delete_item)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };


}
