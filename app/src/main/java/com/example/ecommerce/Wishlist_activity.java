package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ecommerce.Models.Wish;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.WishViewHolder;
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

public class Wishlist_activity extends AppCompatActivity {

    private ImageView wishlist_empty, backarrow;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button explore;
    private RelativeLayout reative_empty;
    private TextView txtempty, txttagline;
    ArrayList<String> keys=new ArrayList<>();
    ArrayList<String> category=new ArrayList<>();
    FirebaseRecyclerAdapter<Wish, WishViewHolder> adapter;
    final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_activity);


        wishlist_empty = findViewById(R.id.empty_wishlist_image);
        backarrow = findViewById(R.id.back_arrow_wishlist);
        explore = findViewById(R.id.explore_btn);
        recyclerView = findViewById(R.id.recycler_wishlist);
        reative_empty = findViewById(R.id.wishlist_relative_empty);
        txtempty = findViewById(R.id.txt_empty_wishlist);
        txttagline = findViewById(R.id.txt_empty_wishlist_tagline);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataFetch();
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Wishlist_activity.this,HomeActivity.class);
                startActivity(i);

            }
        });

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void dataFetch() {

        DatabaseReference wish = FirebaseDatabase.getInstance().getReference().child("Wish List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
        FirebaseRecyclerOptions<Wish> options = new FirebaseRecyclerOptions.Builder<Wish>()
                .setQuery(wishListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"), Wish.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Wish, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, @NonNull final Wish model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                Picasso.get().load(model.getImage()).into(holder.productImage);
                keys.add(position,model.getKey());
                category.add(position,model.getCategory());

            }

            @NonNull
            @Override
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_item_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(view);
                return holder;
            }
        };

        wish.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.exists())
                {
                    recyclerView.setVisibility(View.INVISIBLE);
                    txttagline.setVisibility(View.VISIBLE);
                    txtempty.setVisibility(View.VISIBLE);
                    wishlist_empty.setVisibility(View.VISIBLE);

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
                    break;

                case ItemTouchHelper.RIGHT:
                    adapter.notifyItemRemoved(viewHolder.getPosition());
                    final String keyad=keys.get(viewHolder.getPosition());
                    wishListRef.child("User View")
                            .child(Prevalent.currentonlineusers.getPhone())
                            .child("Products")
                            .child(keys.get(viewHolder.getPosition()))
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        wishListRef.child("Admin View")
                                                .child(Prevalent.currentonlineusers.getPhone())
                                                .child("Products")
                                                .child(keyad)
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(Wishlist_activity.this, "Item Removed From Wish List", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Wishlist_activity.this, HomeActivity.class);
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Wishlist_activity.this,R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.edit_item)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Wishlist_activity.this,R.color.Red))
                    .addSwipeRightActionIcon(R.drawable.delete_item)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };
}
