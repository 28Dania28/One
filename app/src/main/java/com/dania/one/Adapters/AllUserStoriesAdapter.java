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
import com.dania.one.R;
import com.dania.one.StoriesFetchedGlobal;
import com.dania.one.ViewUserProfileStory;
import com.facebook.drawee.view.SimpleDraweeView;

public class AllUserStoriesAdapter extends RecyclerView.Adapter<AllUserStoriesAdapter.AUSViewHolder>{

    private Context context;

    public AllUserStoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AllUserStoriesAdapter.AUSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ams_item,parent,false);
        return new AUSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserStoriesAdapter.AUSViewHolder holder, final int position) {
        if (StoriesFetchedGlobal.allUserStoryModels.get(position).getType().equals("image")){
            Uri uri = Uri.parse(StoriesFetchedGlobal.allUserStoryModels.get(position).getUri());
            holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(StoriesFetchedGlobal.allUserStoryModels.get(position).getUri()).thumbnail(0.2f).into(holder.iv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewUserProfileStory.class);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return StoriesFetchedGlobal.allUserStoryModels.size();
    }

    public class AUSViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;
        TextView date;
        public AUSViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            date = itemView.findViewById(R.id.date);
        }
    }

}

