package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.kursovaya.Shoes.ShoesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.integrity.v;

public class CategoryShoes extends AppCompatActivity {

    private ImageView MujCategory;
    private ImageView JenCategory;
    private ImageView DetiCategory;
    private  ImageView Like;
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

        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFavouriteIntent = new Intent(CategoryShoes.this, LikeActivity.class);
                startActivity(toFavouriteIntent);
            }
        });

        NavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navigation_category)
                {

                }
                else if (ItemId == R.id.navigation_korzina)
                {
                    startActivity(new Intent(CategoryShoes.this, Korzina.class));
                }
                else if (ItemId == R.id.navigation_profile)
                {
                    startActivity(new Intent(CategoryShoes.this, Profile.class));
                }
                return true;
            }
        });

    }

    private void init()
    {
        MujCategory = (ImageView) findViewById(R.id.tapokmen);
        JenCategory = (ImageView) findViewById(R.id.tapokjen);
        DetiCategory = (ImageView) findViewById(R.id.tapokdeti);
        Like = (ImageView)findViewById(R.id.like_btn) ;
        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);

    }





}

