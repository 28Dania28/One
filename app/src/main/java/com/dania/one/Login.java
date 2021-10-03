package com.dania.one;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    private ImageView google_btn;
    private GoogleSignInClient mGoogleSignInClients;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private int RC_GOOGLE_SIGN_IN = 1;
    private Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        google_btn = findViewById(R.id.google_btn);
        mAuth = FirebaseAuth.getInstance();
        loading = new Dialog(Login.this);
        loading.setCanceledOnTouchOutside(false);
        loading.setContentView(R.layout.dialog_loading);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInToGoogle();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent intent = new Intent(getApplicationContext(),Unite2.class);
            startActivity(intent);
            finish();
        }else {
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN){
            loading.show();
            Task<GoogleSignInAccount> gTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acc = gTask.getResult();
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            final GoogleSignInAccount last_account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            assert firebaseUser != null;
                            final String userid = firebaseUser.getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        //for once

                                        //for once
                                        DatabaseMyData mydatadb = new DatabaseMyData(getApplicationContext());
                                        boolean result = mydatadb.addData(userid,last_account.getDisplayName(),last_account.getPhotoUrl().toString().replace("s96-c", "s900-c"),last_account.getGivenName(),last_account.getFamilyName(),last_account.getEmail());
                                        if (result){
                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            loading.dismiss();
                                            FirebaseMessaging.getInstance().subscribeToTopic(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                            FirebaseMessaging.getInstance().subscribeToTopic("ProjectOne").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                        }else {
                                            loading.dismiss();
                                            Toast.makeText(Login.this, "Something went wrong, Please check your internet connection.", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        String email = last_account.getEmail();
                                        String displayName = last_account.getDisplayName();
                                        String givenName = last_account.getGivenName();
                                        String familyName = last_account.getFamilyName();
                                        String createdDate = new SimpleDateFormat("d MMM, yyyy").format(new Date());
                                        Uri photo_uri = last_account.getPhotoUrl();
                                        String dp_str = photo_uri.toString().replace("s96-c", "s900-c");
                                        DatabaseMyData mydatadb = new DatabaseMyData(getApplicationContext());
                                        boolean result = mydatadb.addData(userid,displayName,photo_uri.toString(),givenName,familyName,email);
                                        if (result){
                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                            HashMap<String, Object> hashMapName = new HashMap<>();
                                            hashMapName.put("name",displayName);

                                            HashMap<String, Object> basicInfo = new HashMap<>();
                                            basicInfo.put("name",displayName);
                                            basicInfo.put("display_picture",dp_str);

                                            HashMap<String, Object> extraInfo = new HashMap<>();
                                            extraInfo.put("user_id", userid);
                                            extraInfo.put("email_id",email);
                                            extraInfo.put("first_name",givenName);
                                            extraInfo.put("family_name",familyName);
                                            extraInfo.put("account_created",createdDate);

                                            HashMap<String, Object> countInfo = new HashMap<>();
                                            countInfo.put("stories_no",0);
                                            countInfo.put("views", 0);
                                            countInfo.put("buddies_modify",0);

                                            reference.setValue(hashMapName);
                                            reference.child("extraInfo").setValue(extraInfo);
                                            reference.child("countInfo").setValue(countInfo);
                                            reference.child("basicInfo").setValue(basicInfo);
                                            FirebaseMessaging.getInstance().getToken()
                                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<String> task) {
                                                            if (!task.isSuccessful()) {
                                                                return;
                                                            }

                                                            // Get new FCM registration token
                                                            String token = task.getResult();
                                                            Common.my_token = token;
                                                            Common.updateToken(userid);
                                                            // Log and toast
                                                        }
                                                    });
                                            FirebaseMessaging.getInstance().subscribeToTopic(userid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                            FirebaseMessaging.getInstance().subscribeToTopic("ProjectOne").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            loading.dismiss();
                                        }else {
                                            loading.dismiss();
                                            Toast.makeText(Login.this, "Something went wrong, Please check your internet connection.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }else {

                        }

                    }
                });

            }catch (Exception e){
                loading.dismiss();
            }
        }else {
            loading.dismiss();
        }
    }

    //Google Sign In
    private void signInToGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClients = GoogleSignIn.getClient(this, gso);
        Intent googleSignInIntent = mGoogleSignInClients.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);

    }


}