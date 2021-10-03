package com.dania.one.Service;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dania.one.Common;
import com.dania.one.NotificationHandler;
import com.dania.one.UniteChattingActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    Bitmap bitmap, bitmap2;
    public MyFirebaseInstanceService() {
    }
   /* @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> dataRecv = remoteMessage.getData();
        if (dataRecv!=null){
            Common.showNotification(this,new Random().nextInt(),
                    dataRecv.get("title"),
                    dataRecv.get("content"),
                    null);
        }
    }
    */

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Common.ans = "Yes it is recieved";
        Map<String,String> dataRecv = remoteMessage.getData();
        if (dataRecv!=null){
            if (dataRecv.get("intent").equals("bond")){
                bitmap = getBitmapFromUrl(dataRecv.get("image"));
                if (bitmap!=null){
                    NotificationHandler.showNotification(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            "You got a new bond with "+dataRecv.get("name"),
                            bitmap,
                            null);
                }else {
                    NotificationHandler.showNotificationWithoutPic(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            "You got a new bond with "+dataRecv.get("name"),
                            null);
                }
            }else if (dataRecv.get("intent").equals("msg")){
                Intent notiIntent = new Intent(this, UniteChattingActivity.class);
                notiIntent.putExtra("user_uid",dataRecv.get("i5"));
                notiIntent.putExtra("user_name",dataRecv.get("name"));
                notiIntent.putExtra("user_dp",dataRecv.get("image"));
                notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(this,0,notiIntent,0);
                bitmap = getBitmapFromUrl(dataRecv.get("image"));
                if (bitmap!=null){
                    NotificationHandler.showMsgNotification2(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            bitmap,dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),
                            intent);
                }else {
                    NotificationHandler.showMsgNotificationWithoutPic2(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),
                            intent);
                }

            }else if (dataRecv.get("intent").equals("reaction")){
                bitmap = getBitmapFromUrl(dataRecv.get("image"));
                bitmap2 = getBitmapFromUrl(dataRecv.get("story_uri"));
                if (bitmap!=null){
                    NotificationHandler.showReactionNotification(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" reacted "+dataRecv.get("reaction")+" to your story.",
                            bitmap,bitmap2,
                            null);
                }else {
                    NotificationHandler.showReactionNotificationWithoutPic(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" reacted "+dataRecv.get("reaction")+" to your story.",
                            null);               }
            }else if (dataRecv.get("intent").equals("image_msg")){
                Intent notiIntent = new Intent(this, UniteChattingActivity.class);
                notiIntent.putExtra("user_uid",dataRecv.get("i5"));
                notiIntent.putExtra("user_name",dataRecv.get("name"));
                notiIntent.putExtra("user_dp",dataRecv.get("image"));
                notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(this,0,notiIntent,0);
                bitmap = getBitmapFromUrl(dataRecv.get("image"));
                if (bitmap!=null){
                    NotificationHandler.showUriMsgNotification(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            bitmap,dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),dataRecv.get("i10"),
                            intent);
                }else {
                    NotificationHandler.showUriMsgNotificationWithoutPic(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),dataRecv.get("i10"),
                            intent);
                }

            }else if (dataRecv.get("intent").equals("video_msg")){
                Intent notiIntent = new Intent(this, UniteChattingActivity.class);
                notiIntent.putExtra("user_uid",dataRecv.get("i5"));
                notiIntent.putExtra("user_name",dataRecv.get("name"));
                notiIntent.putExtra("user_dp",dataRecv.get("image"));
                notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(this,0,notiIntent,0);
                bitmap = getBitmapFromUrl(dataRecv.get("image"));
                if (bitmap!=null){
                    NotificationHandler.showUriMsgNotification(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            bitmap,dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),dataRecv.get("i10"),
                            intent);
                }else {
                    NotificationHandler.showUriMsgNotificationWithoutPic(this,new Random().nextInt(),
                            dataRecv.get("title"),
                            dataRecv.get("name")+" : "+dataRecv.get("i2"),
                            dataRecv.get("i1"),dataRecv.get("i2"),dataRecv.get("i3"),dataRecv.get("i4"),dataRecv.get("i5"),dataRecv.get("i6"),dataRecv.get("i7"),dataRecv.get("i8"),dataRecv.get("i9"),dataRecv.get("i10"),
                            intent);
                }

            }
        }
    }



    private Bitmap getBitmapFromUrl(String image) {
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Common.my_token = s;
        //Common.updateToken(this,s);
    }
}
