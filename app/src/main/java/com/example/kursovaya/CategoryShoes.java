package com.example.kursovaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.Shoes.ShoesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.integrity.v;

public class CategoryShoes extends AppCompatActivity {

    private ImageView MujCategory;
    private ImageView JenCategory;
    private ImageView DetiCategory;
    private BottomNavigationView NavigationMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_shoes);

        init();

        MujCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mujIntent = new Intent(CategoryShoes.this, ShoesActivity.class);
                mujIntent.putExtra("category", "Muj");
                startActivity(mujIntent);
            }
        });

        JenCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cinemaIntent = new Intent(CategoryShoes.this, ShoesActivity.class);
                cinemaIntent.putExtra("category", "Jen");
                startActivity(cinemaIntent);
            }
        });

        DetiCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detiIntent = new Intent(CategoryShoes.this, ShoesActivity.class);
                detiIntent.putExtra("category", "Deti");
                startActivity(detiIntent);
            }
        });

    }

    private void init()
    {
        MujCategory = (ImageView) findViewById(R.id.tapokmen);
        JenCategory = (ImageView) findViewById(R.id.tapokjen);
        DetiCategory = (ImageView) findViewById(R.id.tapokdeti);
        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);

    }





}

