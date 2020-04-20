package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.Model.Project;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProjectActivity extends AppCompatActivity {

    String mTitle;
    String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        ImageView back = (ImageView) findViewById(R.id.back);
        final Button bid = (Button) findViewById(R.id.bid_btn);
        bid.setEnabled(true);

        final CardView profile_card = findViewById(R.id.profile_card);
        final CircleImageView profile_image = findViewById(R.id.profile_image);
        final TextView name = findViewById(R.id.name);
        final TextView username = findViewById(R.id.username);

        final TextView title = findViewById(R.id.title);
        final TextView date = findViewById(R.id.date);
        final TextView description = findViewById(R.id.description);
        final TextView budget = findViewById(R.id.budget);
        final TextView skill = findViewById(R.id.skill);
        final TextView bids = findViewById(R.id.bids);
        final TextView seeMore = findViewById(R.id.see_more);

        final Intent intent = getIntent();
        final String projectid = intent.getStringExtra("projectid");
        ownerID = intent.getStringExtra("OwnerId");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        //bind post owner profile image and data
                firebaseDatabase
                .getReference("Users")
                .child(ownerID)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        name.setText(user.getName());
                        username.setText("@" + user.getUsername());
                        if (user.getImageURL().equals("default")){
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        }
                        else{
                            Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                profile_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext() , ProfileActivity.class);
                        intent.putExtra("userid" , ownerID);
                        startActivity(intent);
                    }
                });


        assert projectid != null;
        final DatabaseReference reference = firebaseDatabase.getReference("Project").child(projectid);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);

                assert project != null;
                mTitle = project.getTitle();

                title.setText(project.getTitle());
                date.setText(project.getTime());
                description.setText(project.getDescription());
                budget.setText(" $" + project.getBudget());
                skill.setText(project.getSkill());
                // to get count (number) of bids of the project
                long count = dataSnapshot.child("Bids").getChildrenCount();
                bids.setText(Long.toString(count));

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("bidno", Long.toString(count));

                reference.updateChildren(hashMap);

                ownerID = project.getOwnerid();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        assert firebaseUser != null;
        final DatabaseReference query = FirebaseDatabase.getInstance().getReference("Project")
                .child(projectid)
                .child("Bids");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Bid bid1 = snapshot.getValue(Bid.class);
                    assert bid1 != null;
                    if (bid1.getBidderId().equals(firebaseUser.getUid())){
                        bid.setEnabled(false);
                        Toast.makeText(ProjectActivity.this, "You had already bid for this project", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectActivity.this , BidActivity.class);
                intent.putExtra("projectid", projectid);
                intent.putExtra("title", mTitle);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ProjectActivity.this, BidsListActivity.class));
                Intent intent = new Intent(ProjectActivity.this  , BidsListActivity.class);
                intent.putExtra("projectId" , projectid);
                intent.putExtra("OwnerId", ownerID);
                startActivity(intent);
            }
        });

    }
}
