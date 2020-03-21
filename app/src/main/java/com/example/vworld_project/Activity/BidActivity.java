package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        TextView title = (TextView) findViewById(R.id.title);
        final EditText paid = (EditText) findViewById(R.id.paid_input);
        final EditText day = (EditText) findViewById(R.id.day_input);
        final EditText description = (EditText) findViewById(R.id.description_input);
        ImageView back = (ImageView) findViewById(R.id.back);
        Button bidSubmit = (Button) findViewById(R.id.bid_submit);

        final Intent intent = getIntent();
        final String mTitle = intent.getStringExtra("title");
        final String projectid = intent.getStringExtra("projectid");
        title.setText(mTitle);

        final String[] imgUrl = new String[1];
        final String[] name = new String[1];

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert firebaseUser != null;
        DatabaseReference userData = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String url = dataSnapshot.child("imageURL").getValue().toString();
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                imgUrl[0] = user.getImageURL();
                name[0] = user.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bidSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paid_txt = paid.getText().toString().trim();
                String day_txt = day.getText().toString().trim();
                String description_txt = description.getText().toString().trim();
                if (TextUtils.isEmpty(paid_txt)){
                    paid.setError("Empty!");
                    paid.requestFocus();
                }
                else if (TextUtils.isEmpty(day_txt)){
                    day.setError("Empty!");
                    day.requestFocus();
                }
                else if (TextUtils.isEmpty(description_txt)){
                    description.setError("Emapty!");
                    description.requestFocus();
                }
                else {
                    submitBid(paid_txt, day_txt, description_txt, projectid, imgUrl, name);
                }
            }
        });
    }

    private void submitBid(String paid, String day, String description, String projectid, String[] imgUrl, String[] name) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        assert projectid != null;
        DatabaseReference reference = firebaseDatabase.getReference("Project")
                .child(projectid)
                .child("Bids");

        String id = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        assert firebaseUser != null;
        hashMap.put("id", id);
        hashMap.put("bidderId", firebaseUser.getUid());
        hashMap.put("projectId", projectid);
        hashMap.put("imageURL", imgUrl[0]);
        hashMap.put("name", name[0]);
        hashMap.put("paid", paid);
        hashMap.put("day", day);
        hashMap.put("description", description);

        assert id != null;
        reference.child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BidActivity.this, "Your bid added successfully" , Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BidActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
