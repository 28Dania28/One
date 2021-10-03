package com.dania.one.Adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.Story;
import com.dania.one.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class unite_my_stories_datewise_adapter extends RecyclerView.Adapter<unite_my_stories_datewise_adapter.i_myStoriesViewHolder> {

    Context context;
    ArrayList<DateWiseStoriesModel> dateWiseStoriesModels;

    public unite_my_stories_datewise_adapter(Context context, ArrayList<DateWiseStoriesModel> dateWiseStoriesModels) {
        this.context = context;
        this.dateWiseStoriesModels = dateWiseStoriesModels;
    }

    @NonNull
    @Override
    public i_myStoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.all_my_story_item,parent,false);
        return new unite_my_stories_datewise_adapter.i_myStoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final i_myStoriesViewHolder holder, int position) {
        DateWiseStoriesModel dwsm = dateWiseStoriesModels.get(position);
        holder.date.setText(dwsm.getDate()+"    "+dwsm.getStoryModels().size());
        ArrayList<Story> storyModels = dwsm.getStoryModels();
        if (storyModels.get(0).getType().equals("image")){
            Uri uri = Uri.parse(storyModels.get(0).getUri());
            holder.iv.setImageURI(uri);
        }else {
           Glide.with(context).load(storyModels.get(0).getUri()).thumbnail(0.2f).into(holder.iv);
        }
        int no_of_views = 0;
        for (int i=0;i<storyModels.size();i++){
            no_of_views = no_of_views+(int)storyModels.get(i).getViews();
        }
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) no_of_views);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                holder.views.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(context,iShowAllMyStories.class);
                intent.putExtra("position",position);
                context.startActivity(intent);

               */
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateWiseStoriesModels.size();
    }

    public class i_myStoriesViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;
        CardView cv;
        TextView date, views;

        public i_myStoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            cv = itemView.findViewById(R.id.cv);
            date = itemView.findViewById(R.id.date);
            views = itemView.findViewById(R.id.views);
        }
    }


}
