package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.ChatsFetchedGlobal;
import com.dania.one.ChattingListener;
import com.dania.one.Model.MsgModel;
import com.dania.one.R;
import com.dania.one.ViewChatMedia;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vanniktech.emoji.EmojiTextView;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    Context context;
    ChattingListener chattingListener;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT_REACTED = 2;
    public static final int MSG_TYPE_RIGHT_REACTED = 3;
    public static final int MSG_IMAGE_TYPE_LEFT = 4;
    public static final int MSG_IMAGE_TYPE_RIGHT = 5;
    public static final int MSG_VIDEO_TYPE_LEFT = 6;
    public static final int MSG_VIDEO_TYPE_RIGHT = 7;
    public static final int MSG_DATE_CHANGE = 8;
    public static final int MSG_TYPE_RIGHT_REPLY_TEXT = 9;
    public static final int MSG_TYPE_LEFT_REPLY_TEXT = 10;

    public MsgAdapter(Context context, ChattingListener chattingListener) {
        this.context = context;
        this.chattingListener = chattingListener;
    }

    @NonNull
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_RIGHT_REPLY_TEXT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right_reply_text, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_LEFT_REPLY_TEXT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left_reply_text, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_RIGHT_REACTED){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right_reacted, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_TYPE_LEFT_REACTED){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left_reacted, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_IMAGE_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_image_right, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_IMAGE_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_image_left, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_VIDEO_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_video_right, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_VIDEO_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_video_left, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else if (viewType == MSG_DATE_CHANGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_date_change, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MsgAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MsgModel msgModel = ChatsFetchedGlobal.allChats.get(position);
        if (ChatsFetchedGlobal.replySelected==position){
            holder.itemView.setBackgroundResource(R.drawable.c_r_sahasraha_transparent_15);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.itemView.setBackgroundResource(R.color.transparent);
                }
            }, 1500);
            ChatsFetchedGlobal.replySelected = -1;
        }
        if (msgModel.getType().equals("text")){
            holder.msg.setText(msgModel.getMsg_text());
            holder.time.setText(msgModel.getTime());
            if (position==ChatsFetchedGlobal.allChats.size()-1&&msgModel.getDirection().equals("right")){
                holder.status.setVisibility(View.VISIBLE);
            }
        }else if (msgModel.getType().equals("reply_text")){
            holder.msg.setText(msgModel.getMsg_text());
            holder.time.setText(msgModel.getTime());
            if (position==ChatsFetchedGlobal.allChats.size()-1&&msgModel.getDirection().equals("right")){
                holder.status.setVisibility(View.VISIBLE);
            }
            final String[] extra = msgModel.getExtra().split("DaniaOne28");
            if (extra[1].equals(ChatsFetchedGlobal.user_uid)){
                holder.rname.setText(ChatsFetchedGlobal.user_name);
            }else {
                holder.rname.setText("You");
            }
            if (extra[3].equals("text")||extra[3].equals("reply_text")){
                holder.rmsg.setText(extra[2]);
            }else if (extra[3].equals("image_msg")){
                if (extra[2].trim().equals("")){
                    holder.rmsg.setText("Image");
                }else {
                    holder.rmsg.setText(extra[2]);
                }
            }else if (extra[3].equals("video_msg")){
                if (extra[2].trim().equals("")){
                    holder.rmsg.setText("Video");
                }else {
                    holder.rmsg.setText(extra[2]);
                }
            }else {

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chattingListener.OnReplyClick(ChatsFetchedGlobal.allChatsKey.indexOf(extra[0]));
                }
            });
        }else if (msgModel.getType().equals("reaction")){
            holder.msg.setText(msgModel.getMsg_text());
            holder.time.setText(msgModel.getTime());
            Glide.with(context).load(msgModel.getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.story_iv);
        }else if (msgModel.getType().equals("image_msg")){
            if (msgModel.getMsg_text().isEmpty()){
                holder.msg.setVisibility(View.GONE);
            }else {
                holder.msg.setText(msgModel.getMsg_text());
            }
            holder.time.setText(msgModel.getTime());
            Uri uri = Uri.parse(msgModel.getUri());
            holder.image_msg.setImageURI(uri);
            holder.image_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = ChatsFetchedGlobal.mediaUri.indexOf(msgModel.getUri());
                    Intent i = new Intent(context, ViewChatMedia.class);
                    i.putExtra("position",p);
                    i.putExtra("uri",msgModel.getUri());
                    context.startActivity(i);
                }
            });
        }else if (msgModel.getType().equals("video_msg")){
            holder.msg.setText(msgModel.getMsg_text());
            holder.time.setText(msgModel.getTime());
            Glide.with(context).load(msgModel.getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.story_iv);
        }else if (msgModel.getType().equals("date")){
            holder.date.setText(msgModel.getDate());
        }else {

        }
    }

    @Override
    public int getItemCount() {
        return ChatsFetchedGlobal.allChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView time;
        public TextView rname, rmsg;
        public EmojiTextView msg;
        public ImageView story_iv;
        public SimpleDraweeView image_msg;
        private RelativeLayout status;
        public TextView date;


        public ViewHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
            story_iv = itemView.findViewById(R.id.story_iv);
            image_msg = (SimpleDraweeView) itemView.findViewById(R.id.image_msg);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            rname = itemView.findViewById(R.id.rname);
            rmsg = itemView.findViewById(R.id.rmsg);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (ChatsFetchedGlobal.allChats.get(position).getDirection().equals("right")){
            if (ChatsFetchedGlobal.allChats.get(position).getType().equals("text")){
                return MSG_TYPE_RIGHT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("reaction")){
                return MSG_TYPE_RIGHT_REACTED;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("reply_text")) {
                return MSG_TYPE_RIGHT_REPLY_TEXT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("image_msg")){
                return MSG_IMAGE_TYPE_RIGHT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("video_msg")){
                return MSG_VIDEO_TYPE_RIGHT;
            }else {
                return MSG_TYPE_RIGHT;
            }
        }else if (ChatsFetchedGlobal.allChats.get(position).getDirection().equals("center")) {
            return MSG_DATE_CHANGE;
        }else {
            if (ChatsFetchedGlobal.allChats.get(position).getType().equals("text")){
                return MSG_TYPE_LEFT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("reaction")){
                return MSG_TYPE_LEFT_REACTED;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("reply_text")) {
                return MSG_TYPE_LEFT_REPLY_TEXT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("image_msg")){
                return MSG_IMAGE_TYPE_LEFT;
            }else if (ChatsFetchedGlobal.allChats.get(position).getType().equals("video_msg")){
                return MSG_VIDEO_TYPE_LEFT;
            }else {
                return MSG_TYPE_LEFT;
            }
        }
    }


}