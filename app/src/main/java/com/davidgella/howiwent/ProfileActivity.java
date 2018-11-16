package com.davidgella.howiwent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth mAuth;
    TextView tvUsername;
    ProgressBar progressBar;

    DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);

        mAuth = FirebaseAuth.getInstance();
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        loadUserInformation();

        findViewById(R.id.ivSignOut).setOnClickListener(this);


    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Users").orderByChild("id").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot Users : dataSnapshot.getChildren()) {
                        tvUsername.setText(Users.child("username").getValue().toString());
//                        System.out.println("HEY: " + Users.child("username").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




     }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivSignOut:
               FirebaseAuth.getInstance().signOut();
                Intent i = new Intent (this, LoginActivity.class);
                startActivity(i);
                finish();

                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            //handle already login
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
//    public void toContribute1 (View v) {
//        Intent i = new Intent(this,Contribute1Activity.class);
//        startActivity(i);
//    }
//
//    public void toSeeLatest (View v) {
//        Intent i = new Intent(this,SeeLatestActivity.class);
//        startActivity(i);
//    }

