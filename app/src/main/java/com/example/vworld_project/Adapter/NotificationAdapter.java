package com.example.vworld_project.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Activity.ProjectActivity;
import com.example.vworld_project.Model.Notification;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context context, List<Notification> notification){
        mContext = context;
        mNotification = notification;
    }

    @NonNull
    @Override
    public NotificationAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notify_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ImageViewHolder holder, final int position) {

        final Notification notification = mNotification.get(position);

        holder.title.setText(notification.getTitle());
        holder.text.setText(notification.getText());

        getUserInfo(holder.profile_image, notification.getUserID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("projectid", notification.getProjectID());
                intent.putExtra("OwnerId", notification.getOwnerID());
                mContext.startActivity(intent);
            }
        });



    }
    //
    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_image;
        public TextView title, text;

        ImageViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            title = itemView.findViewById(R.id.notify_title);
            text = itemView.findViewById(R.id.notify_text);
        }
    }

    private void getUserInfo(final ImageView imageView, String publisherID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(mContext).load(user.getImageURL()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
