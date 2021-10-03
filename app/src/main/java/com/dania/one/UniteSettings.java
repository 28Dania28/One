package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class UniteSettings extends AppCompatActivity {

    private UniteSettingsFragment settingsFragment;
    private FrameLayout sf;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_settings);
        sf = findViewById(R.id.sf);
        back_btn = findViewById(R.id.back_btn);
        try {
            settingsFragment = new UniteSettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sf,settingsFragment)
                    .commit();
        }catch (Exception e){
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        back_btn.
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
    }
}
