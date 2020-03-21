package com.example.vworld_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vworld_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public static FirebaseUser user_log = null;

    private ImageView back;
    private TextView register, forgetpassword;

    private EditText email;
    private EditText password;
    public static CheckBox remember;

    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back_ID);
        register = findViewById(R.id.register_ID);

        email = findViewById(R.id.user_reg_ID);
        password = findViewById(R.id.pass_reg_ID);
        remember = findViewById(R.id.remember_ID);
        forgetpassword = findViewById(R.id.forget_ID);


        login = findViewById(R.id.signupbtn_reg_ID);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();


       /* if(user != null){
            startActivity(new Intent(LoginActivity.this , HomeActivity.class));
            finish();
        }*/

       /*if (remember.isChecked()){
           user_log = user;
           Intent intent = new Intent(getBaseContext(), MainActivity.class);
           intent.putExtra("user_log" , user_log);
       }else{
           user_log = null;
       }*/

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , ResetPasswordActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email_txt = email.getText().toString().trim();
                String password_txt = password.getText().toString().trim();

                if(TextUtils.isEmpty(email_txt)){
                    Toast.makeText(LoginActivity.this , "The email is not valid" , Toast.LENGTH_SHORT).show();
                    email.setError("Empty!");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(password_txt)){
                    Toast.makeText(LoginActivity.this , "Enter your password!" , Toast.LENGTH_SHORT).show();
                    password.setError("Empty!");
                    password.requestFocus();
                }
                else{
                    LoginUser(email_txt,password_txt);
                }
            }
        });
    }

    private void LoginUser(String email, String password){

        auth.signInWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this , "login successfully" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this , HomeActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean setKey(boolean key){
        return key;
    }

}
