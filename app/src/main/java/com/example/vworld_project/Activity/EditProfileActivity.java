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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText id = findViewById(R.id.id);
        final EditText firstName = findViewById(R.id.first_name);
        final EditText lastName = findViewById(R.id.last_name);
        final EditText jobTitle = findViewById(R.id.job_title);
        final EditText username = findViewById(R.id.username);

        // the address will be viewed by spinner list
        // the items will be taken from array-string from string.xml
        // i don't have internet now, so i can not do it right now !!
        final EditText address = findViewById(R.id.address);

        RadioGroup genderGroup = findViewById(R.id.radioGroup);
        RadioButton male = findViewById(R.id.male);
        RadioButton female = findViewById(R.id.female);
        RadioButton other = findViewById(R.id.other);

        Button save = findViewById(R.id.save);
        ImageView back = findViewById(R.id.back);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        assert firebaseUser != null;
        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                id.setText(user.getId());
                firstName.setText(user.getName());
                lastName.setText("~");
                jobTitle.setText(user.getJobtitle());
                username.setText(user.getUsername());
                address.setText(user.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = firstName.getText().toString().trim();
                String last_name = lastName.getText().toString().trim();
                String job_title = jobTitle.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String address_text = address.getText().toString().trim();

                if (TextUtils.isEmpty(first_name)){
                    firstName.setError("Empty!");
                    firstName.requestFocus();
                } else if (TextUtils.isEmpty(last_name)){
                    lastName.setError("Empty!");
                    lastName.requestFocus();
                } else if (TextUtils.isEmpty(user_name)){
                    username.setError("Empty!");
                    username.requestFocus();
                } else if (TextUtils.isEmpty(job_title)){
                    jobTitle.setError("Empty!");
                    jobTitle.requestFocus();
                } else if (TextUtils.isEmpty(address_text)){
                    address.setError("Empty!");
                    address.requestFocus();
                }
                else
                {
                    saveEditedData(first_name, last_name, user_name, job_title, address_text);
                }
            }
        });
    }

    private void saveEditedData(String first_name, String last_name, String user_name, String job_title, String address_text) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        assert firebaseUser != null;
        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", first_name + " " + last_name);
        hashMap.put("search", first_name + " " + last_name);
        hashMap.put("username", user_name);
        hashMap.put("jobtitle",job_title);
        hashMap.put("address", address_text);

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditProfileActivity.this,
                        "Your information has been changed successfully",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this,
                        "Error!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
