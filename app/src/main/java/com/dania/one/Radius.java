package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Radius extends AppCompatActivity {

    private EditText et;
    private ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);
        et = findViewById(R.id.et);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et.getText().length()>0){
                    int radius = Integer.parseInt(String.valueOf(et.getText()));
                    Common.radius = radius;
                    Toast.makeText(Radius.this, "Radius set to "+radius, Toast.LENGTH_SHORT).show();
                    et.setText("");
                    finish();
                }
            }
        });
    }
}