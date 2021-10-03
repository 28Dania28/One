package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

public class UniteAdd extends AppCompatActivity {
    private Button captureImage,captureVideo,gallary;
    private ImageView back,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_add);
        captureImage = findViewById(R.id.captureImage);
        captureVideo = findViewById(R.id.captureVideo);
        add = findViewById(R.id.add);
        back = findViewById(R.id.back);
        gallary = findViewById(R.id.gallary);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i4 = new Intent(getApplicationContext(),CreatingCategory.class);
                startActivity(i4);
            }
        });
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i1 = new Intent(getApplicationContext(),StoriesCaptureImage.class);
                //startActivity(i1);
            }
        });
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i2 = new Intent(getApplicationContext(),StoriesCaptureVideo.class);
                //startActivity(i2);
            }
        });
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(),UniteStoriesPickGallary.class);
                startActivity(i2);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
