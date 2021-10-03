package com.dania.one.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.Model.DateWiseStoriesModel;
import com.dania.one.Model.Story;
import com.dania.one.R;
import com.dania.one.TimeAgo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUserStoriesAdapter extends RecyclerView.Adapter<ViewUserStoriesAdapter.VUSAViewHolder>{

    Context context;
    ArrayList<Story> userStoryModels;

    public ViewUserStoriesAdapter(Context context, ArrayList<Story> userStoryModels) {
        this.context = context;
        this.userStoryModels = userStoryModels;
    }

    @NonNull
    @Override
    public VUSAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vusa_item,parent,false);
        return new VUSAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VUSAViewHolder holder, int position) {
        Story sm = userStoryModels.get(position);
        holder.BindSM(sm);
    }

    @Override
    public int getItemCount() {
        return userStoryModels.size();
    }
/*
    public int getNoOfStories(){
        int number = 0;
        for (int i=0;i<dateWiseStoriesModels.size();i++){
            number = number+dateWiseStoriesModels.get(i).getStoryModels().size();
        }
        return number;
    }

 */

    public class VUSAViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView story_bg,story_iv,dp;
        TextView time;

        public VUSAViewHolder(@NonNull View itemView) {
            super(itemView);
            story_bg = itemView.findViewById(R.id.story_bg);
            story_iv = itemView.findViewById(R.id.story_iv);
            dp = itemView.findViewById(R.id.dp);
            time = itemView.findViewById(R.id.time);
        }

        public void BindSM(Story sm) {
            String timeAgo = TimeAgo.getTimeAgo(sm.getTimestamp());
            time.setText(timeAgo);
            if (sm.getType().equals("image")) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(sm.getUri()))
                        .setPostprocessor(new IterativeBoxBlurPostProcessor(20))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(story_bg.getController())
                        .build();
                story_bg.setController(controller);
                Uri uri = Uri.parse(sm.getUri());
                story_iv.setImageURI(uri);
            }else {

            }
            DatabaseReference dp_ref = FirebaseDatabase.getInstance().getReference("Users").child(sm.getUid()).child("basicInfo").child("display_picture");
            dp_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String user_dp = dataSnapshot.getValue().toString();
                    Uri uri = Uri.parse(user_dp);
                    dp.setImageURI(uri);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
