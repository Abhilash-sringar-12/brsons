package com.example.brsons.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brsons.R;
import com.example.brsons.models.EnquiryFormActivity;
import com.example.brsons.models.LoginActivity;
import com.example.brsons.models.ResetPasswordActivity;
import com.example.brsons.pojo.EnquiryInfo;

import java.util.ArrayList;
import java.util.List;

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.EnquiryViewHolder> {

    private Context mContext;
    private List<EnquiryInfo> MainEnqiryInfoList;
    private List<EnquiryInfo> filteredObjects;

    public EnquiryAdapter(Context mContext, List<EnquiryInfo> mainEnqiryInfoList) {
        this.mContext = mContext;
        MainEnqiryInfoList = mainEnqiryInfoList;
        this.filteredObjects = new ArrayList<>(MainEnqiryInfoList);
    }

    @NonNull
    @Override
    public EnquiryAdapter.EnquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enquired_user_item, parent, false);

        return new EnquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryAdapter.EnquiryViewHolder holder, int position) {
        final EnquiryInfo enquireInfo = MainEnqiryInfoList.get(position);

        holder.EnquiryName.setText(enquireInfo.getName());
        holder.EnquiryDescription.setText(enquireInfo.getDescription());
        holder.ActivityStatus.setText(enquireInfo.getStatus());


        holder.ralativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent;
                intent =  new Intent(mContext, EnquiryFormActivity.class);
                intent.putExtra("name", enquireInfo.getName());
                intent.putExtra("email", enquireInfo.getEmail());
                intent.putExtra("phone", enquireInfo.getPhone());
                intent.putExtra("category", enquireInfo.getCategory());
                intent.putExtra("description", enquireInfo.getDescription());
                intent.putExtra("status", enquireInfo.getStatus());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return MainEnqiryInfoList.size();
    }

    public class EnquiryViewHolder extends RecyclerView.ViewHolder {

        public TextView EnquiryName, EnquiryDescription, ActivityStatus;
        public RelativeLayout ralativeLayout;

        public EnquiryViewHolder(@NonNull View itemView) {
            super(itemView);

            EnquiryName = itemView.findViewById(R.id.enquiryName);
            EnquiryDescription = itemView.findViewById(R.id.enquiryDescription);
            ActivityStatus = itemView.findViewById(R.id.activityStatus);
            ralativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutActivities);


        }
    }

    private Filter filterByJewelryName = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<EnquiryInfo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filteredObjects);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (EnquiryInfo item : filteredObjects) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
            MainEnqiryInfoList.clear();
            MainEnqiryInfoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public Filter getFilter() {
        return filterByJewelryName;
    }
}
