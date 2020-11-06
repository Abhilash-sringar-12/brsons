package com.example.brsons.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brsons.R;
import com.example.brsons.pojo.ImageUploadInfo;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageUploadInfo> MainImageUploadInfoList;
    private List<ImageUploadInfo> filteredObjects;

    public RecyclerAdapter(Context mContext, List<ImageUploadInfo> imageUploadInfo) {
        this.mContext = mContext;
        this.MainImageUploadInfoList = imageUploadInfo;
        this.filteredObjects = new ArrayList<>(MainImageUploadInfoList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        holder.textView.setText(UploadInfo.getImageName());
        Glide.with(mContext).load(UploadInfo.getImageURL()).into(holder.jewel_image);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, UploadInfo.getImageName()+" clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView jewel_image;
        public TextView textView;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jewel_image = itemView.findViewById(R.id.jewel_image);
            textView = itemView.findViewById(R.id.textView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.jewelryList);
        }
    }

    private Filter filterByJewelryName = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ImageUploadInfo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filteredObjects);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ImageUploadInfo item : filteredObjects) {
                    if (item.getImageName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            MainImageUploadInfoList.clear();
            MainImageUploadInfoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public Filter getFilter() {
        return filterByJewelryName;
    }
}
