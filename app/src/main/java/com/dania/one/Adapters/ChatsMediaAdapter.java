package com.dania.one.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dania.one.ChatsFetchedGlobal;
import com.dania.one.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

public class ChatsMediaAdapter extends RecyclerView.Adapter<ChatsMediaAdapter.MediaViewHolder>{

    private Context context;

    public ChatsMediaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.mvh_item,parent,false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        if (ChatsFetchedGlobal.mediaChats.get(position).getType().equals("image_msg")){
            Uri uri = Uri.parse(ChatsFetchedGlobal.mediaChats.get(position).getUri());
            DraweeController ctrl = Fresco.newDraweeControllerBuilder().setUri(
                    uri).setTapToRetryEnabled(true).build();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            holder.iv.setController(ctrl);
            holder.iv.setHierarchy(hierarchy);
        }
    }

    @Override
    public int getItemCount() {
        return ChatsFetchedGlobal.mediaUri.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder{

        private com.facebook.samples.zoomable.ZoomableDraweeView iv;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (com.facebook.samples.zoomable.ZoomableDraweeView) itemView.findViewById(R.id.iv);
        }
    }
}
