package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.Story;
import com.dania.one.R;
import com.dania.one.ViewUserProfileStory;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class unite_user_stories_datewise_adapter extends RecyclerView.Adapter<unite_user_stories_datewise_adapter.i_userStoriesViewHolder> {


    Context context;
    ArrayList<DateWiseStoriesModel> dateWiseStoriesModels;

    public unite_user_stories_datewise_adapter(Context context, ArrayList<DateWiseStoriesModel> dateWiseStoriesModels) {
        this.context = context;
        this.dateWiseStoriesModels = dateWiseStoriesModels;
    }

    @NonNull
    @Override
    public i_userStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ams_item2,parent,false);
        return new unite_user_stories_datewise_adapter.i_userStoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final i_userStoriesViewHolder holder, final int position) {
        DateWiseStoriesModel dwsm = dateWiseStoriesModels.get(position);
        holder.date.setText(dwsm.getDate());
        holder.stories_no.setText(String.valueOf(dwsm.getStoryModels().size()));
        ArrayList<Story> storyModels = dwsm.getStoryModels();
        if (storyModels.get(0).getType().equals("image")){
            Uri uri = Uri.parse(storyModels.get(0).getUri());
            holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(storyModels.get(0).getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }
        int no_of_views = 0;
        for (int i=0;i<storyModels.size();i++){
            no_of_views = no_of_views+(int)storyModels.get(i).getViews();
        }

        holder.views.setText(String.valueOf(no_of_views));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewUserProfileStory.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateWiseStoriesModels.size();
    }

    public class i_userStoriesViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;
        CardView cv;
        TextView date, views, stories_no;

        public i_userStoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            cv = itemView.findViewById(R.id.cv);
            date = itemView.findViewById(R.id.date);
            views = itemView.findViewById(R.id.views);
            stories_no = itemView.findViewById(R.id.stories_no);
        }
    }

}
