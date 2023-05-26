package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogetPassword extends AppCompatActivity {

    private ImageView BackBtn;
    private EditText UserPhone;
    private Button ResetPassBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);

        init();


        ResetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                if (!UserPhone.getText().toString().isEmpty()) {

                    auth.sendPasswordResetEmail(UserPhone.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FogetPassword.this, "Письмо с инструкциями отправлено вам на почту.", Toast.LENGTH_SHORT).show();
                                Intent succesIntent = new Intent(FogetPassword.this, LoginActivity.class);
                                startActivity(succesIntent);
                            } else {
                                Toast.makeText(FogetPassword.this, "Что-то пошло не так... Проверьте адрес электронной почты!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(FogetPassword.this, "Где почта?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FogetPassword.this, LoginActivity.class);
                startActivity(backIntent);
            }
        });

    }

    private void init()
    {
        BackBtn = findViewById(R.id.back_btn);
        UserPhone = findViewById(R.id.input_email_reset);
        ResetPassBtn = findViewById(R.id.reset_btn);

    }
}