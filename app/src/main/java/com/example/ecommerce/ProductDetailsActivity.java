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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Models.RelatedProducts;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.ViewHolder.RelatedProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{
    private ImageView productimage;
    private TextView productPrice,productName;
    private String key="";
    private String productCategory="";
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ProductRelated;
    FirebaseRecyclerAdapter<Products, RelatedProductViewHolder> adapter;
    DatabaseReference request;
    Query query;
    FirebaseDatabase database;
    private Button addToCart,buyNow;
    ElegantNumberButton numberButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);
        key=getIntent().getStringExtra("key");
        productCategory=getIntent().getStringExtra("pCategory");
        productimage=findViewById(R.id.product_detail_imageview);
        productPrice=findViewById(R.id.product_detail_price);
        productName=findViewById(R.id.product_detail_name);

        addToCart=findViewById(R.id.product_detail_add_cart);
        buyNow=findViewById(R.id.product_detail_buy_now);
        numberButton=findViewById(R.id.product_detail_quantity);
       // ProductRelated=  FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").equalTo(getIntent().getStringExtra("pCategory"));
        getProductDetails(key);

       recyclerView=findViewById(R.id.recycler_related_product);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);

        query = FirebaseDatabase.getInstance()
                .getReference("Products").orderByChild("category").equalTo(productCategory);
        Toast.makeText(this, productCategory, Toast.LENGTH_SHORT).show();
        getRelatedProducts(productCategory);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartList();
            }
        });

    }

    private void addToCartList()
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar calforDate= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTime=currentDate.format(calforDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("key",key);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentonlineusers.getPhone())
                .child("Products").child(key)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentonlineusers.getPhone())
                                    .child("Products").child(key)
                                    .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                        startActivity(i);
                                    }

                                }
                            });
                        }
                    }
                });



    }

    private void getRelatedProducts(String productCategory)
    {
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(query,Products.class).build();

       adapter=new FirebaseRecyclerAdapter<Products, RelatedProductViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull RelatedProductViewHolder holder, final int position, @NonNull final Products model) {
               holder.txtProductName.setText(model.getPname());
               holder.txtProductPrice.setText("Price = "+ model.getPrice() +" $");
               Picasso.get().load(model.getImage()).into(holder.imageView);
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i = new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
                       i.putExtra("key", adapter.getRef(position).getKey());
                       i.putExtra("pCategory",model.getCategory());
                       startActivity(i);
                       finish();
                   }
               });
           }


            @NonNull
            @Override
            public RelatedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details_all_products,parent,false);
                RelatedProductViewHolder holder=new RelatedProductViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void getProductDetails(final String key)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");
       // Toast.makeText(this, productCategory, Toast.LENGTH_SHORT).show();
        productsRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products products=dataSnapshot.getValue(Products.class);
                if(dataSnapshot.exists())
                {

                    productName.setText(products.getPname());
                    productPrice.setText("Price = "+products.getPrice()+" $");
                    Picasso.get().load(products.getImage()).into(productimage);

                }
                else
                {
                   // Toast.makeText(ProductDetailsActivity.this, products.getPname(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
