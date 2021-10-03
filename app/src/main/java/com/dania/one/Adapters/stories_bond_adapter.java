package com.dania.one.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dania.one.Model.Story;
import com.dania.one.Model.StoryPacket;
import com.dania.one.Model.UserModel;
import com.dania.one.R;
import com.dania.one.StoriesFetchedGlobal;
import com.dania.one.ViewBuddiesStories;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.BlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class stories_bond_adapter extends RecyclerView.Adapter <stories_bond_adapter.storiesBondViewHolder>{

    Context context;

    public stories_bond_adapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public stories_bond_adapter.storiesBondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.story_bond_cv,parent,false);

        return new stories_bond_adapter.storiesBondViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final stories_bond_adapter.storiesBondViewHolder holder, final int position) {
        StoryPacket storyPacket = StoriesFetchedGlobal.buddies_packets.get(position);
        final UserModel um = storyPacket.getUserModel();
        Story sm = storyPacket.getStoryModels().get(0);
        String mUri = sm.getUri();
        if (sm.getType().equals("image")){
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mUri))
                    .setPostprocessor(new BlurPostProcessor(10,context,50))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder().setImageRequest(request)
                    .setOldController(holder.iv.getController())
                    .build();
            holder.iv.setController(controller);
            //Uri uri = Uri.parse(mUri);
            //holder.iv.setImageURI(uri);
        }else {
            Glide.with(context).load(mUri).thumbnail(0.2f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        }
        String mdpUri = um.getDisplayPicture();
        Uri dpUri = Uri.parse(mdpUri);
        holder.pp.setImageURI(dpUri);
        holder.name.setText(um.getName());

        int no_of_stories = storyPacket.getStoryModels().size();
        String no = Integer.toString(no_of_stories);
        if (no_of_stories==1){
            holder.no_of_stories.setText(no+" new story");
        }else {
            holder.no_of_stories.setText(no+" new stories");

        }

       /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Glide.with(detail_user_dialog.getContext()).load(mUri).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(dialog_dp);
                Glide.with(detail_user_dialog.getContext()).load(um.getDisplayPicture()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(dialog_pp);
                dialog_name.setText(um.getName());
                detail_user_dialog.show();
                return false;
            }
        });
        */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewBuddiesStories.class);
                intent.putExtra("position",position);
                context.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return StoriesFetchedGlobal.buddies_packets.size();
    }

    public class storiesBondViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        SimpleDraweeView iv,pp;
        CircleImageView profile_pic;
        TextView name,no_of_stories;

        public storiesBondViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            pp = (SimpleDraweeView) itemView.findViewById(R.id.pp);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            name = itemView.findViewById(R.id.name);
            no_of_stories = itemView.findViewById(R.id.no_of_stories);
        }
    }
}

