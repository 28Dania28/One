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
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.dania.one.DatabaseSqlite.DatabaseHelperChats;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationHandler {


    public static boolean status = false;

    public static void showNotification(Context context, int id, String title, String content, Bitmap bitmap, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "DANIA";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Unite");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.one_logo)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(bitmap);
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }


    public static void showMsgNotification(Context context, int id, String title, String content, Bitmap bitmap, Intent intent) {
        if (!status){
            PendingIntent pendingIntent = null;
            if (intent != null)
                pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(bitmap);
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);
        }

    }

    public static void showNotificationWithoutPic(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "DANIA";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Unite");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.one_logo)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.earth));
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }


    public static void showMsgNotificationWithoutPic(Context context, int id, String title, String content, Intent intent) {
        if (!status){
            PendingIntent pendingIntent = null;
            if (intent != null)
                pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.earth));
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);
        }

    }

    public static void showMsgNotification2(Context context, int id, String title, String content, Bitmap bitmap, String i1, String i2, String i3, String i4, String i5, String i6, String i7, String i8, String i9, PendingIntent pendingIntent) {
        if(!status){
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(bitmap);
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);
            DatabaseHelperChats mDatabaseHelperChats;
            mDatabaseHelperChats = new DatabaseHelperChats(context);
//            boolean insertData = mDatabaseHelperChats.addData(i1,i2,i3,i4,i5,i6,Integer.parseInt(i7),i8,i9," ",i10);
//            if (insertData){
//                FirebaseFirestore db;
//                db = FirebaseFirestore.getInstance();
//                db.collection(i1).document(i6).delete();
//            }
        }

    }

    public static void showMsgNotificationWithoutPic2(Context context, int id, String title, String content, String i1, String i2, String i3, String i4, String i5, String i6, String i7, String i8, String i9, PendingIntent pendingIntent) {
        if (!status){
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.buddies_center_heart_high));
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);

            DatabaseHelperChats mDatabaseHelperChats;
            mDatabaseHelperChats = new DatabaseHelperChats(context);
//            boolean insertData = mDatabaseHelperChats.addData(i1,i2,i3,i4,i5,i6,Integer.parseInt(i7),i8,i9," ");
//            if (insertData){
//                FirebaseFirestore db;
//                db = FirebaseFirestore.getInstance();
//                db.collection(i1).document(i6).delete();
//            }
        }

    }


    public static void showUriMsgNotification(Context context, int id, String title, String content, Bitmap bitmap, String i1, String i2, String i3, String i4, String i5, String i6, String i7, String i8, String i9, String i10, PendingIntent pendingIntent) {
        if(!status){
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(bitmap);
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);
            DatabaseHelperChats mDatabaseHelperChats;
            mDatabaseHelperChats = new DatabaseHelperChats(context);
//            boolean insertData = mDatabaseHelperChats.addData(i1,i2,i3,i4,i5,i6,Integer.parseInt(i7),i8,i9," ");
//            if (insertData){
//                FirebaseFirestore db;
//                db = FirebaseFirestore.getInstance();
//                db.collection(i1).document(i6).delete();
//            }
        }

    }

    public static void showUriMsgNotificationWithoutPic(Context context, int id, String title, String content, String i1, String i2, String i3, String i4, String i5, String i6, String i7, String i8, String i9, String i10, PendingIntent pendingIntent) {
        if (!status){
            String NOTIFICATION_CHANNEL_ID = "DANIA";
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Unite");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.one_logo)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.buddies_center_heart_high));
            if (pendingIntent != null)
                builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notificationManager.notify(id,notification);

            DatabaseHelperChats mDatabaseHelperChats;
            mDatabaseHelperChats = new DatabaseHelperChats(context);
//            boolean insertData = mDatabaseHelperChats.addData(i1,i2,i3,i4,i5,i6,Integer.parseInt(i7),i8,i9," ");
//            if (insertData){
//                FirebaseFirestore db;
//                db = FirebaseFirestore.getInstance();
//                db.collection(i1).document(i6).delete();
//            }
        }

    }


    public static void showReactionNotification(Context context, int id, String title, String content, Bitmap bitmap, Bitmap bitmap2, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "DANIA";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Unite");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.one_logo)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(bitmap);
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }

    public static void showReactionNotificationWithoutPic(Context context, int id, String title, String content,Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "DANIA";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "DANIA_NAME",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Unite");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.one_logo)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.earth));
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id,notification);

    }





}
