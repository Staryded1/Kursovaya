package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Profile extends AppCompatActivity {

    private ImageView logoutBtn;
    private ImageView settingsBtn;
    private CircleImageView ProfileImage;
    private BottomNavigationView NavigationMenu;
    private String currentUserId;
    private TextView HelloUser;
    private Button toFavouriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                HelloUser.setText("Здравствуйте, " + username);
                String imageUrl = snapshot.child("image").getValue() != null ? snapshot.child("image").getValue().toString() : "";
                if (!imageUrl.equals("")) {
                    Picasso.get().load(imageUrl).into(ProfileImage);
                } else {
                    Picasso.get().load(R.drawable.userprofile).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                FirebaseAuth.getInstance().signOut();

                Intent logoutIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Profile.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        NavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if (ItemId == R.id.navigation_profile)
                {

                }
                else if (ItemId == R.id.navigation_korzina)
                {
                    startActivity(new Intent(Profile.this, Korzina.class));
                }
                else if (ItemId == R.id.navigation_category)
                {
                    startActivity(new Intent(Profile.this, CategoryShoes.class));
                }
                return true;
            }
        });

//        toFavouriteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toFavouriteIntent = new Intent(Profile.this, FavouriteActivity.class);
//                startActivity(toFavouriteIntent);
//            }
//        });


    }

    private void init()
    {
        logoutBtn = (ImageView) findViewById(R.id.logout_btn);
        settingsBtn = (ImageView) findViewById(R.id.settings_btn);
        ProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HelloUser = (TextView) findViewById(R.id.username);

        NavigationMenu = (BottomNavigationView) findViewById(R.id.navigation_panel);

//        toFavouriteBtn = findViewById(R.id.to_favourite_button);
    }
}