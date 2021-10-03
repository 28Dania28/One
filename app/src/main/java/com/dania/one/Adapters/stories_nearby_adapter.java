package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.Story;
import com.dania.one.Model.StoryPacket;
import com.dania.one.R;
import com.dania.one.StoriesFetchedGlobal;
import com.dania.one.ViewNearbyStories;
import com.facebook.drawee.view.SimpleDraweeView;

public class stories_nearby_adapter extends RecyclerView.Adapter <stories_nearby_adapter.storiesNearbyViewHolder>{

    Context context;

    public stories_nearby_adapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public stories_nearby_adapter.storiesNearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.only_image_adapter_nearby,parent,false);
        return new stories_nearby_adapter.storiesNearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final stories_nearby_adapter.storiesNearbyViewHolder holder, final int position) {
        StoryPacket storyPacket = StoriesFetchedGlobal.nearbyStoryPackets.get(position);
        Story sm = storyPacket.getStoryModels().get(0);
        String mUri = sm.getUri();
        if (sm.getType().equals("image")){
            Uri uri = Uri.parse(mUri);
            holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(mUri).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }
        int no_of_stories = storyPacket.getStoryModels().size();
        String no = Integer.toString(no_of_stories);
        holder.no_of_stories.setText(no);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewNearbyStories.class);
                intent.putExtra("position",position);
                context.startActivity(intent);


            }
        });

    }



    @Override
    public int getItemCount() {
        return StoriesFetchedGlobal.nearbyStoryPackets.size();
    }

    public class storiesNearbyViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;
        TextView no_of_stories;

        public storiesNearbyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            no_of_stories = itemView.findViewById(R.id.no_of_stories);
        }
    }
}

