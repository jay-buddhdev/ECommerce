package com.example.ecommerce.Prevalent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerce.AdminAddNewProductActivity;
import com.example.ecommerce.R;

public class AdminCategoryActivity extends AppCompatActivity  {

    private ImageView tshirts,sportsTshirts,femaleDresses,sweathers;
    private ImageView glasses,hatsCaps,walletsBagsPurses,shoes;
    private ImageView headPhonesHandsFree,Laptops,watches,mobilePhones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirts=(ImageView)findViewById(R.id.t_shirts);
        sportsTshirts=(ImageView)findViewById(R.id.sports_t_shirts);
        femaleDresses=(ImageView)findViewById(R.id.female_dresses);
        sweathers=(ImageView)findViewById(R.id.sweather);

        glasses=(ImageView)findViewById(R.id.glasses);
        hatsCaps=(ImageView)findViewById(R.id.hats_caps);
        walletsBagsPurses=(ImageView)findViewById(R.id.purses_bags_wallets);
        shoes=(ImageView)findViewById(R.id.shoes);

        headPhonesHandsFree=(ImageView)findViewById(R.id.headphones_handfree);
        Laptops=(ImageView)findViewById(R.id.laptop_pc);
        watches=(ImageView)findViewById(R.id.watches);
        mobilePhones=(ImageView)findViewById(R.id.mobilephones);





        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","tshirts");
                startActivity(i);
            }
        });
        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","sportsTshirts");
                startActivity(i);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Female Dresses");
                startActivity(i);
            }
        });
        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","sweathers");
                startActivity(i);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","glasses");
                startActivity(i);
            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","hatsCaps");
                startActivity(i);
            }
        });
        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Wallets Bags Purses");
                startActivity(i);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Shoes");
                startActivity(i);
            }
        });
        headPhonesHandsFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","HeadPhones HandsFree");
                startActivity(i);
            }
        });
        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Laptops");
                startActivity(i);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Watches");
                startActivity(i);
            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                i.putExtra("Category","Mobile Phones");
                startActivity(i);
            }
        });

    }


}
