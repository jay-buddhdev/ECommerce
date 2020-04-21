package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ecommerce.Models.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class IntroActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private Button joinNowButton,loginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        joinNowButton=(Button)findViewById(R.id.intro_sign_up_btn);
        loginButton=(Button)findViewById(R.id.intro_login_btn);
        loadingBar=new ProgressDialog(this);
        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i=new Intent(IntroActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!="" && UserPasswordKey!="")
        {
            if(!TextUtils.isEmpty(UserPhoneKey)&& !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please Wait..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        layoutOnboardingIndicators=findViewById(R.id.layoutOnboardingIndicator);

        setupOnboardingItems();
        ViewPager2 onboardingViewPager=findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicator();
       setCurrentOnboardingIndicator(0);
       onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });
    }
    private void setupOnboardingItems()
    {
        List<OnboardingItem> onboardingItems=new ArrayList<>();

        OnboardingItem itemorginalproduct=new OnboardingItem();
        itemorginalproduct.setTitle("ORIGINAL PRODUCT");
        itemorginalproduct.setDescription("Original with 1000 product from a lot of diffrent brand");
        itemorginalproduct.setImage(R.drawable.orginal_product);

        OnboardingItem itemfreeshipping=new OnboardingItem();
        itemfreeshipping.setTitle("FREE SHIPPING");
        itemfreeshipping.setDescription("Original with 1000 product from a lot of diffrent brand");
        itemfreeshipping.setImage(R.drawable.free_shipping);

        OnboardingItem itemfastdelivery=new OnboardingItem();
        itemfastdelivery.setTitle("FAST DELIVERY");
        itemfastdelivery.setDescription("Original with 1000 product from a lot of diffrent brand");
        itemfastdelivery.setImage(R.drawable.fast_delivery);

        onboardingItems.add(itemorginalproduct);
        onboardingItems.add(itemfreeshipping);
        onboardingItems.add(itemfastdelivery);

        onboardingAdapter=new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicator()
    {
        ImageView[] indicators=new ImageView[onboardingAdapter.getItemCount()];

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i=0;i<indicators.length;i++)
        {
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }
    private void setCurrentOnboardingIndicator(int index)
    {
        int childcount=layoutOnboardingIndicators.getChildCount();
        for(int i=0;i<childcount;i++)
        {
            ImageView imageView=(ImageView)layoutOnboardingIndicators.getChildAt(i);
            if(i== index)
            {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active)
                );
            }
            else
            {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive)
                );
            }
        }
    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child(phone).exists())
                {
                    Users userData=dataSnapshot.child("User").child(phone).getValue(Users.class);
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            Toast.makeText(IntroActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i=new Intent(IntroActivity.this,HomeActivity.class);
                            Prevalent.currentonlineusers=userData;
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(IntroActivity.this, "Your Password is Incorrect", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i=new Intent(IntroActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                    }
                }
                else
                {
                    Toast.makeText(IntroActivity.this, "Account with this"+phone+"Number Do not Exits", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
