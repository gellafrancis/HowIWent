package com.davidgella.howiwent;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText etUsername, etPassword, etEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);


        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

      findViewById(R.id.btnLogin).setOnClickListener(this);
      findViewById(R.id.tvSignUp).setOnClickListener(this);
      findViewById(R.id.tvForgotPassword).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvSignUp:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.tvForgotPassword:
                Log.i("HW", "Click forgot password");
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }
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

    private void userLogin() {
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent i = new Intent(LoginActivity.this,ProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





}
