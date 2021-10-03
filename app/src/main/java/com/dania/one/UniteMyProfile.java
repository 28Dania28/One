package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class UniteMyProfile extends AppCompatActivity {

    private UniteMyProfileFragment profileFragment;
    private FrameLayout pf;
    private ImageView setting_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_my_profile);
        pf = findViewById(R.id.pf);
        setting_iv = findViewById(R.id.setting_iv);
        setting_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),UniteSettings.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
            }
        });
        try {
            profileFragment = new UniteMyProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pf,profileFragment)
                    .commit();
        }catch (Exception e){
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
