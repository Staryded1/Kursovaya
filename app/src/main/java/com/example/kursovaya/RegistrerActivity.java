package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button registrationButton;
    private EditText inputMail;
    private EditText inputLogin;
    private EditText inputPassword;
    private ProgressDialog loadingBar;

    private String USER_KEY = "User";

    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                } else {

                }
            }
        };

        init();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputMail.getText().toString())) {
                    Toast.makeText(RegistrerActivity.this, "Введите номер!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(inputLogin.getText().toString())) {
                    Toast.makeText(RegistrerActivity.this, "Введите логин!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(inputPassword.getText().toString())) {
                    Toast.makeText(RegistrerActivity.this, "Введите пароль!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Создание аккаунта");
                    loadingBar.setMessage("Пожалуйста, подождите...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    CreateAccount(inputMail.getText().toString(), inputPassword.getText().toString());
                }
            }
        });
    }

    private void CreateAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrerActivity.this, "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();

                    String userUid = mAuth.getCurrentUser().getUid();
                    String userName = inputLogin.getText().toString();
                    String userRole = "User";

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("username", userName);
                    userData.put("email", email);
                    userData.put("password", password);
                    userData.put("role", userRole);

                    mDataBase.child(userUid).setValue(userData);

                    Intent succesRegIntent = new Intent(RegistrerActivity.this, LoginActivity.class);
                    startActivity(succesRegIntent);
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(RegistrerActivity.this, "Ошибка...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        registrationButton = (Button) findViewById(R.id.register_btn);
        inputLogin = (EditText) findViewById(R.id.register_username_input);
        inputMail = (EditText) findViewById(R.id.register_mail_input);
        inputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
}
