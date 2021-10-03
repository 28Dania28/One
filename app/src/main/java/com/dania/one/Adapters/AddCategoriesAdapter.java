package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.AddCategoriesListener;
import com.dania.one.Model.CategoriesPublicModel;
import com.dania.one.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCategoriesAdapter extends RecyclerView.Adapter<AddCategoriesAdapter.ACViewHolder> {

    private Context context;
    public ArrayList<CategoriesPublicModel> categoriesPublicModels;
    private AddCategoriesListener addCategoriesListener;

    public AddCategoriesAdapter(Context context, ArrayList<CategoriesPublicModel> categoriesPublicModels, AddCategoriesListener addCategoriesListener) {
        this.context = context;
        this.categoriesPublicModels = categoriesPublicModels;
        this.addCategoriesListener = addCategoriesListener;
    }

    @NonNull
    @Override
    public ACViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.add_categories_item,parent,false);
        return new ACViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ACViewHolder holder, int position) {
        CategoriesPublicModel cpm = categoriesPublicModels.get(position);
        holder.BindCategoryModel(cpm);
    }

    @Override
    public int getItemCount() {
        return categoriesPublicModels.size();
    }

    public ArrayList<CategoriesPublicModel> getSelectedCategories(){
        ArrayList<CategoriesPublicModel> catPublicModels = new ArrayList<>();
        for (CategoriesPublicModel cpm : categoriesPublicModels){
            if (cpm.getSelected()){
                catPublicModels.add(cpm);
            }
        }
        return catPublicModels;
    }

    public class ACViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView catIcon;
        public TextView name;
        public RadioButton checkbox;

        public ACViewHolder(@NonNull View itemView) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.catIcon);
            name = itemView.findViewById(R.id.name);
            checkbox = itemView.findViewById(R.id.checkbox);
        }

        public void BindCategoryModel(final CategoriesPublicModel cpm) {
            name.setText(cpm.getName());
            if (cpm.getSelected()){
                checkbox.setChecked(true);
            }else {
                checkbox.setChecked(false);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cpm.getSelected()){
                        checkbox.setChecked(false);
                        cpm.setSelected(false);
                    }else {
                        checkbox.setChecked(true);
                        cpm.setSelected(true);
                    }
                    if (getSelectedCategories().size()==0){
                        addCategoriesListener.OnAction(false);
                    }else {
                        addCategoriesListener.OnAction(true);
                    }
                }
            });
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cpm.getSelected()){
                        checkbox.setChecked(false);
                        cpm.setSelected(false);
                    }else {
                        checkbox.setChecked(true);
                        cpm.setSelected(true);
                    }
                    if (getSelectedCategories().size()==0){
                        addCategoriesListener.OnAction(false);
                    }else {
                        addCategoriesListener.OnAction(true);
                    }
                }
            });
        }
    }
}
