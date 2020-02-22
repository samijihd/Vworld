package com.example.vworld_project;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Button signout;
    private FirebaseAuth auth;

    HomeActivity homeActivity = new HomeActivity();


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account , container , false);

        signout = viewGroup.findViewById(R.id.signoutID);

        auth = FirebaseAuth.getInstance();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                //Toast.makeText(getActivity() , "logged out" , Toast.LENGTH_LONG);
                startActivity(new Intent(getActivity().getApplicationContext() , LoginActivity.class));
            }
        });

        return viewGroup;
    }
}
