package com.example.vworld_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.Account;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav_View = findViewById(R.id.bottom_nav);

        final MessagesFragment msgfragment = new MessagesFragment();
        final MyprojectsFragment myprojectfragment = new MyprojectsFragment();
        final PostFragment postFragment = new PostFragment();
        final BrowseFragment browsefragment = new BrowseFragment();
        final AccountFragment accountfragment = new AccountFragment();

        setFragment(msgfragment);

        nav_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.msgID){
                    setFragment(msgfragment);
                    return true;
                }
                else if(id == R.id.projectID){
                    setFragment(myprojectfragment);
                    return  true;
                }
                else if(id == R.id.postID){
                    setFragment(postFragment);
                    return true;
                }
                else if(id == R.id.browseID){
                    setFragment(browsefragment);
                    return true;
                }
                else if(id == R.id.accountID){
                    setFragment(accountfragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameID , fragment);
        fragmentTransaction.commit();
    }
}
