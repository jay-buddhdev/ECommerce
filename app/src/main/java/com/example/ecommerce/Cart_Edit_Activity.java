package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Cart_Edit_Activity extends AppCompatActivity {

    private String key=null;
    private int num=0;
    private ImageView productimage;
    private TextView productname;
    private TextView productprice;
    private TextView closeTextBtn,saveBtn;
    ElegantNumberButton quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__edit);
        key=getIntent().getStringExtra("key");
        productimage=findViewById(R.id.cart_edit_image);
        productname=findViewById(R.id.cart_edit_product_name);
        productprice=findViewById(R.id.cart_edit_product_price);
        quantity=findViewById(R.id.cart_edit_quantity);
        closeTextBtn=findViewById(R.id.close_settings_btn);
        saveBtn=findViewById(R.id.update_account_settings_btn);

        productdetail(key);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateData(key);

            }
        });


    }

    private void updateData(String key)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("quantity",quantity.getNumber().toString());
        ref.child(Prevalent.currentonlineusers.getPhone()).child("Products").child(key).updateChildren(userMap);

        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        ref1.child(Prevalent.currentonlineusers.getPhone()).child("Products").child(key).updateChildren(userMap);
        startActivity(new Intent(Cart_Edit_Activity.this,HomeActivity.class));
        Toast.makeText(Cart_Edit_Activity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void productdetail(String key)
    {
        DatabaseReference proRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products").child(key);
        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String image=dataSnapshot.child("image").getValue().toString();
                    String pname=dataSnapshot.child("pname").getValue().toString();
                    String price=dataSnapshot.child("price").getValue().toString();
                    String quan=dataSnapshot.child("quantity").getValue().toString();
                    Picasso.get().load(image).into(productimage);
                    productname.setText(pname);
                    productprice.setText(price+"$");
                    quantity.setNumber(quan);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
