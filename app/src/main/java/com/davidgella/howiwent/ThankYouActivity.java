package com.davidgella.howiwent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class ThankYouActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyoupage);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivBack:
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}
