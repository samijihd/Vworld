package com.example.vworld_project.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vworld_project.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    Date currentTime;
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);

        currentTime = Calendar.getInstance().getTime();
        //time = currentTime.toString();

        final Spinner typeSpinner = (Spinner) viewGroup.findViewById(R.id.type_spinner);
        final Spinner budgetSpinner = (Spinner) viewGroup.findViewById(R.id.spinner_budget);
        final Spinner moneySpinner = (Spinner) viewGroup.findViewById(R.id.money_type_spinner);

        final EditText title = (EditText) viewGroup.findViewById(R.id.title_txt);
        final EditText description = (EditText) viewGroup.findViewById(R.id.description_txt);
        final EditText skills = (EditText) viewGroup.findViewById(R.id.skill_txt);

        Button post = (Button) viewGroup.findViewById(R.id.post_project);

        String[] types = new String[]{
                "Website and IT",
                "Mobile",
                "Art and Design",
                "Data Entry",
                "Software Dev",
                "Writing",
                "Business",
                "Sales",
                "Contest",
                "Local Jobs",
                "Other"
        };

        String[] budget = new String[]{
                " 10 - 30",
                " 30 - 250",
                " 250 - 750",
                " 750 - 1500",
                " 1500 - 3000",
                " 3000 - 5000",
                " 5000 - 10000",
                " 10000 - 20000",
                " 20000 - 50000",
                " 50000 +"
        };

        String[] money = new String[]{
                " $ USD",
                " $ NZD",
                " $ AUD",
                " $ GBP",
                " $ HKD",
                " $ SGD",
                " $ EUR",
                " $ CAD",
                " $ INR"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_item_project_type, types);
        adapter.setDropDownViewResource(R.layout.spinner_item_project_type);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapterBudget = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_item_project_type, budget);
        adapter.setDropDownViewResource(R.layout.spinner_item_project_type);
        budgetSpinner.setAdapter(adapterBudget);

        ArrayAdapter<String> adapterMoney = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_item_project_type, money);
        adapter.setDropDownViewResource(R.layout.spinner_item_project_type);
        moneySpinner.setAdapter(adapterMoney);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String project_title = title.getText().toString().trim();
                String project_description = description.getText().toString().trim();
                String project_type = typeSpinner.getSelectedItem().toString();
                String project_budget = budgetSpinner.getSelectedItem().toString();
                String project_money = moneySpinner.getSelectedItem().toString();
                String project_skill = skills.getText().toString().trim();

                if (TextUtils.isEmpty(project_title)){
                    title.setError("Empty!");
                    title.requestFocus();
                }
                else if (TextUtils.isEmpty(project_description)){
                    description.setError("Empty!");
                    description.requestFocus();
                }
                else {
                    postProject(project_title, project_description, project_type, project_budget, project_money, project_skill);
                    title.setText("");
                    description.setText("");
                    skills.setText("");
                }
            }
        });

        return viewGroup;
    }

    private void postProject(final String title, String description, String type, String budget, String money, String skill) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        currentTime = Calendar.getInstance().getTime();
        //time = currentTime.toString();
        time = java.text.DateFormat.getDateTimeInstance().format(new Date());

        String id = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("title", title);
        hashMap.put("ownerid", firebaseUser.getUid());
        hashMap.put("description", description);
        hashMap.put("type", type);
        hashMap.put("budget", budget);
        hashMap.put("money", money);
        hashMap.put("skill", skill);
        hashMap.put("bidno", " 0");
        hashMap.put("time", time);

        assert id != null;
        reference.child("Project").child(id).setValue(hashMap, new DatabaseReference.CompletionListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(),"Error while creating a project...try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),"Project added successfully", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getActivity(), HomeActivity.class));
                }
            }
        });
    }
}
