package com.example.kursovaya.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kursovaya.MainActivity;
import com.example.kursovaya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminPanel extends AppCompatActivity {

    private Button gotoAddShoes_btn;
    private ImageView logoutBtn;
    private ImageView settingsBtn;
    private CircleImageView ProfileImage;
    private String currentUserId;
    private TextView HelloUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        init();

        gotoAddShoes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPlaceIntent = new Intent(AdminPanel.this, AddNewShoes.class);
                startActivity(addPlaceIntent);
            }
        });

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                HelloUser.setText("Администратор " + username);
                String imageUrl = snapshot.child("Image").getValue() != null ? snapshot.child("Image").getValue().toString() : "";
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

                Intent logoutIntent = new Intent(AdminPanel.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(AdminPanel.this, AdminSettings.class);
                startActivity(settingsIntent);
            }
        });
    }


    private void init()
    {
        gotoAddShoes_btn = (Button) findViewById(R.id.gotoadd_btn);
        logoutBtn = (ImageView) findViewById(R.id.logout_btn);
        settingsBtn = (ImageView) findViewById(R.id.settings_btn);
        ProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HelloUser = (TextView) findViewById(R.id.username);
    }
}