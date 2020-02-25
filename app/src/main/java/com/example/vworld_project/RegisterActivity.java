package com.example.vworld_project;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, password, copassowrd;
    private Button signUp;

    private ImageView back;
    private TextView login;

    private FirebaseAuth auth;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.user_reg_ID);
        email = findViewById(R.id.email_reg_ID);
        password = findViewById(R.id.pass_reg_ID);
        copassowrd = findViewById(R.id.pass_reg_ID2);

        back = findViewById(R.id.back_ID);
        login = findViewById(R.id.loginlink_ID);
        signUp = findViewById(R.id.signupbtn_reg_ID);

        auth = FirebaseAuth.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_txt = username.getText().toString().trim();
                String email_txt = email.getText().toString().trim();
                String password_txt = password.getText().toString().trim();
                String copassword_txt = copassowrd.getText().toString().trim();

                if(TextUtils.isEmpty(username_txt)){
                    Toast.makeText(RegisterActivity.this , "Enter a username" , Toast.LENGTH_LONG).show();
                    username.setError("Empty!");
                    username.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(email_txt)){
                    Toast.makeText(RegisterActivity.this , "Enter a email" , Toast.LENGTH_LONG).show();
                    email.setError("Empty!");
                    email.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(password_txt) || password_txt.length() < 6){
                    Toast.makeText(RegisterActivity.this , "Make sure that the password is longer than 6 char" ,
                            Toast.LENGTH_LONG).show();
                    password.setError("rewrite password!");
                    password.requestFocus();
                    return;
                }
                else if(!copassword_txt.equals(password_txt)){
                    Toast.makeText(RegisterActivity.this , "the passwords are not same" ,
                            Toast.LENGTH_LONG).show();
                    password.requestFocus();
                    copassowrd.requestFocus();
                    return;
                }
                else {
                    Register(username_txt, email_txt, password_txt);
                }
            }
        });
    }


    private void Register(final String user, String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if(task.isSuccessful()){
                        //get current user data
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null; //its not important ! you can remove it
                        String userid = firebaseUser.getUid();

                        //make refernce for database name= Users
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        reference = database.getReference("Users").child(userid);

                        //create hash map and insert it to realtime db
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("name", user);
                        hashMap.put("username", user);
                        hashMap.put("imageURL", "default");

                        //insert hash map to firebase realtime database
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(RegisterActivity.this , LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this ,
                                "you cant register now try again later!" ,
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(RegisterActivity.this ,
                            e.getLocalizedMessage().toString() , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
