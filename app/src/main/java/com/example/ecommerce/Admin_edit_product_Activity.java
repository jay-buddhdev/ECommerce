package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Admin_edit_product_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navgation);
        bottomNavigationView.setSelectedItemId(R.id.view_Products);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.add_Products:
                        startActivity(new Intent(getApplicationContext(),Admin_add_product_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
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
    }
}
