package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.CategoriesGlobal;
import com.dania.one.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder> {

    private Context context;

    public AllCategoriesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.aca_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.catLabel.setText(CategoriesGlobal.allCategories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return CategoriesGlobal.allCategories.size();
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
