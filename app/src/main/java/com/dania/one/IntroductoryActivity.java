package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroductoryActivity extends AppCompatActivity {

    private ImageView bg, logo;
    private TextView appName;
    private LottieAnimationView lottie;
    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        bg = findViewById(R.id.bg);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.appName);
        lottie = findViewById(R.id.lottie);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            loggedIn = true;
        }else {
            loggedIn = false;
        }
        bg.animate().translationY(-1600).setDuration(500).setStartDelay(2000);
        logo.animate().translationY(1400).setDuration(500).setStartDelay(2000);
        appName.animate().translationY(1400).setDuration(500).setStartDelay(2000);
        lottie.animate().translationY(1400).setDuration(500).setStartDelay(2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loggedIn){
                    Intent intent = new Intent(getApplicationContext(),Unite2.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2700);
    }
}