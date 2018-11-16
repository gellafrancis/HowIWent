package com.davidgella.howiwent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword, etEmail;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    //private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.btnSignUp).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSignUp:
                registerUser();
                break;
            case R.id.ivBack:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }

    public void Back (View v) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            //handle already login
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }

    }

    private void registerUser() {
        final String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();




        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            etPassword.setError("Minimum length of password is 6");
            etPassword.requestFocus();
            return;
        }

        if(username.isEmpty()){
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User (id,username,email);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        finish();
                                        Log.d("HW", "createUserWithEmail:success");
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(getApplicationContext(),ThankYouActivity.class));
                                    }else{
                                        // If sign in fails, display a message to the user.
                                        Log.w("HW", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w("HW", "createUserWithEmail:failure", task.getException());

                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(SignupActivity.this, "You are already registered..",
                                            Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        }

                        // ...

         });








    }
}
