package com.example.projectinterface;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth FAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.holder1,new OnBoardingFragmentOne()).commit();

        FAuth = FirebaseAuth.getInstance();
        if(FAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }
        else {
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.holder1,new OnBoardingFragmentOne()).commit();
        }
    }
}