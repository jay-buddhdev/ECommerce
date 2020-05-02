package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Chekout_Summary_Activity extends AppCompatActivity implements PaymentResultListener
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    private TextView address,amount_dis;
    private Button cod,pay;
    private DatabaseReference Orderef;
    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
    private  String  amount=null;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chekout__summary);
        recyclerView = findViewById(R.id.recycler_summary);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        address=findViewById(R.id.address_summary);
        cod=findViewById(R.id.cod_btn_summary);
        pay=findViewById(R.id.pay_btn_summary);
        amount_dis=findViewById(R.id.txtview_amount);
        back=findViewById(R.id.back_arrow_summary);

        Checkout.preload(getApplicationContext());
        Toast.makeText(this, Prevalent.currentonlineusers.getPhone(), Toast.LENGTH_SHORT).show();
        Orderef= FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child(Prevalent.currentonlineusers.getPhone()).push();
        address.setText(getIntent().getStringExtra("address"));
        amount=getIntent().getStringExtra("Amount");
        amount_dis.setText("Rs. "+amount);
        datafetch();
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inserInOrder("COD");


            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupPayment(amount);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Chekout_Summary_Activity.this,Chekout_Activity.class);
                startActivity(i);
            }
        });
    }

    private void inserInOrder(String type)
    {
        String saveCurrentTime,saveCurrentDate;
        Calendar calforDate= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTime=currentTime.format(calforDate.getTime());
        final HashMap<String,Object> OrderMap=new HashMap<>();
        OrderMap.put("date",saveCurrentDate);
        OrderMap.put("time",saveCurrentTime);
        OrderMap.put("address",address.getText().toString());
        OrderMap.put("type",type.toString());
        OrderMap.put("Amount",amount);
        OrderMap.put("Status","Placed");
        Orderef.updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    String key=Orderef.getKey();

                    //To Remove Data From Cart node of Admin View
                    final DatabaseReference cartListRef1_admin=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
                    //To Fetch Data From Cart node of Admin View
                    final DatabaseReference cartListRef_admin=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentonlineusers.getPhone()).child("Products");

                   // To Remove Data from the Cart of User View
                    final DatabaseReference cartListRef1=FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");

                    //To Fetch Data From Cart node of User View
                    final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentonlineusers.getPhone()).child("Products");
                    //This is used to add Product to User View From Cart node of User View
                    final DatabaseReference toOrderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child(Prevalent.currentonlineusers.getPhone()).child(key).child("Products");


                    // This is used to add Product to Admin View From Cart node of Admin View
                    final DatabaseReference toOrderRef_admin=FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child(Prevalent.currentonlineusers.getPhone()).child(key).child("Products");


                   // This is used to add Hashmap details to Admin View Node
                    final DatabaseReference toOrderRef_admin_add_details=FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child(Prevalent.currentonlineusers.getPhone()).child(key);
                    cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for(DataSnapshot cartListRef:dataSnapshot.getChildren())
                                {
                                    String cartkey=cartListRef.getKey();
                                    String pname=cartListRef.child("pname").getValue(String.class);
                                    String price=cartListRef.child("price").getValue(String.class);
                                    String quantity=cartListRef.child("quantity").getValue(String.class);
                                    String image=cartListRef.child("image").getValue(String.class);
                                    toOrderRef.child(cartkey).child("pname").setValue(pname);
                                    toOrderRef.child(cartkey).child("price").setValue(price);
                                    toOrderRef.child(cartkey).child("quantity").setValue(quantity);
                                    toOrderRef.child(cartkey).child("image").setValue(image);

                                    cartListRef1.child(cartkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            cartListRef_admin.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists())
                                                    {
                                                        for(DataSnapshot cartListRef_admin:dataSnapshot.getChildren())
                                                        {
                                                            String cartkey=cartListRef_admin.getKey();
                                                            String pname=cartListRef_admin.child("pname").getValue(String.class);
                                                            String price=cartListRef_admin.child("price").getValue(String.class);
                                                            String quantity=cartListRef_admin.child("quantity").getValue(String.class);
                                                            String image=cartListRef_admin.child("image").getValue(String.class);
                                                            toOrderRef_admin.child(cartkey).child("pname").setValue(pname);
                                                            toOrderRef_admin.child(cartkey).child("price").setValue(price);
                                                            toOrderRef_admin.child(cartkey).child("quantity").setValue(quantity);
                                                            toOrderRef_admin.child(cartkey).child("image").setValue(image);
                                                            cartListRef1_admin.child(cartkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            });
                                                        }

                                                        toOrderRef_admin_add_details.updateChildren(OrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });

                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

     //   genrateNotification();
    }

    private void genrateNotification()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=
                    new NotificationChannel("MyNotification","MyNotification",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle("Order Has Been Placed")
                .setAutoCancel(true)
                .setContentText("Dear"+Prevalent.currentonlineusers.getName()+" Your Order Has been Placed and Shipped Soon");

        NotificationManagerCompat manger=NotificationManagerCompat.from(this);
        manger.notify(999,builder.build());
    }

    private void setupPayment(String amount)
    {
        final Activity activity=this;
        final Checkout checkout=new Checkout();

        JSONObject object=new JSONObject();

        try
        {
            object.put("name","Ecommerce");
            object.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            object.put("currency", "INR");
            int amount_int=Integer.parseInt(amount);
            amount_int=amount_int*100;

            object.put("amount", amount_int);
            JSONObject preFill=new JSONObject();
            preFill.put("email",Prevalent.currentonlineusers.getEmail());
            preFill.put("phone",Prevalent.currentonlineusers.getPhone());
            object.put("prefill",preFill);

            checkout.open(activity, object);
        }
        catch (Exception e)
        {
            Toast.makeText(Chekout_Summary_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void datafetch()
    {
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"), Cart.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                Picasso.get().load(model.getImage()).into(holder.productImage);

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }


        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onPaymentSuccess(String Razorpay_id) {
        inserInOrder(Razorpay_id);
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Fail "+s, Toast.LENGTH_SHORT).show();

    }
}
