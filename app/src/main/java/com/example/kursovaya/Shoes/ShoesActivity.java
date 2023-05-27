package com.example.kursovaya.Shoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kursovaya.CategoryShoes;
import com.example.kursovaya.Interface.ItemClickListner;
import com.example.kursovaya.LikeActivity;
import com.example.kursovaya.Model.Likes;
import com.example.kursovaya.Model.Shoe;
import com.example.kursovaya.R;
import com.example.kursovaya.ShoesDetailsActivity;
import com.example.kursovaya.ViewHolder.ShoesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ShoesActivity extends AppCompatActivity {

    private Map<String, Boolean> likeShoes = new HashMap<>();

    private String categoryName;
    private RecyclerView ShoesList;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference ShoesRef, LikeRef;
    private TextView CategoryShoesTxt;
    private ImageView BackBtn;
    private String CategoryShoesTxtSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);

        categoryName = getIntent().getStringExtra("category");

        init();

        checkCategoy();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(ShoesActivity.this, CategoryShoes.class);
                startActivity(backIntent);
            }
        });

        loadFavouritePlaces();


    }

    private void loadFavouritePlaces() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favouritesRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        Query favouritePlacesQuery = favouritesRef.orderByKey().startAt(userId+"_").endAt(userId+ "\uf8ff");

        favouritePlacesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot favouriteSnapshot : dataSnapshot.getChildren()) {
                    Likes like = favouriteSnapshot.getValue(Likes.class);
                    if (like != null) {
                        likeShoes.put(like.getShoesId(), true);
                    }
                }
                loadPlaces();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработать ошибку
            }
        });
    }

    private void checkCategoy() {
        if (categoryName.equals("Muj"))
        {
            CategoryShoesTxtSet = "Мужщинам";
            CategoryShoesTxt.setText(CategoryShoesTxtSet);
        }
        else if(categoryName.equals("Jen"))
        {
            CategoryShoesTxtSet = "Женщинам";
            CategoryShoesTxt.setText(CategoryShoesTxtSet);
        }
        else if(categoryName.equals("Deti"))
        {
            CategoryShoesTxtSet = "Детям";
            CategoryShoesTxt.setText(CategoryShoesTxtSet);
        }

    }



    protected void  loadPlaces()
    {
        super.onStart();

        FirebaseRecyclerOptions<Shoe> options = new FirebaseRecyclerOptions.Builder<Shoe>()
                .setQuery(ShoesRef, Shoe.class).build();

        FirebaseRecyclerAdapter<Shoe, ShoesViewHolder> adapter = new FirebaseRecyclerAdapter<Shoe, ShoesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShoesViewHolder holder, int position, @NonNull Shoe model) {
                holder.txtShoeName_List.setText(model.getName());
                holder.txtShoeSize_List.setText(model.getSize());
                Picasso.get().load(model.getImage()).into(holder.ShoesImage_List);
                Log.d("FirebaseData", "Name: " + model.getName() + ", Description: " + model.getDescription());

                if (likeShoes.containsKey(model.getShoesID())) {
                    boolean isLike = likeShoes.get(model.getShoesID());
                    model.setLike(isLike);
                    updateFavouriteIcon(holder.likeIcon, isLike);
                }

                holder.likeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isLike = model.isLike();
                        model.setLike(!isLike);
                        likeShoes.put(model.getShoesID(), !isLike);
                        updateFavouriteIcon(holder.likeIcon, !isLike);

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String shoesID = model.getShoesID();
                        String userId_shoesId = userId + "_" + shoesID;

                        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Like");

                        if (!isLike) {
                            // Если место не является избранным, добавляем его
                            Likes like = new Likes (userId, shoesID, categoryName);
                            likeRef.child(userId_shoesId).setValue(like);
                        } else {
                            // Если место является избранным, удаляем его
                            likeRef.child(userId_shoesId).removeValue();
                        }
                    }
                });

                holder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(ShoesActivity.this, ShoesDetailsActivity.class);
                        intent.putExtra("ShoesID", model.getShoesID());
                        intent.putExtra("category", categoryName);
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public ShoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoes_item_layout, parent, false);
                ShoesViewHolder holder = new ShoesViewHolder(view);
                return holder;
            }
        };
        ShoesList.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateFavouriteIcon(ImageView likeIcon, boolean isLike) {
        if (isLike) {
            likeIcon.setImageResource(R.drawable.like);
        } else {
            likeIcon.setImageResource(R.drawable.unlike);
        }
    }

    private void init()
    {
        ShoesRef = FirebaseDatabase.getInstance().getReference().child("Shoes").child(categoryName);

        CategoryShoesTxt = findViewById(R.id.category_name);
        BackBtn = findViewById(R.id.back_btn);
        ShoesList = findViewById(R.id.recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        ShoesList.setHasFixedSize(true);
        ShoesList.setLayoutManager(layoutManager);
    }

}