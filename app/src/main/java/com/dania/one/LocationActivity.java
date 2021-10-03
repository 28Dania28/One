package com.dania.one;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;

import java.util.Date;


public class LocationActivity extends AppCompatActivity {

    private TextView lon, lat, timestamp_tv;
    FusedLocationProviderClient fusedLocationClient;
    private Button getTimeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        lon = findViewById(R.id.lon);
        lat = findViewById(R.id.lat);
        timestamp_tv = findViewById(R.id.timestamp_tv);
        getTimeBtn = findViewById(R.id.getTimeBtn);
        getLocation();
        getTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp tm = new Timestamp(new Date());
                String a = tm.toString();
                String b = String.valueOf(new Date().getTime());
                int timestamp = Math.round(new Date().getTime()/1000);
                lon.setText("a : "+a);
                lat.setText("b : "+b);
                timestamp_tv.setText("Timestamp : "+timestamp);
            }
        });
    }

    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 28);
            }
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LocationGlobal.location = location;
                            LocationGlobal.longitude = location.getLongitude();
                            LocationGlobal.latitude = location.getLatitude();
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            lon.setText("Longitude : " + LocationGlobal.longitude);
                            lat.setText("Latitude  : " + LocationGlobal.latitude);
                            // Logic to handle location object
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==28 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permisiion Granted", Toast.LENGTH_SHORT).show();
            getLocation();
        }else {
            finish();
        }
    }
}