package com.example.projectinterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class LogIn extends AppCompatActivity {

    EditText UserName, Password;
    Button bLogIn;
    FirebaseAuth FAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        UserName = findViewById(R.id.Uname);
        Password = findViewById(R.id.upassword);
        bLogIn = findViewById(R.id.mlogin);
        FAuth = FirebaseAuth.getInstance();

        bLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UName = UserName.getText().toString().trim();
                String PWord = Password.getText().toString().trim();

                if(TextUtils.isEmpty(UName)){
                    UserName.setError("UserName no is empty");
                    return;
                }
                if(TextUtils.isEmpty(PWord)){
                    Password.setError("Password no is empty");
                    return;
                }

                //Authenticate the user
                FAuth.signInWithEmailAndPassword(UName, PWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        }
                        else{
                            Toast.makeText(LogIn.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}