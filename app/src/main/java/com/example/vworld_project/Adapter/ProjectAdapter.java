package com.example.vworld_project.Adapter;

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
import com.example.vworld_project.MessageActivity;
import com.example.vworld_project.Model.Project;
import com.example.vworld_project.R;

import org.w3c.dom.Text;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private Context mContext;
    private List<Project> mProjects;

    public ProjectAdapter(Context mContext, List<Project> mProjects){
        this.mContext = mContext;
        this.mProjects = mProjects;
    }

    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        return new ProjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        Project project = mProjects.get(position);
        holder.title.setText(project.getTitle());
        holder.time.setText(project.getTime());
        holder.bids.setText(project.getBidno());
        holder.budget.setText(project.getBudget());
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, time, bids, budget;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.project_title);
            time = itemView.findViewById(R.id.period_project);
            bids = itemView.findViewById(R.id.bids);
            budget = itemView.findViewById(R.id.cost);

        }
    }
}