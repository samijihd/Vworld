package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Adapter.MessageAdapter;
import com.example.vworld_project.Model.Message;
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
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ImageView profile_image_, send;
    private EditText message_text;
    private TextView name;
    private RecyclerView recyclerView;

    MessageAdapter messageAdapter;
    List<Message> mMessage;
    String userid;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image_ = findViewById(R.id.profile_image_chat);
        name = findViewById(R.id.name_user_chat);
        ImageView back_arrow = findViewById(R.id.back_message);
        send = findViewById(R.id.send_message_btn);
        message_text = findViewById(R.id.message_Text);

        recyclerView = findViewById(R.id.recycle_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent1 = getIntent();
        userid = intent1.getStringExtra("userid");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message_text.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(msg, firebaseUser.getUid(), userid);
                }
                else{
                    Toast.makeText(MessageActivity.this ,
                            "you can't send empty message" ,
                            Toast.LENGTH_LONG)
                            .show();
                }
                message_text.setText("");
            }
        });

        assert userid != null;
        reference = firebaseDatabase.getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name.setText(user.getUsername());

                if(user.getImageURL().equals("default")){
                    profile_image_.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image_);
                }
                readMessages(firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MessageActivity.this , HomeActivity.class));
               //startActivity(new Intent(MessageActivity.this, HomeActivity.class)
                 //       .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }

    private void sendMessage(String message, String sender, final String receiver) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("sender", sender);
        map.put("receiver", receiver);

        reference.child("Messages").push().setValue(map);

        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userid);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatref.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readMessages(final String myid, final String userid, final String imageurl){
        mMessage = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();

        reference = firebaseDatabase.getReference().child("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessage.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    assert message != null;
                    if(message.getReceiver().equals(myid) && message.getSender().equals(userid)
                            || message.getReceiver().equals(userid) && message.getSender().equals(myid)){
                        mMessage.add(message);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mMessage, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
