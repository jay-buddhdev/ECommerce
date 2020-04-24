package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkout;
    private TextView total_price_checkout;
    int  OverralTotalPrice=0;
    String totalprice="";
    private ProgressDialog loadingBar;
    int oneTypeProductTPrice=0;
    private ImageView empty;
    private RelativeLayout checkarea;
    private Button start_shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        loadingBar=new ProgressDialog(this);
        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        empty=findViewById(R.id.empty_cart);
        checkarea=findViewById(R.id.cart_relative);
        checkout = findViewById(R.id.cart_checkout);
        start_shopping=findViewById(R.id.cart_start_shopping);
        total_price_checkout = findViewById(R.id.cart_total_price);

        datafetch();



    }

    private void datafetch()
    {


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
DatabaseReference cart=FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price "+model.getPrice()+"$");
                        Picasso.get().load(model.getImage()).into(holder.productImage);
                        oneTypeProductTPrice=Integer.parseInt(model.getPrice())*Integer.parseInt(model.getQuantity());
                        OverralTotalPrice += oneTypeProductTPrice;


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        if(i==0)
                                        {
                                            Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                            intent.putExtra("key",model.getKey());
                                            startActivity(intent);

                                        }
                                        if(i==1)
                                        {
                                            cartListRef.child("User View")
                                                    .child(Prevalent.currentonlineusers.getPhone())
                                                    .child("Products")
                                                    .child(model.getKey())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(CartActivity.this, "Item Removed From Cart", Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.show();


                            }
                        });
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
                 if(dataSnapshot.exists())
                 {
                     recyclerView.setVisibility(View.VISIBLE);
                     empty.setVisibility(View.INVISIBLE);


                 }
                 else
                 {
                    checkarea.setVisibility(View.INVISIBLE);
                    start_shopping.setVisibility(View.VISIBLE);
                    start_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(CartActivity.this,HomeActivity.class);
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
        loadingBar.dismiss();
        display(oneTypeProductTPrice);
        totalprice=String.valueOf(OverralTotalPrice);

        total_price_checkout.setText(totalprice);
    }

    private void display(int oneTypeProductTPrice)
    {
        Toast.makeText(this, String.valueOf(oneTypeProductTPrice), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();




    }
}
