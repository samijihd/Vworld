package com.example.vworld_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.Account;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    public Button signout;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    /*
optioins menu items


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinf = getMenuInflater();
        menuinf.inflate(R.menu.home_options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signout){
            auth.signOut();
            startActivity(new Intent(HomeActivity.this , LoginActivity.class));
        }
            return super.onOptionsItemSelected(item);
    }
*/
    /*public void setSignout(FirebaseAuth auth){
        auth.signOut();
        startActivity(new Intent(this , LoginActivity.class));
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        BottomNavigationView nav_View = findViewById(R.id.bottom_nav);

        final MessagesFragment msgfragment = new MessagesFragment();
        final MyprojectsFragment myprojectfragment = new MyprojectsFragment();
        final PostFragment postFragment = new PostFragment();
        final BrowseFragment browsefragment = new BrowseFragment();
        final AccountFragment accountFragment = new AccountFragment();

        setFragment(msgfragment);

        nav_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                try {
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
                        setFragment(accountFragment);
                        return true;
                    }
                    return false;

                }
                catch (Exception e){
                    Toast.makeText(HomeActivity.this , e.getLocalizedMessage().toString() , Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameID , fragment); //.commit(); you can write the next line here
        fragmentTransaction.commit();
    }

    private void status(String status){
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
