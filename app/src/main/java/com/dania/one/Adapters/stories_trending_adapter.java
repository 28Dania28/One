package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.Story;
import com.dania.one.R;
import com.dania.one.StoriesFetchedGlobal;
import com.dania.one.ViewTrendingStories;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class stories_trending_adapter extends RecyclerView.Adapter <stories_trending_adapter.trendingStoriesViewHolder>{

    Context context;
    ArrayList<Story> storyModels;

    public stories_trending_adapter(Context context, ArrayList<Story> storyModels) {
        this.context = context;
        this.storyModels = storyModels;
    }

    @NonNull
    @Override
    public trendingStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.only_image_adapter,parent,false);
        return new stories_trending_adapter.trendingStoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull trendingStoriesViewHolder holder, final int position) {
        Story storyModel = StoriesFetchedGlobal.TrendingAllStories.get(position);
        if (storyModel.getType().equals("image")){
            Uri uri = Uri.parse(storyModel.getUri());
            holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(storyModel.getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewTrendingStories.class);
                i.putExtra("position",position);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return StoriesFetchedGlobal.TrendingAllStories.size();
    }

    public class trendingStoriesViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;

        public trendingStoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
        }
    }
}

