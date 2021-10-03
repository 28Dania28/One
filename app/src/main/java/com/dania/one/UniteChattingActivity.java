package com.dania.one;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Adapters.MsgAdapter;
import com.dania.one.DatabaseSqlite.DatabaseHelperChats;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FCMResponse;
import com.dania.one.Model.FCMSendData;
import com.dania.one.Model.MsgModel;
import com.dania.one.Model.MsgServerModel;
import com.dania.one.Remote.IFCMService;
import com.dania.one.Remote.RetrofitFCMClient;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UniteChattingActivity extends AppCompatActivity implements ChattingListener{

    private TextView name;
    private CircleImageView pp;
    private ImageView back, emoji_iv, send_iv;
    private String user_uid, user_name, user_dp, my_id, my_name, my_dp;
    private EmojiPopup emojiPopup;
    private EmojiEditText msg;
    private Cursor c;
    private String key_text;
    private String extra_text;
    private String type_text;
    private String time_text;
    private String msg_text;
    private String uri_text;
    private String last_date_text = "na";
    private String direction_text;
    private String date_text;
    private String timestamp_txt;
    private boolean first = true;
    private ViewGroup rootView;
    private FirebaseFirestore db, db2;
    private ListenerRegistration registration;
    private CollectionReference collectionReference;
    private RecyclerView rv;
    private IFCMService ifcmService;
    private DatabaseHelperChats mDatabaseHelperChats;
    private RelativeLayout reply_bar;
    private ImageView close;
    private TextView rname, rmsg;
    private MsgModel replyMsg;
    private boolean replying = false;
    private ImageView goDown;
    private MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unite_chatting);
        // getWindow().setSharedElementEnterTransition(enterTransition());
        //getWindow().setSharedElementReturnTransition(returnTransition());
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b!=null){
            user_uid = b.getString("user_uid");
            user_name = b.getString("user_name");
            user_dp = b.getString("user_dp");
        }
        ChatsFetchedGlobal.user_name = user_name;
        ChatsFetchedGlobal.user_uid = user_uid;
        ChatsFetchedGlobal.user_dp = user_dp;
        emoji_iv = findViewById(R.id.emoji_iv);
        rootView = findViewById(R.id.rootView);
        send_iv = findViewById(R.id.send_iv);
        name = findViewById(R.id.name);
        pp = findViewById(R.id.pp);
        msg = findViewById(R.id.msg);
        back = findViewById(R.id.back);
        reply_bar = findViewById(R.id.reply_bar);
        close = findViewById(R.id.close);
        rname = findViewById(R.id.rname);
        rmsg = findViewById(R.id.rmsg);
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();
        goDown = findViewById(R.id.goDown);
        rv = findViewById(R.id.rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);
        ChatsFetchedGlobal.allChats = new ArrayList<>();
        ChatsFetchedGlobal.allChatsKey = new ArrayList<>();
        ChatsFetchedGlobal.replySelected = -1;
        SwipeController.SwipeControllerActions lInterFace = new SwipeController.SwipeControllerActions() {
            @Override
            public void onSwipePerformed(int position)
            {
                MsgModel m = ChatsFetchedGlobal.allChats.get(position);
                replyMsg = m;
                if (m.getDirection().equals("right")){
                    replying = true;
                    reply_bar.setVisibility(View.VISIBLE);
                    msg.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(msg, InputMethodManager.SHOW_IMPLICIT);
                    rname.setText("You");
                    if (m.getType().equals("text")){
                        rmsg.setText(m.getMsg_text());
                    }else if (m.getType().equals("image_msg")){
                        if (m.getMsg_text().trim().equals("")){
                            rmsg.setText("Image");
                        }else {
                            rmsg.setText(m.getMsg_text());
                        }
                    }else if (m.getType().equals("video_msg")){
                        if (m.getMsg_text().trim().equals("")){
                            rmsg.setText("Video");
                        }else {
                            rmsg.setText(m.getMsg_text());
                        }
                    }else {
                        rmsg.setText(m.getMsg_text());
                    }

                }else if (m.getDirection().equals("left")){
                    replying = true;
                    reply_bar.setVisibility(View.VISIBLE);
                    msg.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(msg, InputMethodManager.SHOW_IMPLICIT);
                    rname.setText(user_name);
                    if (m.getType().equals("text")){
                        rmsg.setText(m.getMsg_text());
                    }else if (m.getType().equals("image_msg")){
                        if (m.getMsg_text().trim().equals("")){
                            rmsg.setText("Image");
                        }else {
                            rmsg.setText(m.getMsg_text());
                        }
                    }else if (m.getType().equals("video_msg")){
                        if (m.getMsg_text().trim().equals("")){
                            rmsg.setText("Video");
                        }else {
                            rmsg.setText(m.getMsg_text());
                        }
                    }else {
                        rmsg.setText(m.getMsg_text());
                    }
                }else {
                    reply_bar.setVisibility(View.GONE);
                    replying = false;
                }
//                rv.setPadding(0,60,0,btmm.getHeight());
            }
        };

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_bar.setVisibility(View.GONE);
                replying = false;
