package com.example.vworld_project.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Model.Review;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private Context mContext;
    private List<Review> mReview;

    public ReviewListAdapter(Context mContext, List<Review> mReview){
        this.mContext = mContext;
        this.mReview = mReview;
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rating_item, parent, false);
        return new ReviewListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ReviewListAdapter.ViewHolder holder, int position) {
        final Review review = mReview.get(position);

        holder.ratingBar.setEnabled(false);
        holder.ratingBar.setRating(review.getRateValue());
        holder.text.setText(review.getText());

        getUserInfo(review.getReviewAdderId(), holder.profile_image, holder.name);
    }

    @Override
    public int getItemCount() {
        return mReview.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, text;
        RatingBar ratingBar;
        ImageView profile_image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            text = itemView.findViewById(R.id.text);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            profile_image = itemView.findViewById(R.id.profile_img);
        }
    }

    private void getUserInfo(final String userId, final ImageView imageView, final TextView name){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name.setText(user.getName());
                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(mContext).load(user.getImageURL()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, databaseError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
