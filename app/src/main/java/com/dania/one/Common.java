package com.dania.one;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dania.one.DatabaseSqlite.DatabaseHelperChats;
import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.FriendModel;
import com.dania.one.Model.MyData;
import com.dania.one.Model.Story;
import com.dania.one.Model.StoryPacket;
import com.dania.one.Model.UserModel;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static final String token_tb1 = "Tokens";
    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static MyData myData;
    public static int radius = 25;
    public static String my_uid = null;
    public static String my_name = null;
    public static String my_dp = null;
    public static String my_first_name=null;
    public static String my_family_name=null;
    public static String my_email_id=null;
    public static Boolean dataIsReady=false;
    public static ArrayList<UserModel> FFuser_model = null;
    public static ArrayList<Story> myStoryModels = null;
    public static ArrayList<StoryPacket> storyPackets = null;
    public static DatabaseReference FFLiveLocationRef = null;
    public static GeoFire FFgeoFire = null;
    public static GeoQuery FFgeoQuery = null;
    public static ArrayList<Story> myLiveStoryModels = null;
    public static ArrayList<Story> userLiveStoryModels = null;
    public static ArrayList<Story> userStoryModels = null;
    public static String my_token;
    public static ArrayList<FriendModel> follow_requests = null;
    public static ArrayList<FriendModel> bonds = null;
    public static Uri capture_image_uri = null;
    public static Bitmap newBitmap;
    public static Uri gallary_video_uri = null;
    public static ArrayList<DateWiseStoriesModel> myDateWiseStoriesModels = null;
    public static ArrayList<DateWiseStoriesModel> userDateWiseStoriesModels = null;
    public static List<String> FFuser_uid = null;
    public static String ans = "No not recieved";
    public static int selected_category = 0;
    public static boolean day = true;
    public static DocumentSnapshot userLastStory=null;
    public static String userLastStoryDate = null;

   /* public static void getMyData() {
        if (Common.my_uid==null){
            Common.my_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(Common.my_uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Common.my_name = dataSnapshot.child("name").getValue().toString();
                Common.my_display_picture = dataSnapshot.child("display_picture").getValue().toString();
                Common.my_first_name = dataSnapshot.child("first_name").getValue().toString();
                Common.my_family_name = dataSnapshot.child("family_name").getValue().toString();
                Common.my_email_id = dataSnapshot.child("email_id").getValue().toString();
                Common.dataIsReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    */

    public static void updateToken(String userid) {
        try{
            if (Common.my_token.isEmpty()){
            }else{
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userid)
                        .child("basicInfo")
                        .child("token")
                        .setValue(Common.my_token);
            }
        }catch (Exception e){

        }
    }
    /*
    public static void updateToken(Context context, String s) {
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("token")
                .setValue(s);


    }

     */
}

//Notifications got shifted to Notification Handler