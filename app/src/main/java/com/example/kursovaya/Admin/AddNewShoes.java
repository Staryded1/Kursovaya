package com.example.kursovaya.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.R;

public class AddNewShoes extends AppCompatActivity {

    private ImageView mujCategory;
    private ImageView jenCategory;
    private ImageView detiCategory;
    private ImageView BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shoes);

        init();


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackIntent = new Intent(AddNewShoes.this, AdminPanel.class);
                startActivity(BackIntent);
            }
        });

        mujCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mujIntent = new Intent(AddNewShoes.this, AddActivity.class);
                mujIntent.putExtra("category", "Muj");
                startActivity(mujIntent);
            }
        });

        jenCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jenIntent = new Intent(AddNewShoes.this, AddActivity.class);
                jenIntent.putExtra("category", "Jen");
                startActivity(jenIntent);
            }
        });

        detiCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detiIntent = new Intent(AddNewShoes.this, AddActivity.class);
                detiIntent.putExtra("category", "Deti");
                startActivity(detiIntent);
            }
        });

    }

    private void init() {
        mujCategory = findViewById(R.id.tapokmen);
        jenCategory = findViewById(R.id.tapokjen);
        detiCategory = findViewById(R.id.tapokdeti);

        BackBtn = findViewById(R.id.back_btn);
    }
}