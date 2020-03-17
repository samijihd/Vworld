package com.example.vworld_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vworld_project.Model.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        ImageView back = (ImageView) findViewById(R.id.back);

        final TextView title = findViewById(R.id.title);
        final TextView date = findViewById(R.id.date);
        final TextView description = findViewById(R.id.description);
        final TextView budget = findViewById(R.id.budget);
        final TextView skill = findViewById(R.id.skill);
        final TextView bids = findViewById(R.id.bids);

        final Intent intent = getIntent();
        final String projectid = intent.getStringExtra("projectid");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert projectid != null;
        DatabaseReference reference = firebaseDatabase.getReference("Project").child(projectid);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                assert project != null;
                title.setText(project.getTitle());
                date.setText(project.getTime());
                description.setText(project.getDescription());
                budget.setText(" $" + project.getBudget());
                skill.setText(project.getSkill());
                bids.setText(project.getBidno());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
