package com.example.vworld_project.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vworld_project.BidsListActivity;
import com.example.vworld_project.MessageActivity;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;

import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder> {

    private Context mContext;
    private List<Bid> mBid;

    public BidAdapter(Context mContext, List<Bid> mBid){
        this.mContext = mContext;
        this.mBid = mBid;
    }

    @NonNull
    @Override
    public BidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bid_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BidAdapter.ViewHolder holder, int position) {
        final Bid bid = mBid.get(position);

        holder.name.setText(bid.getName());
        holder.budget.setText(bid.getPaid() + " $");
        holder.days.setText("in " + bid.getDay() + " Days");
        holder.description.setText(bid.getDescription());

        if (bid.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(bid.getImageURL()).into(holder.profile_image);
        }

       /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , BidsListActivity.class);
                intent.putExtra("projectId" , bid.getProjectId());
                mContext.startActivity(intent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mBid.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, budget, days, description;
        ImageView profile_image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            budget = itemView.findViewById(R.id.budget);
            days = itemView.findViewById(R.id.days);
            description = itemView.findViewById(R.id.description);
            profile_image = itemView.findViewById(R.id.profile_img);
        }
    }
}
