package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vworld_project.Model.Bid;
import com.example.vworld_project.Model.Project;
import com.example.vworld_project.Model.User;
import com.example.vworld_project.R;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptedProjectActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private static final int FILE_SELECT_CODE = 1000;

    Button choose_file, save_work_btn, download_file, finish_work, save_rate;
    CardView profile_card;
    RelativeLayout relativeLayout;
    RatingBar ratingBar;
    EditText rating_text;

    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    Bitmap selectedImage;
    Uri uri;

    String projectid, projectOwnerId, bidId, freelancerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_project);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("finishedWorkFiles");

        profile_card = findViewById(R.id.profile_card);
        final CircleImageView profile_image = findViewById(R.id.profile_image);
        final TextView name = findViewById(R.id.name);
        final TextView username = findViewById(R.id.username);

        ratingBar = findViewById(R.id.rating_bar);
        rating_text = findViewById(R.id.text);
        save_rate = findViewById(R.id.save_rate);
        save_rate.setOnClickListener(this);

        ImageView back = (ImageView) findViewById(R.id.back);
        relativeLayout = findViewById(R.id.footer2);
        relativeLayout.setVisibility(View.GONE);

        choose_file = findViewById(R.id.choose_btn);
        choose_file.setOnClickListener(this);
        save_work_btn = findViewById(R.id.save_work_btn);
        save_work_btn.setOnClickListener(this);
        finish_work = findViewById(R.id.finish_project);
        finish_work.setOnClickListener(this);
        download_file = findViewById(R.id.download_file);
        download_file.setOnClickListener(this);

        TextView time = findViewById(R.id.time);
        TextView budget = findViewById(R.id.budget);
        TextView description = findViewById(R.id.description_txt);

        final Intent intent = getIntent();
        projectid = intent.getStringExtra("projectid");
        projectOwnerId = intent.getStringExtra("projectOwnerId");
        bidId = intent.getStringExtra("bidId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //open publisher profile page
        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , ProfileActivity.class);
                intent.putExtra("userid" , projectOwnerId);
                startActivity(intent);
            }
        });

        //bind post owner profile image and data
        bindPublisherInfo(projectOwnerId, name, username, profile_image);

        //bind bid data
        bindBidInfo(projectid, bidId, time, budget, description);

        setViewsVisibility();

    }

    //bind post owner profile image and data
    private void bindPublisherInfo(String ownerID, final TextView name, final TextView username, final ImageView profile_image){

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
    }


    // bind bid info
    private void bindBidInfo(String projectId, String bidId, final TextView time, final TextView budget, final TextView description){

                firebaseDatabase
                .getReference("Project")
                .child(projectId)
                .child("Bids")
                .child(bidId)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Bid bid = dataSnapshot.getValue(Bid.class);
                        assert bid != null;
                        time.setText(" within: "+ bid.getDay());
                        budget.setText(" budget: "+bid.getPaid());
                        description.setText(bid.getDescription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void openFileChooser(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }else {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_SELECT_CODE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_SELECT_CODE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(getApplicationContext()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();

        if (uri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(uri));

            uploadTask = fileReference.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String mUri =   downloadUri.toString();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Project").child(projectid);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("finishedWork", mUri);
                        databaseReference.updateChildren(map);

                        setViewsVisibility();
                        pd.dismiss();

                        addNotificationOnUploadProject();

                        Toast.makeText(getApplicationContext(), "work saved successfully", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No file selected!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(), "Saving files...", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "your file is selected, press save to continue", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void completeProject(){
        databaseReference = firebaseDatabase.getReference("Project").child(projectid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("completed", "true");
        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    addNotificationOnCompleteProject();
                    Toast.makeText(getApplicationContext(), "Your project completed".toString(),
                            Toast.LENGTH_LONG).show();

                    finish_work.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadWork(){
        firebaseDatabase.getReference("Project").child(projectid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                assert project != null;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(project.getFinishedWork()));
                startActivity(browserIntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addRate(){
        String text = rating_text.getText().toString().trim();
        if (TextUtils.isEmpty(text)){
            text = "no comment";
        }

        databaseReference = firebaseDatabase.getReference("Users").child(freelancerId).child("Reviews");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("reviewAdderId", firebaseUser.getUid());
        hashMap.put("rateValue", ratingBar.getRating());
        hashMap.put("text", text);

        databaseReference.child(firebaseUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful()){
                     databaseReference = firebaseDatabase.getReference("Project").child(projectid);
                     HashMap<String, Object> map = new HashMap<>();
                     map.put("rated", "true");
                     databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 Toast.makeText(getApplicationContext(), "your review added successfully", Toast.LENGTH_LONG).show();
                             }
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     });
                 }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == choose_file){
            openFileChooser();
        } else if (view == save_work_btn){
            uploadFile();
        } else if (view == download_file){
            downloadWork();
        } else if (view == finish_work){
            completeProject();
        } else if (view == save_rate){
            addRate();
        }
    }

    private void addNotificationOnUploadProject(){
        databaseReference = firebaseDatabase.getReference("Notifications").child(projectOwnerId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userID", firebaseUser.getUid());
        hashMap.put("title", "Your project is ready");
        hashMap.put("text", "check your project now");
        hashMap.put("projectID", projectid);
        hashMap.put("ownerID", projectOwnerId);
        hashMap.put("notifyType", "readyProject");
        hashMap.put("bidId", bidId);
        databaseReference.push().setValue(hashMap);
    }

    private void addNotificationOnCompleteProject(){
        databaseReference = firebaseDatabase.getReference("Notifications").child(freelancerId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userID", firebaseUser.getUid());
        hashMap.put("title", "Well Done");
        hashMap.put("text", "Your work is completed, you did a good work");
        hashMap.put("projectID", projectid);
        hashMap.put("ownerID", freelancerId);
        hashMap.put("notifyType", "completedProject");
        hashMap.put("bidId", bidId);
        databaseReference.push().setValue(hashMap);
    }

    private void setViewsVisibility(){
        firebaseDatabase.getReference("Project").child(projectid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                assert project != null;
                freelancerId = project.getFreelancer();

                if (project.getFinishedWork() != null){
                    choose_file.setVisibility(View.GONE);
                    save_work_btn.setVisibility(View.GONE);
                    download_file.setVisibility(View.VISIBLE);
                    finish_work.setVisibility(View.VISIBLE);
                } else {
                    choose_file.setVisibility(View.VISIBLE);
                    save_work_btn.setVisibility(View.VISIBLE);
                    download_file.setVisibility(View.GONE);
                    finish_work.setVisibility(View.GONE);
                }

                if (project.getCompleted().equals("false") && projectOwnerId.equals(firebaseUser.getUid())){
                    finish_work.setVisibility(View.VISIBLE);
                } else {
                    finish_work.setVisibility(View.GONE);
                }

                if (project.getCompleted().equals("true") && projectOwnerId.equals(firebaseUser.getUid()) && project.getRated().equals("false")){
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
