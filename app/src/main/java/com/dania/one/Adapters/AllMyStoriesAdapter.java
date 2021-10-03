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
import com.dania.one.ViewAllMyStories;
import com.facebook.drawee.view.SimpleDraweeView;

public class AllMyStoriesAdapter extends RecyclerView.Adapter<AllMyStoriesAdapter.AMSViewHolder>{

    private Context context;

    public AllMyStoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ams_item,parent,false);
        return new AMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AMSViewHolder holder, final int position) {
        if (StoriesFetchedGlobal.allMyStoryModels.get(position).getType().equals("image")){
            Uri uri = Uri.parse(StoriesFetchedGlobal.allMyStoryModels.get(position).getUri());
            holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(StoriesFetchedGlobal.allMyStoryModels.get(position).getUri()).thumbnail(0.2f).into(holder.iv);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewAllMyStories.class);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return StoriesFetchedGlobal.allMyStoryModels.size();
    }

    public class AMSViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv;
        TextView date;
        public AMSViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            date = itemView.findViewById(R.id.date);
        }
    }

}
