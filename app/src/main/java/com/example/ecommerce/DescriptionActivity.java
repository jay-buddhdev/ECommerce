package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DescriptionActivity extends AppCompatActivity {

    private TextView des;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        des=findViewById(R.id.description);
        String desc=getIntent().getStringExtra("des");
        des.setText(desc);

        back=findViewById(R.id.back_arrow_des);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DescriptionActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
