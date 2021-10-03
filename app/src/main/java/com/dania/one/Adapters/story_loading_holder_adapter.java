package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.R;

public class story_loading_holder_adapter extends RecyclerView.Adapter <story_loading_holder_adapter.StoriesViewHolder>{

    Context context;
    String holder_type;

    public story_loading_holder_adapter(Context context, String holder_type) {
        this.context = context;
        this.holder_type = holder_type;
    }

    @NonNull
    @Override
    public story_loading_holder_adapter.StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.story_loading_holder,parent,false);
        return new story_loading_holder_adapter.StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull story_loading_holder_adapter.StoriesViewHolder holder, int position) {
        holder.type.setText(holder_type);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder{
        TextView type;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
        }
    }
}

