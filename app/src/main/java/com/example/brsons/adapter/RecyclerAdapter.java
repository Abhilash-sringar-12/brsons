package com.example.brsons.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brsons.R;
import com.example.brsons.pojo.ImageUploadInfo;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageUploadInfo> MainImageUploadInfoList;

    public RecyclerAdapter(Context mContext, List<ImageUploadInfo> imageUploadInfo) {
        this.mContext = mContext;
        this.MainImageUploadInfoList = imageUploadInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.textView.setText(UploadInfo.getImageName());

        Glide.with(mContext).load(UploadInfo.getImageURL()).into(holder.jewel_image);

    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView jewel_image;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jewel_image = itemView.findViewById(R.id.jewel_image);
            textView = itemView.findViewById(R.id.textView);
        }
    }

}
