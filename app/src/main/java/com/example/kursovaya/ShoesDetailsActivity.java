package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovaya.Shoes.ShoesActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

public class ShoesDetailsActivity extends AppCompatActivity {

    TextView shoesName, shoesDescription, shoesSize, shoesMoney;
    ImageView shoesImage, backBtn;
    Button shoesUrl, shoesKorzina;
    String categoryName, shoesID;
    Uri shoesURLTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_details);

        init();

        shoesID = getIntent().getStringExtra("ShoesID");

        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference().child("Shoes").child(categoryName).child(shoesID);

        shoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String money = snapshot.child("Money").getValue(String.class);
                    String image = snapshot.child("Image").getValue(String.class);
                    String description = snapshot.child("Description").getValue(String.class);
                    String name = snapshot.child("Name").getValue(String.class);
                    String size = snapshot.child("Size").getValue(String.class);

                    Picasso.get().load(image).into(shoesImage);
                    shoesName.setText(name);
                    shoesDescription.setText(description);
                    shoesMoney.setText(money);
                    shoesSize.setText(size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибки получения данных из базы данных
            }
        });

        shoesUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoesURLTxt != null && !shoesURLTxt.toString().isEmpty()) {
                    String bankUrl = "https://web7-new.online.sberbank.ru/main";
                    shoesURLTxt = Uri.parse(bankUrl);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, shoesURLTxt);
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(ShoesDetailsActivity.this, "URL отсутствует или недействителен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(ShoesDetailsActivity.this, ShoesActivity.class);
                backIntent.putExtra("category", categoryName);
                startActivity(backIntent);
            }
        });

        shoesKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent korzinaIntent = new Intent(ShoesDetailsActivity.this, Korzina.class);
                korzinaIntent.putExtra("category", categoryName);
                korzinaIntent.putExtra("ShoesID", shoesID);
                startActivity(korzinaIntent);
            }
        });
    }

    private void init() {
        shoesName = findViewById(R.id.shoes_name);
        shoesDescription = findViewById(R.id.shoes_description);
        shoesSize = findViewById(R.id.shoes_size);
        shoesMoney = findViewById(R.id.shoes_money);
        shoesImage = findViewById(R.id.shoes_image);
        shoesUrl = findViewById(R.id.shoes_url);
        shoesKorzina = findViewById(R.id.shoes_korzina);
        backBtn = findViewById(R.id.back_btn);
        categoryName = getIntent().getStringExtra("category");
    }
}