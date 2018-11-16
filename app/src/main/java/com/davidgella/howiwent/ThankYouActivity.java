package com.davidgella.howiwent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoupage);
    }

    public void Back (View v) {
        finish();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}
