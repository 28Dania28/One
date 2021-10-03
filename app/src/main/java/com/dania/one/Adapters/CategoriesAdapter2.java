package com.dania.one.Adapters;

import android.content.Context;
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
import com.dania.one.CategoriesListener;
import com.dania.one.Common;
import com.dania.one.Model.CategoriesModel;
import com.dania.one.Model.TagBuddyModel;
import com.dania.one.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter2 extends RecyclerView.Adapter<CategoriesAdapter2.ViewHolder> {

    private Context context;
    private ArrayList<CategoriesModel> categoriesModels;
    CategoriesListener categoriesListener;


    public CategoriesAdapter2(Context context, ArrayList<CategoriesModel> categoriesModels, CategoriesListener categoriesListener) {
        this.context = context;
        this.categoriesModels = categoriesModels;
        this.categoriesListener = categoriesListener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_adapter_view,parent,false);
        return new CategoriesAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CategoriesModel cm = categoriesModels.get(position);
        holder.BindCategoryModel(cm,position);

    }

    @Override
    public int getItemCount() {
        return categoriesModels.size();
    }

    public ArrayList<CategoriesModel> getAllModels(){
        ArrayList<CategoriesModel> models = new ArrayList<>();
        models.addAll(categoriesModels);
        return models;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView catIcon;
        public TextView catLabel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.catIcon);
            catLabel = itemView.findViewById(R.id.catLabel);

        }
        void BindCategoryModel(final CategoriesModel cm, final int position){
            Glide.with(context).load(cm.getUri()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(catIcon);
            catLabel.setText(cm.getName());
            if (cm.getSelected()){
                catLabel.setTextColor(context.getResources().getColor(R.color.txt3));
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_jump);
                catIcon.startAnimation(hyperspaceJumpAnimation);
            }else {
                catLabel.setTextColor(context.getResources().getColor(R.color.txt2));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.selected_category = position;
                    if (cm.getSelected()){
                        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_jump);
                        catIcon.startAnimation(hyperspaceJumpAnimation);
                    }else {
                        categoriesListener.OnSelect(cm.getName(),cm.getType());
                    }
                }
            });
        }

    }
}
