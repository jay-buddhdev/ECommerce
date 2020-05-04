package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_Password_Activity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private Button resetpassword;
    FirebaseAuth firebaseAut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password_);

        back=findViewById(R.id.back_arrow_fpassword);
        email=findViewById(R.id.fpassword_email);
        resetpassword=findViewById(R.id.fpassword_btn);
        firebaseAut=FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString()))
                {
                    Toast.makeText(Forget_Password_Activity.this, "Please Enter Email id", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAut.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(Forget_Password_Activity.this, "Password Reset Link Sent to Email", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Forget_Password_Activity.this,IntroActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(Forget_Password_Activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
                }
            }
        });
    }
}
