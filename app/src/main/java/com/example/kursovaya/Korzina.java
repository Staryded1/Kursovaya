package com.example.kursovaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.example.kursovaya.Adapter.LikeShoesAdapter;
import com.example.kursovaya.Model.Likes;
import com.example.kursovaya.Model.Shoe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Korzina extends AppCompatActivity {

    private RecyclerView likeShoes;
    private AppCompatButton BackBtnFav;
    private List<Shoe> likeShoesList = new ArrayList<>();
    private LikeShoesAdapter likeShoesAdapter;

    private String currentUserID, shoesId, category;
    private FirebaseAuth mAuth;

    private BottomNavigationView NavigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzina);

        init();



        BackBtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoProf = new Intent(Korzina.this, CategoryShoes.class);
                startActivity(backtoProf);
            }
        });



        NavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navigation_korzina)
                {

                }
                else if (ItemId == R.id.navigation_category)
                {
                    startActivity(new Intent(Korzina.this, CategoryShoes.class));
                }
                else if (ItemId == R.id.navigation_profile)
                {
                    startActivity(new Intent(Korzina.this, Profile.class));
                }
                return true;
            }
        });

    }




    private void init() {
        likeShoes = findViewById(R.id.recycler_like);
        BackBtnFav = findViewById(R.id.back_btn_like);




        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);
    }
}

