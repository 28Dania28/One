package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.Model.CategoriesPublicModel;
import com.dania.one.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedAddCategoriesAdapter extends RecyclerView.Adapter<SelectedAddCategoriesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CategoriesPublicModel> categoriesPublicModels;

    public SelectedAddCategoriesAdapter(Context context, ArrayList<CategoriesPublicModel> categoriesPublicModels) {
        this.context = context;
        this.categoriesPublicModels = categoriesPublicModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.selected_add_category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesPublicModel cpm = categoriesPublicModels.get(position);
        holder.name.setText(cpm.getName());
    }

    @Override
    public int getItemCount() {
        return categoriesPublicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView catIcon ;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.catIcon);
            name = itemView.findViewById(R.id.name);
        }
    }
}
