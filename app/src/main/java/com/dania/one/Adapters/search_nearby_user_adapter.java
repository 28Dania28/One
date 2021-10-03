package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Common;
import com.dania.one.DatabaseSqlite.DatabaseHelperChatRanker;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FCMResponse;
import com.dania.one.Model.FCMSendData;
import com.dania.one.Model.UserModel;
import com.dania.one.R;
import com.dania.one.Remote.IFCMService;
import com.dania.one.Remote.RetrofitFCMClient;
import com.dania.one.UniteUserProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class search_nearby_user_adapter extends RecyclerView.Adapter <search_nearby_user_adapter.findFriendViewHolder>{

    Context context;
    ArrayList<UserModel> user_model;
    IFCMService ifcmService;
    String my_id, my_name, my_dp;

    public search_nearby_user_adapter(Context context, ArrayList<UserModel> user_model) {
        this.context = context;
        this.user_model = user_model;
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(context);
            Cursor c = mydatadb.getData();
            if (c.getCount() > 0) {
                while (c.moveToNext()){
                    my_id = c.getString(0);
                    my_name = c.getString(1);
                    my_dp = c.getString(2);
                }
            }
        }else {
            my_id = Common.my_uid;
            my_name = Common.my_name;
            my_dp = Common.my_dp;
        }

    }

    @NonNull
    @Override
    public findFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.find_nearby_friend_cv,parent,false);
        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        //mService = FCMClient.getInstance().create(IFCMService.class);
        return new search_nearby_user_adapter.findFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final findFriendViewHolder holder, int position) {
        final UserModel um = user_model.get(position);
        holder.name.setText(um.getName());
        Glide.with(context).load(um.getDisplayPicture()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pp);

        if (um.getStatus().equals("bonded")){
            holder.bond.setVisibility(View.GONE);
            holder.bonded.setVisibility(View.VISIBLE);
        }else if (um.getStatus().equals("not_bonded")){
            holder.bond.setVisibility(View.VISIBLE);
            holder.bonded.setVisibility(View.GONE);
        }

        holder.pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, UniteUserProfileActivity.class);
                i1.putExtra("user_uid",um.getId());
                context.startActivity(i1);

            }
        });

        holder.bond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference sendRequest = FirebaseDatabase.getInstance().getReference(um.getId()+"Bonds").child(my_id);
                sendRequest.setValue(my_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("Users").child(um.getId()).child("countInfo").child("buddies_modify");
                user_ref.setValue("1");
                updateSqlDatabase();
                DatabaseReference requested = FirebaseDatabase.getInstance().getReference(my_id+"Bonds").child(um.getId());
                requested.setValue(um.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.bond.setVisibility(View.GONE);
                        holder.bonded.setVisibility(View.VISIBLE);
                        Map<String,String> notiData = new HashMap<>();
                        notiData.put("title","DnS");
                        notiData.put("uid",my_id);
                        notiData.put("name",my_name);
                        notiData.put("intent","bond");
                        notiData.put("image",my_dp);
                        FCMSendData sendData = new FCMSendData("/topics/"+um.getId(),notiData);
                        ifcmService.sendNotification(sendData)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<FCMResponse>() {
                                    @Override
                                    public void accept(FCMResponse fcmResponse) throws Exception {

                                    }
                                });
                    }
                });
                FirebaseMessaging.getInstance().subscribeToTopic("Stories"+um.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

            }
        });

    }

    private void updateSqlDatabase() {
        final DatabaseHelperChatRanker mDatabaseHelperRank = new DatabaseHelperChatRanker(context);
        mDatabaseHelperRank.deleteAllFriends("Buddies"+my_id);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(my_id+"Bonds");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final String uid = snapshot.getKey();
                        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("basicInfo");
                        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                String dp_str = dataSnapshot.child("display_picture").getValue().toString();
                                if (dataSnapshot.hasChild("token")){
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    boolean insertData = mDatabaseHelperRank.addFriendsData("Buddies"+my_id,uid,name,dp_str,token);
                                }else {
                                    boolean insertData = mDatabaseHelperRank.addFriendsData("Buddies"+my_id,uid,name,dp_str,"nothing");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return user_model.size();
    }

    public class findFriendViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView pp, bonded;
        TextView name;
        Button bond;

        public findFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            pp = itemView.findViewById(R.id.pp);
            name = itemView.findViewById(R.id.name);
            bond = itemView.findViewById(R.id.bond);
            bonded = itemView.findViewById(R.id.bonded);
        }
    }
}



