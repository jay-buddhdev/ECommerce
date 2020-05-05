package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Prevalent.AdminAddNewProductActivity;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Category_Products_Activity extends AppCompatActivity
{

    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private ImageView back;
    private String productCategory=null;
    Query query;
    ArrayList<String> category=new ArrayList<>();
    ArrayList<String> keys=new ArrayList<>();
    private TextView catname;
    final DatabaseReference productListRef = FirebaseDatabase.getInstance().getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__products_);

        recyclerView=findViewById(R.id.recycler_category_product);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        productCategory=getIntent().getStringExtra("pCategory");
        catname=findViewById(R.id.toolbar_cat_name);
        catname.setText(productCategory);
        back=findViewById(R.id.back_arrow_cat_admin);
        query = FirebaseDatabase.getInstance()
                .getReference("Products").orderByChild("category").equalTo(productCategory);

        getProducts();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Category_Products_Activity.this,Admin_edit_product_Activity.class);
                startActivity(i);
                finish();
            }
        });

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void getProducts()
    {
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(query,Products.class).build();

        adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, final int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = "+ model.getPrice() +"$");
                Picasso.get().load(model.getImage()).into(holder.imageView);
                keys.add(position,adapter.getRef(position).getKey());
                category.add(position,model.getCategory());



            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;

            }
        };

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
                    Intent intent = new Intent(Category_Products_Activity.this, Admin_Change_Product_Detail_Activity.class);
                    intent.putExtra("key", keys.get(viewHolder.getPosition()));
                    intent.putExtra("pCategory", category.get(viewHolder.getPosition()));
                    startActivity(intent);
                    finish();
                    break;

                case ItemTouchHelper.RIGHT:
                    adapter.notifyItemRemoved(viewHolder.getPosition());
                    productListRef.child(keys.get(viewHolder.getPosition()))
                        .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Category_Products_Activity.this, "Product Removed", Toast.LENGTH_SHORT).show();

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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Category_Products_Activity.this,R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.edit_item)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Category_Products_Activity.this,R.color.Red))
                    .addSwipeRightActionIcon(R.drawable.delete_item)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };
}
