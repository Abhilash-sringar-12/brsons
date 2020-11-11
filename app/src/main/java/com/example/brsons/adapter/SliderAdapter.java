package com.example.brsons.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.brsons.R;
import com.example.brsons.pojo.SliderImageInfo;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ImageViewHolder> {



    private Context mContext;
    private List<SliderImageInfo> MainSliderInfoList;

    public SliderAdapter(Context mContext, List<SliderImageInfo> mainSliderInfoList) {
        this.mContext = mContext;
        MainSliderInfoList = mainSliderInfoList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final SliderImageInfo UploadInfo = MainSliderInfoList.get(position);
        holder.textView.setText(UploadInfo.getSliderTitle());
        Glide.with(mContext).load(UploadInfo.getSliderImageURL()).into(holder.jewel_image);

    }

    @Override
    public int getItemCount() {
        return MainSliderInfoList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView jewel_image;
        public TextView textView;
        public LinearLayout linearLayout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            jewel_image = itemView.findViewById(R.id.jewel_image);
            textView = itemView.findViewById(R.id.textView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.jewelryList);
        }
    }
}
