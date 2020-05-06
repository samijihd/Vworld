package com.example.vworld_project.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder> {

    private Context mContext;
    private List<Bid> mBid;
    private SharedPreferences sharedPreferences;
    private AlertDialog.Builder dialog;

    public BidAdapter(Context mContext, List<Bid> mBid){
        this.mContext = mContext;
        this.mBid = mBid;
        sharedPreferences = mContext.getSharedPreferences("IDs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public BidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bid_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final BidAdapter.ViewHolder holder, int position) {
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

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               final String OwnerID = sharedPreferences.getString("OwnerId", "");
               final String projectID = sharedPreferences.getString("ProjectId", "");
               FirebaseAuth auth = FirebaseAuth.getInstance();
               FirebaseUser firebaseUser = auth.getCurrentUser();
               assert firebaseUser != null;
               if (firebaseUser.getUid().equals(OwnerID)){
                   dialog = new AlertDialog.Builder(mContext);
                   dialog.setTitle("Accept Bid");
                   dialog.setMessage("Do you want to accept this Bid?");
                   dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           final HashMap<String, Object> hashMap = new HashMap<>();
                           hashMap.put("isAccepted", "true");
                           hashMap.put("isVisible", "false");
                           hashMap.put("freelancer", bid.getBidderId());


                           //add notification for the bidder to tell him that his bid was accepted
                           addNotification(bid.getBidderId(), OwnerID, bid.getProjectId(), bid.getId());

                           FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    firebaseDatabase
                                   .getReference("Project")
                                   .child(projectID)
                                   .updateChildren(hashMap)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           Toast.makeText(mContext, "The bid accepted successfully",
                                                   Toast.LENGTH_LONG).show();
                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                       }
                                   });
                       }
                   });
                   dialog.setNegativeButton("Cancel", null);
                   dialog.setIcon(android.R.drawable.ic_dialog_alert);
                   dialog.show();
               }
               return true;
           }
       });
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

    private void addNotification(String bidderID, String publisher, String projectID, String bidId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(bidderID);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userID", publisher);
        hashMap.put("title", "bid accepted");
        hashMap.put("text", "your bid has been accepted");
        hashMap.put("projectID", projectID);
        hashMap.put("ownerID", publisher);
        hashMap.put("notifyType", "projectAccepted");
        hashMap.put("bidId", bidId);

        reference.push().setValue(hashMap);
    }
}
