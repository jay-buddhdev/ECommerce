package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Change_Product_Detail_Activity extends AppCompatActivity {
    private CircleImageView productImageView;
    private EditText pname,desciption,category,price;
    private TextView closeTextBtn,saveBtn;
    private ImageView profileChange;
    String key=null;
    private Uri imageUri;
    private String myUri="";
    private StorageTask uploadTask;
    private StorageReference storageProductpicRef;
    private String checked="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__change__product__detail);
        storageProductpicRef= FirebaseStorage.getInstance().getReference().child("Product Images");

        closeTextBtn=findViewById(R.id.close_settings_btn);
        saveBtn=findViewById(R.id.update_product_settings_btn);

        productImageView=findViewById(R.id.settings_productimage);
        profileChange=findViewById(R.id.Change_Product);
        key=getIntent().getStringExtra("key");
        pname=findViewById(R.id.setting_product_name);
        desciption=findViewById(R.id.setting_Description);
        category=findViewById(R.id.setting_Category);
        price=findViewById(R.id.setting_Price);

        userProductDisplay(productImageView,pname,desciption,price,key);

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
                if(checked.equals("clicked"))
                {
                    productInfosaved();
                }
                else
                {
                    updateOnlyProductInfo(key);
                }

            }
        });

        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checked="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(Admin_Change_Product_Detail_Activity.this);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();

            productImageView.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Admin_Change_Product_Detail_Activity.this,SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyProductInfo(String key)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Products");

        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("pname",pname.getText().toString());
        userMap.put("description",desciption.getText().toString());
        userMap.put("price",price.getText().toString());
        userMap.put("category",category.getText().toString());
        ref.child(key).updateChildren(userMap);


        startActivity(new Intent(Admin_Change_Product_Detail_Activity.this,Admin_Home_Activity.class));
        Toast.makeText(Admin_Change_Product_Detail_Activity.this, "Product Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void productInfosaved() {
    }

    private void userProductDisplay(final CircleImageView productImageView, final EditText pname, final EditText desciption, final EditText price, String key)
    {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(key);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String Pname=dataSnapshot.child("pname").getValue().toString();
                        String Price=dataSnapshot.child("price").getValue().toString();
                        String Des=dataSnapshot.child("description").getValue().toString();
                        String Cat=dataSnapshot.child("category").getValue().toString();

                        Picasso.get().load(image).into(productImageView);
                        pname.setText(Pname);
                        desciption.setText(Des);
                        price.setText(Price);
                        category.setText(Cat);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
