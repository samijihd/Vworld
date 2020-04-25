package com.example.vworld_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.vworld_project.Fragment.AccountFragment;
import com.example.vworld_project.Fragment.BrowseFragment;
import com.example.vworld_project.Fragment.MessagesFragment;
import com.example.vworld_project.Fragment.MyprojectsFragment;
import com.example.vworld_project.Fragment.PostFragment;
import com.example.vworld_project.Fragment.SearchFragment;
import com.example.vworld_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView nav_View;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        nav_View = findViewById(R.id.bottom_nav);

        final MessagesFragment msgfragment = new MessagesFragment();
        final MyprojectsFragment myprojectfragment = new MyprojectsFragment();
        final PostFragment postFragment = new PostFragment();
        final SearchFragment searchFragment = new SearchFragment();
        final AccountFragment accountFragment = new AccountFragment();

        if (savedInstanceState == null){
            setFragment(msgfragment);
        }

        nav_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.msgID:
                        setFragment(msgfragment);
                        return true;
                    case R.id.projectID:
                        setFragment(myprojectfragment);
                        return  true;
                    case R.id.postID:
                        setFragment(postFragment);
                        return true;
                    case R.id.searchID:
                        setFragment(searchFragment);
                        return true;
                    case R.id.accountID:
                        setFragment(accountFragment);
                        return true;
                }
                return true;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameID , fragment);
        fragmentTransaction.commit();
    }

    void status(String status){
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);

        reference.updateChildren(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
