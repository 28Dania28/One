package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.FriendModel;
import com.dania.one.R;
import com.dania.one.UniteChattingActivity;
import com.dania.one.UniteUserProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class search_chats_adapter extends RecyclerView.Adapter<search_chats_adapter.chatFriendViewHolder>{

    Context context;
    ArrayList<FriendModel> friendModels;

    public search_chats_adapter(Context context, ArrayList<FriendModel> friendModels) {
        this.context = context;
        this.friendModels = friendModels;
    }

    @NonNull
    @Override
    public chatFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.chat_user_item,parent,false);
        return new chatFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatFriendViewHolder holder, int position) {
        final FriendModel fm = friendModels.get(position);
        holder.name.setText(fm.getName());
        Glide.with(context).load(fm.getDisplayPicture()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_pic);
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

        public chatFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.name);
        }
    }
}
