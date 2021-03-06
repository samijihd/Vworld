package com.example.vworld_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vworld_project.Model.Project;
import com.example.vworld_project.Activity.ProjectActivity;
import com.example.vworld_project.R;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        final Project project = mProjects.get(position);
        holder.title.setText(project.getTitle());
        holder.time.setText(project.getTime());

        holder.bids.setText(project.getBidno());
        holder.budget.setText(project.getBudget());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("projectid", project.getId());
                intent.putExtra("OwnerId", project.getOwnerid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

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