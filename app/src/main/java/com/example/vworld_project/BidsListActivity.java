package com.example.vworld_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vworld_project.Adapter.BidAdapter;
import com.example.vworld_project.Adapter.ProfileAdapter;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.Model.Project;
import com.example.vworld_project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BidsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Bid> mBid;
    private ImageView back;

    private BidAdapter bidAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_list);

        recyclerView = findViewById(R.id.recycle_bids_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        back = findViewById(R.id.back);

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

            }
        });

    }
}
