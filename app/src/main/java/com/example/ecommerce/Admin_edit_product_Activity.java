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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Models.Category;
import com.example.ecommerce.Models.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CategoryViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Admin_edit_product_Activity extends AppCompatActivity {

    private DatabaseReference CategoryRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    ArrayList<String> category=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        recyclerView=findViewById(R.id.recycler_admin_category);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navgation);
        bottomNavigationView.setSelectedItemId(R.id.view_Products);

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
                        return true;
                    case R.id.Orders:
                        startActivity(new Intent(getApplicationContext(),Admin_Orders_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }


                return false;
            }
        });

        CategoryRef= FirebaseDatabase.getInstance().getReference().child("Category");

        fetchData();
    }

    private void fetchData()
    {
        FirebaseRecyclerOptions<Category> options=new FirebaseRecyclerOptions.Builder<Category>().setQuery(CategoryRef,Category.class).build();

        adapter=new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model)
            {
                holder.txtCategoryName.setText(model.getCategoryName());
                Picasso.get().load(model.getImage()).into(holder.CategoryImage);
                category.add(position,model.getCategoryName());

            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
                CategoryViewHolder holder=new CategoryViewHolder(view);
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
                    Intent intent = new Intent(Admin_edit_product_Activity.this, Category_Products_Activity.class);
                    //intent.putExtra("key", keys.get(viewHolder.getPosition()));
                    intent.putExtra("pCategory", category.get(viewHolder.getPosition()));
                    startActivity(intent);
                    finish();
                    break;

                case ItemTouchHelper.RIGHT:
                    adapter.notifyItemRemoved(viewHolder.getPosition());
                    CategoryRef.child(category.get(viewHolder.getPosition()))
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Admin_edit_product_Activity.this, "Category Removed", Toast.LENGTH_SHORT).show();

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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Admin_edit_product_Activity.this,R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.edit_item)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Admin_edit_product_Activity.this,R.color.Red))
                    .addSwipeRightActionIcon(R.drawable.delete_item)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    };
}
