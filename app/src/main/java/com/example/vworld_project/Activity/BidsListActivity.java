package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vworld_project.Adapter.BidAdapter;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BidsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Bid> mBid;

    private BidAdapter bidAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_list);


        // recycler view setting
        recyclerView = findViewById(R.id.recycle_bids_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        ImageView back = findViewById(R.id.back);

        mBid = new ArrayList<>();

        final Intent intent = getIntent();
        final String projectId = intent.getStringExtra("projectId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert projectId != null;
        String ownerID = intent.getStringExtra("OwnerId");

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("IDs", MODE_PRIVATE).edit();
        editor.putString("OwnerId", ownerID);
        editor.putString("ProjectId", projectId);
        editor.apply();

        // reference to users to get their names and profile images
        final DatabaseReference usersRef = firebaseDatabase.getReference("Users");
        final DatabaseReference reference = firebaseDatabase.getReference("Project")
                .child(projectId)
                .child("Bids");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Bid bid = snapshot.getValue(Bid.class);
                    assert bid != null;
                    // to make sure that bidders name and profile img is up to date
                    // take them again from users list and bind them to appropriate place in bids list
                    usersRef.child(bid.getBidderId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;
                            reference.child(bid.getId()).child("imageURL").setValue(user.getImageURL());
                            reference.child(bid.getId()).child("name").setValue(user.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                mBid.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    final Bid bid = snapshot1.getValue(Bid.class);
                    mBid.add(bid);
                }
                bidAdapter = new BidAdapter(BidsListActivity.this, mBid);
                recyclerView.setAdapter(bidAdapter);

                if (mBid.isEmpty()){
                    Toast.makeText(BidsListActivity.this, "There is no bids available yet" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("IDs", MODE_PRIVATE).edit();
                editor.putString("OwnerId", "");
                editor.putString("ProjectId", "");
                editor.apply();
            }
        });

    }
}
