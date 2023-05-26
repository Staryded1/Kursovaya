package com.example.kursovaya.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovaya.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    private String categoryName;
    private ImageView ShoesPhoto;
    private EditText ShoesName;
    private String Name;
    private EditText ShoesDescription;
    private String Description;
    private EditText ShoesSize;
    private String Size;
    private EditText ShoesNumber;
    private String Number;
    private EditText ShoesMoney;
    private String Money;
    private ImageView BackBtn;
    private Button CreateShoesBtn;
    private TextView TextCategory;
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private String ShoesRandomKey;
    private StorageReference ShoesImageRef;
    private String downloadImageUrl;
    private DatabaseReference ShoesRef;
    private ProgressDialog loadingBar;


    public AddActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        init();

        TextCategory.setText(getIntent().getExtras().getString("category"));

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(AddActivity.this, AddNewShoes.class);
                startActivity(backIntent);
            }
        });

        ShoesPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        CreateShoesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePlaceData();
            }
        });
    }

    private void ValidatePlaceData()
    {
        Description = ShoesDescription.getText().toString();
        Name = ShoesName.getText().toString();
        Number = ShoesNumber.getText().toString();
        Size = ShoesSize.getText().toString();
        Money = ShoesMoney.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(this, "Добавьте изображение", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name))
        {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Добавьте описание", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Size))
        {
            Toast.makeText(this, "Укажите размер", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Money))
        {
            Toast.makeText(this, "Укажите цену", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreShoesInformation();
        }
    }

    private void StoreShoesInformation()
    {

        loadingBar.setTitle("Создание нового места");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        long currentTimeMillis = System.currentTimeMillis();
        ShoesRandomKey = String.valueOf(currentTimeMillis);
        StorageReference filePath = ShoesImageRef.child(ImageUri.getLastPathSegment() + ShoesRandomKey + ".png");


        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddActivity.this, "Произошла ошибка: " + message , Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddActivity.this, "Изображение получено", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddActivity.this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
                            SavePlaceInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SavePlaceInfoToDatabase()
    {
        HashMap<String, Object> shoesData = new HashMap<>();

        shoesData.put("ShoesID", ShoesRandomKey);
        shoesData.put("Description", Description);
        shoesData.put("Image", downloadImageUrl);
        shoesData.put("Money", Money);
        shoesData.put("Number", Number);
        shoesData.put("Name", Name);
        shoesData.put("Size", Size);


        // Здесь мы сохраняем данные об обуви внутри категории.
        ShoesRef.child(categoryName).child(ShoesRandomKey).updateChildren(shoesData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();

                    Toast.makeText(AddActivity.this, "Ваша обувь добавлена в магазин!", Toast.LENGTH_SHORT).show();

                    Intent Intent = new Intent(AddActivity.this, AddNewShoes.class);
                    startActivity(Intent);
                }
                else
                {
                    loadingBar.dismiss();

                    String message = task.getException().toString();
                    Toast.makeText(AddActivity.this, "Произошла ошибка: " + message, Toast.LENGTH_SHORT).show();
                }
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

        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            ShoesPhoto.setImageURI(ImageUri);
        }
    }

    public void init()
    {
        categoryName = getIntent().getStringExtra("category");
        ShoesPhoto = findViewById(R.id.select_shoes_image);
        ShoesName = findViewById(R.id.shoes_name);
        ShoesDescription = findViewById(R.id.description_shoes);
        ShoesSize = findViewById(R.id.shoes_size);
        ShoesNumber = findViewById(R.id.number_shoes);
        ShoesMoney = findViewById(R.id.shoes_money);
        BackBtn = findViewById(R.id.back_btn);
        CreateShoesBtn = findViewById(R.id.btn_addNewProduct);
        ShoesImageRef = FirebaseStorage.getInstance().getReference().child("Shoes Images");
        TextCategory = (TextView) findViewById(R.id.category_name);
        ShoesRef = FirebaseDatabase.getInstance().getReference().child("Shoes");
        loadingBar = new ProgressDialog(this);

    }

}