//                rv.setPadding(0,60,0,55);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeController(getApplicationContext(), lInterFace));
        itemTouchHelper.attachToRecyclerView(rv);
        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        mDatabaseHelperChats = new DatabaseHelperChats(this);
        setUpEmojiPopUp();
        ChatsFetchedGlobal.mediaChats = new ArrayList<>();
        ChatsFetchedGlobal.mediaUri = new ArrayList<>();
        NotificationHandler.status = true;
        if (user_name!=null){
            name.setText(user_name);
        }else {
            DatabaseReference name_ref = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("basicInfo").child("name");
            name_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String my_name = dataSnapshot.getValue().toString();
                    user_name = my_name;
                    name.setText(user_name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(UniteChattingActivity.this);
            Cursor cur = mydatadb.getData();
            if (cur.getCount() > 0) {
                while (cur.moveToNext()){
                    my_id = cur.getString(0);
                    my_name = cur.getString(1);
                    my_dp = cur.getString(2);
                }
            }
        }else {
            my_id = Common.my_uid;
            my_name = Common.my_name;
            my_dp = Common.my_dp;
        }

        Uri uri = Uri.parse(my_dp);
        pp.setImageURI(uri);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(),UniteUserProfileActivity.class);
                i1.putExtra("user_uid",user_uid);
                startActivity(i1);

            }
        });


        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(),UniteUserProfileActivity.class);
                i1.putExtra("user_uid",user_uid);
                startActivity(i1);

            }
        });

        msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (event.getRawX() >= (msg.getRight() - msg.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        Intent i1 = new Intent(getApplicationContext(),UniteSendMediaMessage.class);
                        i1.putExtra("user_uid",user_uid);
                        i1.putExtra("user_name",user_name);
                        i1.putExtra("user_dp",user_dp);
                        startActivity(i1);
                        return false;


                    }
                }
                return false;
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    goDown.setVisibility(View.GONE);
                }else {
                    goDown.setVisibility(View.VISIBLE);
                }
            }

        });

        goDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.smoothScrollToPosition(ChatsFetchedGlobal.allChats.size()-1);
            }
        });

        send_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_txt = msg.getText().toString().trim();
                if (replying){
                    reply_bar.setVisibility(View.GONE);
                    replying = false;
                    if (!msg_txt.equals("")){
                        //                    sendMsg(msg_txt);
                        sendReply(msg_txt,replyMsg);
                        msg.setText("");
                    }else {
                        Toast.makeText(UniteChattingActivity.this, "You can't send empty messages.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!msg_txt.equals("")){
                        sendMsg(msg_txt);
                        msg.setText("");
                    }else {
                        Toast.makeText(UniteChattingActivity.this, "You can't send empty messages.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        collectionReference = db.collection(my_id+user_uid+"Chats");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user_uid).child("basicInfo");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue().toString();
                String user_dp  = dataSnapshot.child("display_picture").getValue().toString();
                Glide.with(getApplicationContext()).load(user_dp).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
                name.setText(user_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        c = mDatabaseHelperChats.getData("Chats"+my_id+user_uid);
        if (c.getCount() > 0) {
            while (c.moveToNext()){
                key_text = c.getString(0);
                type_text = c.getString(6);
                time_text = c.getString(2);
                msg_text = c.getString(1);
                direction_text = c.getString(7);
                uri_text = c.getString(8);
                date_text = c.getString(3);
                timestamp_txt = c.getString(5);
                extra_text = c.getString(9);
                if (!date_text.equals(last_date_text) || first){
                    String relativeDate = getRelativeDate(date_text);
                    MsgModel m = new MsgModel("date","time","msg","center","uri",relativeDate,timestamp_txt,"key"," ");
                    ChatsFetchedGlobal.allChats.add(m);
                    ChatsFetchedGlobal.allChatsKey.add("key");
                    last_date_text = date_text;
                    first = false;
                }
                MsgModel m2 = new MsgModel(type_text,time_text,msg_text,direction_text,uri_text,date_text,timestamp_txt,key_text,extra_text);
                ChatsFetchedGlobal.allChats.add(m2);
                ChatsFetchedGlobal.allChatsKey.add(m2.getKey());
                if (type_text.equals("image_msg")||type_text.equals("video_msg")){
                    ChatsFetchedGlobal.mediaChats.add(m2);
                    ChatsFetchedGlobal.mediaUri.add(uri_text);
                }
                msgAdapter = new MsgAdapter(UniteChattingActivity.this,this);
                rv.setAdapter(msgAdapter);
                rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
            }
            first = true;
        }

        com.google.firebase.firestore.Query query = db.collection(my_id+user_uid+"Chats").whereEqualTo("sender",user_uid);
        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.size()>0){
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                            if (dc.getType()== DocumentChange.Type.ADDED){
                                MsgServerModel msgServerModel = dc.getDocument().toObject(MsgServerModel.class);
                                if (msgServerModel.getUri() == null){
                                    boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msgServerModel.getMsg(),msgServerModel.getTime(),msgServerModel.getDate(),user_uid,msgServerModel.getMsg_id(),msgServerModel.getTimestamp(),msgServerModel.getType(),"left", " ",msgServerModel.getExtra());
                                    if (insertData){
                                        addToArray();
                                        db.collection(my_id+user_uid+"Chats").document(msgServerModel.getMsg_id()).delete();
                                    }else {
                                        Toast.makeText(UniteChattingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msgServerModel.getMsg(),msgServerModel.getTime(),msgServerModel.getDate(),user_uid,msgServerModel.getMsg_id(),msgServerModel.getTimestamp(),msgServerModel.getType(),"left", msgServerModel.getUri(),msgServerModel.getExtra());
                                    if (insertData){
                                        addToArray();
                                        db.collection(my_id+user_uid+"Chats").document(msgServerModel.getMsg_id()).delete();
                                    }else {
                                        Toast.makeText(UniteChattingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }else {

                            }
                        }
                    }else {

                    }
                }
            }
        });

        readServerMsg();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    private void readServerMsg() {

    }

    private void sendMsg(String msg_txt) {
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("h:mm a");
        String time = currentTime.format(calForTime.getTime());
        String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
        String key = db.collection("Chats").document().getId();
        int timestamp = (int) new Timestamp(new Date()).getSeconds();
        Map<String, Object> map = new HashMap<>();
        map.put("index", FieldValue.serverTimestamp());
        map.put("timestamp", timestamp);
        map.put("time", time);
        map.put("date", date);
        map.put("msg", msg_txt);
        map.put("type", "text");
        map.put("sender", my_id);
        map.put("msg_id",key);
        map.put("extra"," ");
        //map.put("status",0);//extra
        boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msg_txt,time,date,my_id,key,timestamp,"text","right"," ","");
        if (insertData) {
            addToArray();
            db.collection(user_uid+my_id+"Chats").document(key).set(map);
            Map<String,String> notiData = new HashMap<>();
            notiData.put("title","One");
            notiData.put("intent","msg");
            notiData.put("name",my_name);
            notiData.put("i1","Chats"+my_id+user_uid);
            notiData.put("i2",msg_txt);
            notiData.put("i3",time);
            notiData.put("i4",date);
            notiData.put("i5",my_id);
            notiData.put("i6",key);
            notiData.put("i7",String.valueOf(timestamp));
            notiData.put("i8","text");
            notiData.put("i9","left");
            notiData.put("image",my_dp);
            FCMSendData sendData = new FCMSendData("/topics/"+user_uid,notiData);
            ifcmService.sendNotification(sendData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FCMResponse>() {
                        @Override
                        public void accept(FCMResponse fcmResponse) throws Exception {

                        }
                    });


        }else {
            Toast.makeText(this, "Failed to send the message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendReply(String msg_txt, MsgModel rmsg) {
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("h:mm a");
        String time = currentTime.format(calForTime.getTime());
        String date = new SimpleDateFormat("d MMM, yyyy").format(new Date());
        String key = db.collection("Chats").document().getId();
        String extra = "";
        if (rmsg.getDirection().equals("right")){
            extra = rmsg.getKey()+"DaniaOne28"+my_id+"DaniaOne28"+rmsg.getMsg_text()+"DaniaOne28"+rmsg.getType()+"DaniaOne28"+rmsg.getUri();
        }else {
            extra = rmsg.getKey()+"DaniaOne28"+user_uid+"DaniaOne28"+rmsg.getMsg_text()+"DaniaOne28"+rmsg.getType()+"DaniaOne28"+rmsg.getUri();
        }
        int timestamp = (int) new Timestamp(new Date()).getSeconds();
        Map<String, Object> map = new HashMap<>();
        map.put("index", FieldValue.serverTimestamp());
        map.put("timestamp", timestamp);
        map.put("time", time);
        map.put("date", date);
        map.put("msg", msg_txt);
        map.put("type", "reply_text");
        map.put("sender", my_id);
        map.put("msg_id",key);
        map.put("extra",extra);
        //map.put("status",0);//extra
        boolean insertData = mDatabaseHelperChats.addData("Chats"+my_id+user_uid,msg_txt,time,date,my_id,key,timestamp,"reply_text","right"," ",extra);
        if (insertData) {
            addToArray();
            db.collection(user_uid+my_id+"Chats").document(key).set(map);
            Map<String,String> notiData = new HashMap<>();
            notiData.put("title","One");
            notiData.put("intent","msg");
            notiData.put("name",my_name);
            notiData.put("i1","Chats"+my_id+user_uid);
            notiData.put("i2",msg_txt);
            notiData.put("i3",time);
            notiData.put("i4",date);
            notiData.put("i5",my_id);
            notiData.put("i6",key);
            notiData.put("i7",String.valueOf(timestamp));
            notiData.put("i8","text");
            notiData.put("i9","left");
            notiData.put("image",my_dp);
            FCMSendData sendData = new FCMSendData("/topics/"+user_uid,notiData);
            ifcmService.sendNotification(sendData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FCMResponse>() {
                        @Override
                        public void accept(FCMResponse fcmResponse) throws Exception {

                        }
                    });
        }else {
            Toast.makeText(this, "Failed to send the message.", Toast.LENGTH_SHORT).show();
        }
    }


    private void addToArray() {
        ChatsFetchedGlobal.allChats = new ArrayList<>();
        ChatsFetchedGlobal.mediaChats = new ArrayList<>();
        ChatsFetchedGlobal.mediaUri = new ArrayList<>();
        c = mDatabaseHelperChats.getData("Chats"+my_id+user_uid);
        if (c.getCount() > 0) {
            while (c.moveToNext()){
                key_text = c.getString(0);
                type_text = c.getString(6);
                time_text = c.getString(2);
                msg_text = c.getString(1);
                direction_text = c.getString(7);
                date_text = c.getString(3);
                timestamp_txt = c.getString(5);
                uri_text = c.getString(8);
                extra_text = c.getString(9);
                if (!date_text.equals(last_date_text) || first){
                    String relativeDate = getRelativeDate(date_text);
                    MsgModel m = new MsgModel("date","time","msg","center","uri",relativeDate,timestamp_txt,"key"," ");
                    ChatsFetchedGlobal.allChats.add(m);
                    ChatsFetchedGlobal.allChatsKey.add("key");
                    last_date_text = date_text;
                    first = false;
                }
                MsgModel m2 = new MsgModel(type_text,time_text,msg_text,direction_text,uri_text,date_text,timestamp_txt,key_text,extra_text);
                ChatsFetchedGlobal.allChats.add(m2);
                ChatsFetchedGlobal.allChatsKey.add(m2.getKey());
                if (type_text.equals("image_msg")||type_text.equals("video_msg")){
                    ChatsFetchedGlobal.mediaChats.add(m2);
                    ChatsFetchedGlobal.mediaUri.add(uri_text);
                }
                msgAdapter = new MsgAdapter(UniteChattingActivity.this,this);
                rv.setAdapter(msgAdapter);
                rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
            }
            first = true;
        }

    }

    private String getRelativeDate(String date_text) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR,-1);
        if (date_text.equals(new SimpleDateFormat("d MMM, yyyy").format(new Date()))){
            return "Today";
        }else if (date_text.equals(new SimpleDateFormat("d MMM, yyyy").format(c1.getTime()))){
            return "Yesterday";
        }else {
            return date_text;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setUpEmojiPopUp() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setIconColor(R.color.color1)
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        emoji_iv.setImageResource(R.drawable.keyboard_logo);
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        emoji_iv.setImageResource(R.drawable.emoji_ios_category_smileysandpeople);
                    }
                })
                .build(msg);
        emoji_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });
    }


    private android.transition.Transition enterTransition() {
        android.transition.ChangeBounds bounds = new android.transition.ChangeBounds();
        bounds.setDuration(100);

        return bounds;
    }

    private android.transition.Transition returnTransition() {
        android.transition.ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(100);

        return bounds;
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationHandler.status = true;
        addToArray();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        NotificationHandler.status = false;
        registration.remove();
        overridePendingTransition(R.anim.slide_in_left_fast, R.anim.slide_out_right_fast);
    }

    @Override
    public void OnReplyClick(int position) {
        if (position>=0){
            rv.smoothScrollToPosition(position);
            ChatsFetchedGlobal.replySelected = position;
            msgAdapter.notifyItemChanged(position);
        }

    }
}