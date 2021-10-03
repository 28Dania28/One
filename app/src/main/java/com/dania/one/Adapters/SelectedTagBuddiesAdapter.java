package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.TagBuddyModel;
import com.dania.one.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedTagBuddiesAdapter extends RecyclerView.Adapter<SelectedTagBuddiesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TagBuddyModel> tagBuddyModels;

    public SelectedTagBuddiesAdapter(Context context, ArrayList<TagBuddyModel> tagBuddyModels) {
        this.context = context;
        this.tagBuddyModels = tagBuddyModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.selected_tag_buddies_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagBuddyModel tbm = tagBuddyModels.get(position);
        Glide.with(context).load(tbm.getDisplayPicture()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return tagBuddyModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView dp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.dp);
        }
    }

}
