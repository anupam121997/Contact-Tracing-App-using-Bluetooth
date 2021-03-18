package com.example.projectinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    EditText PhoneNo, UserName, Password;
    Button register;
    TextView RLoginButton;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        PhoneNo = findViewById(R.id.editText);
        UserName = findViewById(R.id.editTextTextPersonName2);
        Password = findViewById(R.id.editTextTextPersonName3);

        register = findViewById(R.id.button3);
        RLoginButton = findViewById(R.id.textView4);

        FAuth = FirebaseAuth.getInstance();
        if(FAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phNo = PhoneNo.getText().toString().trim();
                String UName = UserName.getText().toString().trim();
                String PWord = Password.getText().toString().trim();

                if(TextUtils.isEmpty(phNo)){
                    PhoneNo.setError("Phone no is empty");
                    return;
                }
                if(phNo.length()<10){
                    PhoneNo.setError("Invalid Mobile No");
                    return;
                }

                if(TextUtils.isEmpty(UName)){
                    UserName.setError("UserName no is empty");
                    return;
                }
                if(TextUtils.isEmpty(PWord)){
                    Password.setError("Password no is empty");
                    return;
                }

                //Register the user in the firebase
                FAuth.createUserWithEmailAndPassword(UName, PWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        }
                        else{
                            Toast.makeText(Registration.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        RLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });
    }
}