package com.dania.one.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.TagBuddyModel;
import com.dania.one.R;
import com.dania.one.SearchTagBuddiesListener;
import com.dania.one.TagBuddiesListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchTagBuddiesAdapter extends RecyclerView.Adapter<SearchTagBuddiesAdapter.STBViewHolder>{

    private Context context;
    private ArrayList<TagBuddyModel> tagBuddyModels;
    private SearchTagBuddiesListener searchTagBuddiesListener;

    public SearchTagBuddiesAdapter(Context context, ArrayList<TagBuddyModel> tagBuddyModels, SearchTagBuddiesListener searchTagBuddiesListener) {
        this.context = context;
        this.tagBuddyModels = tagBuddyModels;
        this.searchTagBuddiesListener = searchTagBuddiesListener;
    }

    @NonNull
    @Override
    public STBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tag_buddies_user_item,parent,false);
        return new STBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull STBViewHolder holder, int position) {
        TagBuddyModel tbm = tagBuddyModels.get(position);
        holder.BindFriendModel(tbm);
    }

    @Override
    public int getItemCount() {
        return tagBuddyModels.size();
    }

    public ArrayList<TagBuddyModel> getSelectedIds(){
        ArrayList<TagBuddyModel> ids = new ArrayList<>();
        for (TagBuddyModel tbm : tagBuddyModels){
            if (tbm.getSelected()){
                ids.add(tbm);
            }
        }
        return ids;
    }

    public class STBViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView pp;
        public TextView name;
        public RadioButton checkbox;

        public STBViewHolder(@NonNull View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp);
            name = itemView.findViewById(R.id.name);
            checkbox = itemView.findViewById(R.id.checkbox);
        }

        void BindFriendModel(final TagBuddyModel tbm){
            Glide.with(context).load(tbm.getDisplayPicture()).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(pp);
            name.setText(tbm.getName());
            if (tbm.getSelected()){
                checkbox.setChecked(true);
            }else {
                checkbox.setChecked(false);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tbm.getSelected()){
                        checkbox.setChecked(false);
                        tbm.setSelected(false);
                        searchTagBuddiesListener.OnDeSelect(tbm);
                    }else {
                        checkbox.setChecked(true);
                        tbm.setSelected(true);
                        searchTagBuddiesListener.OnSelect(tbm);
                    }
                    if (getSelectedIds().size()==0){
                        searchTagBuddiesListener.OnSearchAction(false);
                    }else {
                        searchTagBuddiesListener.OnSearchAction(true);
                    }
                }
            });
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tbm.getSelected()){
                        checkbox.setChecked(false);
                        tbm.setSelected(false);
                        searchTagBuddiesListener.OnDeSelect(tbm);
                    }else {
                        checkbox.setChecked(true);
                        tbm.setSelected(true);
                        searchTagBuddiesListener.OnSelect(tbm);
                    }
                    if (getSelectedIds().size()==0){
                        searchTagBuddiesListener.OnSearchAction(false);
                    }else {
                        searchTagBuddiesListener.OnSearchAction(true);
                    }
                }
            });
        }
    }
}
