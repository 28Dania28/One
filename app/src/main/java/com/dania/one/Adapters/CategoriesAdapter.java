package com.dania.one.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.CategoriesModel;
import com.dania.one.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    EventListener listener;
    Context context;
    ArrayList<CategoriesModel> categoriesModels;
    private int selectedItem;
    private int previousItem;

    public CategoriesAdapter(Context context, ArrayList<CategoriesModel> categoriesModels, EventListener listener) {
        this.context = context;
        this.categoriesModels = categoriesModels;
        this.listener = listener;
        selectedItem = 0;
    }

    public interface EventListener {
        void onEvent(int data);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_adapter_view,parent,false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CategoriesModel categoriesModel = categoriesModels.get(position);
        holder.catLabel.setText(categoriesModel.getName());
        if (selectedItem == position) {
            holder.catLabel.setTextColor(context.getResources().getColor(R.color.txt3));
        }else {
            holder.catLabel.setTextColor(context.getResources().getColor(R.color.txt2));
        }
        if (categoriesModel.getType()==0){
            Glide.with(context).load(categoriesModel.getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.catIcon);
        }else if (categoriesModel.getType()==1){
            holder.catIcon.setImageResource(R.drawable.trending_logo);
        }else if (categoriesModel.getType()==2){
            holder.catIcon.setImageResource(R.drawable.nearby_logo);
        }else if (categoriesModel.getType()==3){
            holder.catIcon.setImageResource(R.drawable.buddies_logo);
        }else {
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_jump);
                holder.catIcon.startAnimation(hyperspaceJumpAnimation);
                previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(position);
                notifyItemChanged(previousItem);
                if (categoriesModel.getType()==1){
                    listener.onEvent(1);
                }else if (categoriesModel.getType()==2){
                    listener.onEvent(2);
                }else if (categoriesModel.getType()==3){
                    listener.onEvent(3);
                }else {
                    listener.onEvent(0);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView catIcon;
        public TextView catLabel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.catIcon);
            catLabel = itemView.findViewById(R.id.catLabel);

        }

    }
}
