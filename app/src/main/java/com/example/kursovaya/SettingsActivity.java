package com.example.kursovaya;

import static com.example.kursovaya.Prevalent.Prevalent.currentOnlineUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kursovaya.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private ImageView BackBtn, SaveChangesBtn;
    private EditText InputEmail, InputLogin, InputPhone;
    private CircleImageView ProfileImage;
    private String checker = "";
    private StorageReference StorageProfilePictureRef;
    private StorageTask uploadTask;
    private String currentUserId;

    private Uri ImageUri;
    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        userInfoDisplay(ProfileImage, InputEmail, InputLogin, InputPhone);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(SettingsActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        SaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked"))
                {
                    userInfoAndImageSaved();
                }
                else
                {
                    userInfoSaved();
                }
            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                OpenGallery();

            }
        });
    }

    private void userInfoDisplay(final CircleImageView profileImage, final EditText inputEmail, final EditText inputLogin, final EditText inputPhone)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue() != null ? snapshot.child("image").getValue().toString() : "";
                        String name = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                        String phone = snapshot.child("phone").getValue() != null ? snapshot.child("phone").getValue().toString() : "";
                        String email = snapshot.child("email").getValue() != null ? snapshot.child("email").getValue().toString() : "";

                        Picasso.get().load(image).into(profileImage);
                        inputEmail.setText(email);
                        inputLogin.setText(name);
                        inputPhone.setText(phone);
                    }
                    if (snapshot.child("username").exists())
                    {
                        String name = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                        String phone = snapshot.child("phone").getValue() != null ? snapshot.child("phone").getValue().toString() : "";
                        String email = snapshot.child("email").getValue() != null ? snapshot.child("email").getValue().toString() : "";

                        inputEmail.setText(email);
                        inputLogin.setText(name);
                        inputPhone.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            ProfileImage.setImageURI(ImageUri);
        }
    }

    private void userInfoSaved()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");


        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username", InputLogin.getText().toString());
        userMap.put("email", InputEmail.getText().toString());
        userMap.put("phone", InputPhone.getText().toString());
        ref.child(currentUserId).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, Profile.class));
        Toast.makeText(this, "Данные успешно изменены", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoAndImageSaved()
    {
        if (TextUtils.isEmpty(InputEmail.getText().toString()))
        {
            Toast.makeText(this, "Введите вашу почту", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(InputLogin.getText().toString()))
        {
            Toast.makeText(this, "Введите ваше имя", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(InputPhone.getText().toString()))
        {
            Toast.makeText(this, "Введите ваш номер телефона", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Обновление...");
        progressDialog.setMessage("Пожалуйста, подождите");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (ImageUri != null)
        {
            final StorageReference fileRef = StorageProfilePictureRef.child(currentUserId + ".WebP");

            uploadTask = fileRef.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("username", InputLogin.getText().toString());
                        userMap.put("email", InputEmail.getText().toString());
                        userMap.put("phone", InputPhone.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(currentUserId).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, Profile.class));
                        Toast.makeText(SettingsActivity.this, "Данные успешно изменены", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Ошибка...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
    }

    private void init()
    {
        BackBtn = (ImageView) findViewById(R.id.close_btn);
        SaveChangesBtn = (ImageView) findViewById(R.id.savesettings_btn);

        InputEmail = (EditText) findViewById(R.id.settings_email);
        InputLogin = (EditText)  findViewById(R.id.settings_login_user);
        InputPhone = (EditText) findViewById(R.id.settings_user_phone);
        ProfileImage = (CircleImageView) findViewById(R.id.user_profile_image);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
    }
}
