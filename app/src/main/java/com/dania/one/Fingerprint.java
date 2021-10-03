package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fingerprint extends AppCompatActivity {

    private TextView txt1;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private ImageView img1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        txt1 = (TextView) findViewById(R.id.txt1);
        img1 = findViewById(R.id.img1);
        //Check1: Android version should be greater or equal to Marshmallow
        //Check2: Device has fingerprint scanner
        //Check3: Have permission to use fingerprint scanner in the app
        //Check4: Lock screen is secured with at least 1 type of lock
        //Check5: Atleast 1 fingerprint is registered

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (fingerprintManager.isHardwareDetected()
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT)== PackageManager.PERMISSION_GRANTED
                    &&keyguardManager.isKeyguardSecure()
                    &&fingerprintManager.hasEnrolledFingerprints()
                    ){
                txt1.setText("Place your Fingerprint to LOG IN");
                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager,null);
            } else {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }

       }else {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
       }
       img1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(Fingerprint.this, "Not here, On the Fingerprint Sensor.", Toast.LENGTH_SHORT).show();
           }
       });



    }



}
