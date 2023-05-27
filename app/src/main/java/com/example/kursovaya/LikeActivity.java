package com.example.kursovaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.kursovaya.Adapter.LikeShoesAdapter;
import com.example.kursovaya.Model.Likes;
import com.example.kursovaya.Model.Shoe;
import com.example.kursovaya.Shoes.ShoesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity {

    private RecyclerView likeShoes;
    private AppCompatButton BackBtnFav;
    private List<Shoe> likeShoesList = new ArrayList<>();
    private LikeShoesAdapter likeShoesAdapter;

    private String currentUserID, shoesId, category;
    private FirebaseAuth mAuth;
    DatabaseReference LikeRef, ShoesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        init();

        likeShoes.setLayoutManager(new LinearLayoutManager(this));
        likeShoesAdapter = new LikeShoesAdapter(likeShoesList, shoe -> {
            LikeRef.child(currentUserID + "_" + shoesId).removeValue();
            likeShoesList.remove(shoe);
            likeShoesAdapter.notifyDataSetChanged();
        }, currentUserID);
        likeShoes.setAdapter(likeShoesAdapter);

        BackBtnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoProf = new Intent(LikeActivity.this, CategoryShoes.class);
                startActivity(backtoProf);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Like");
        Query likeShoesQuery = likeRef.orderByKey().startAt(userId+"_").endAt(userId+ "\uf8ff");

        likeShoesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likeShoesList.clear();
                for (DataSnapshot likeSnapshot : dataSnapshot.getChildren()) {
                    Likes like = likeSnapshot.getValue(Likes.class);
                    if (like != null) {
                        String shoesId = like.getShoesId();
                        String category = like.getCategory();
                        getShoesDetails(shoesId, category);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки
            }
        });

    }

    private void getShoesDetails(String shoesId, String category) {
        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference().child("Shoes").child(category);

        shoesRef.child(shoesId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shoe shoe = dataSnapshot.getValue(Shoe.class);
                if (shoe != null) {
                    likeShoesList.add(shoe);
                    likeShoesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        likeShoes = findViewById(R.id.recycler_like);
        BackBtnFav = findViewById(R.id.back_btn_like);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Like");

        ShoesRef = FirebaseDatabase.getInstance().getReference().child("Shoes");
    }
}


