package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Chekout_Activity extends AppCompatActivity  {
private EditText str1,str2,city,state,country;
private String Amount;
private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chekout);

        str1=findViewById(R.id.street1_editxt);
        str2=findViewById(R.id.street2_editxt);
        city=findViewById(R.id.city_editxt);
        state=findViewById(R.id.states_editxt);
        country=findViewById(R.id.country_editxt);
        next=findViewById(R.id.next_btn);
        Amount=getIntent().getStringExtra("Total_amount");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(str1.getText()))
                {
                    Toast.makeText(Chekout_Activity.this, "Please Enter Street Address 1", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(str2.getText()))
                {
                    Toast.makeText(Chekout_Activity.this, "Please Enter Street Address 2", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(city.getText()))
                {Toast.makeText(Chekout_Activity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Chekout_Activity.this, "Please Enter City", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(state.getText()))
                {
                    Toast.makeText(Chekout_Activity.this, "Please Enter State", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(country.getText()))
                {
                    Toast.makeText(Chekout_Activity.this, "Please Enter Country", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   String final_address=str1.getText().toString()+","+str2.getText().toString()+","+city.getText().toString()+","+state.getText().toString()+","+country.getText().toString();
                    Toast.makeText(Chekout_Activity.this, final_address, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Chekout_Activity.this,Chekout_Summary_Activity.class);
                    i.putExtra("Amount",Amount);
                    i.putExtra("address",final_address);
                    startActivity(i);
                }
            }
        });





    }
}
