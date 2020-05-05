package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Models.Orders_View;
import com.example.ecommerce.Models.Orders_track;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Order_Track_Activity extends AppCompatActivity {

    private ImageView line,placed,confirm_uncheck,confirm_check,packed_uncheck,packed_check,shipped_uncheck,shipped_check,delivered_uncheck,delivered_check;
   String key=null;
   ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__track);

        line=findViewById(R.id.line_track);
        placed=findViewById(R.id.imageView_orderplaced);
        confirm_check=findViewById(R.id.imageView_order_confirmed_check);
        confirm_uncheck=findViewById(R.id.imageView_order_confirmed_uncheck);
        packed_uncheck=findViewById(R.id.imageView_order_packed_uncheck);
        packed_check=findViewById(R.id.imageView_order_packed_check);
        shipped_uncheck=findViewById(R.id.imageView_order_shipped_uncheck);
        shipped_check=findViewById(R.id.imageView_order_shipped_check);
        delivered_uncheck=findViewById(R.id.imageView_order_complete_uncheck);
        delivered_check=findViewById(R.id.imageView_order_complete_check);
        back=findViewById(R.id.back_arrow_order_track);
        key=getIntent().getStringExtra("key");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Order_Track_Activity.this,Order_View_Activity.class);
                startActivity(i);
                finish();
            }
        });
        getOrderdetail(key);





    }

    private void getOrderdetail(String key)
    {
        DatabaseReference OrderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child(Prevalent.currentonlineusers.getPhone());
        OrderRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Orders_track orders=dataSnapshot.getValue(Orders_track.class);
                    String Status=orders.getStatus();
                    Toast.makeText(Order_Track_Activity.this,orders.getStatus(), Toast.LENGTH_SHORT).show();
                    if(Status.equals("Confirmed"))
                    {
                        confirm_check.setVisibility(View.VISIBLE);
                    }
                    else if(Status.equals("Packed"))
                    {
                        confirm_check.setVisibility(View.VISIBLE);
                        packed_check.setVisibility(View.VISIBLE);
                    }
                    else if(Status.equals("Shipped"))
                    {
                        confirm_check.setVisibility(View.VISIBLE);
                        packed_check.setVisibility(View.VISIBLE);
                        shipped_check.setVisibility(View.VISIBLE);
                    }
                    else if(Status.equals("Delivered"))
                    {
                        confirm_check.setVisibility(View.VISIBLE);
                        packed_check.setVisibility(View.VISIBLE);
                        shipped_check.setVisibility(View.VISIBLE);
                        delivered_check.setVisibility(View.VISIBLE);
                    }
                    else
                    {

                        Toast.makeText(Order_Track_Activity.this, "Here", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
