package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Common;
import com.dania.one.DatabaseSqlite.DatabaseHelperChats;
import com.dania.one.DatabaseSqlite.DatabaseMyData;
import com.dania.one.Model.FriendModel;
import com.dania.one.R;
import com.dania.one.UniteChattingActivity;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_friend_adapter extends RecyclerView.Adapter <chat_friend_adapter.chatFriendViewHolder>{

    Context context;
    ArrayList<FriendModel> friendModels;
    private String my_id;
    DatabaseHelperChats mDatabaseHelperChats;


    public chat_friend_adapter(Context context, ArrayList<FriendModel> friendModels) {
        this.context = context;
        this.friendModels = friendModels;
        if (Common.my_uid.equals(null)){
            DatabaseMyData mydatadb = new DatabaseMyData(context);
            Cursor cur = mydatadb.getData();
            if (cur.getCount() > 0) {
                while (cur.moveToNext()){
                    my_id = cur.getString(0);
                }
            }
        }else {
            my_id = Common.my_uid;
        }

    }


    @NonNull
    @Override
    public chatFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_user_item,parent,false);
        return new chatFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final chatFriendViewHolder holder, int position) {
        mDatabaseHelperChats = new DatabaseHelperChats(context);
        final FriendModel fm = friendModels.get(position);
        holder.name.setText(fm.getName());
        Glide.with(context).load(fm.getDisplayPicture()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_pic);
        Cursor c = mDatabaseHelperChats.getData("Chats"+ my_id +fm.getId());
        if (c.getCount()>0){
            c.moveToLast();
            String last_msg_text = c.getString(1);
            String time_txt = c.getString(2);
            if (last_msg_text.isEmpty()){
                String type_text = c.getString(6);
                if (type_text.equals("image_msg")){
                    holder.last_msg.setVisibility(View.VISIBLE);
                    holder.last_msg.setText("image message");
                    holder.time.setText(time_txt);
                }else {
                    holder.last_msg.setVisibility(View.VISIBLE);
                    holder.last_msg.setText("video message");
                    holder.time.setText(time_txt);
                }
            }else {
                holder.last_msg.setVisibility(View.VISIBLE);
                holder.last_msg.setText(last_msg_text);
                holder.time.setText(time_txt);
            }
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, UniteChattingActivity.class);
                i1.putExtra("user_uid",fm.getId());
                i1.putExtra("user_name",fm.getName());
                i1.putExtra("user_dp",fm.getDisplayPicture());
                context.startActivity(i1);
                /*Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(holder.profile_pic, "user_pp");
                pairs[1] = new Pair<View, String>(holder.name,"user_name");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context , pairs);
                context.startActivity(i1, options.toBundle());

                 */

            }
        });
    }

    @Override
    public int getItemCount() {
        return friendModels.size();
    }

    public class chatFriendViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_pic;
        TextView name;
        EmojiTextView last_msg;
        TextView time;

        public chatFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.name);
            last_msg = itemView.findViewById(R.id.last_msg);
            time = itemView.findViewById(R.id.time);
        }
    }

}
