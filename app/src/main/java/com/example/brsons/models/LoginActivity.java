package com.example.brsons.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brsons.R;
import com.example.brsons.commons.Commons;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView resetPassword;
    ProgressDialog pd;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Commons.isNetworkConnected(getApplicationContext())) {
            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            buttonLogin = findViewById(R.id.login);
            resetPassword = findViewById(R.id.reset_password);
            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (Commons.isNetworkConnected(getApplicationContext())) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (Optional.ofNullable(user).isPresent()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }
                }
            };

            /**
             * Click listener to validate login credentials entered and authenticate
             */
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextEmail.getEditText().getText().toString();
                    String passw = editTextPassword.getEditText().getText().toString().trim();
                    if (isValid(email, passw)) {
                        pd = ProgressDialog.show(LoginActivity.this, "Please wait", "Signing in....", true, false);
                        loginUser(email, passw);
                    }
                }
            });

            /**
             * Click listener to start reset password activity
             */
            resetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                }
            });
        } else {
            Toast.makeText(this, "Please check you Network connectivity", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Firebase authentication function
     *
     * @param email
     * @param passw
     */
    private void loginUser(String email, String passw) {
        firebaseAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Commons.dismissProgressDialog(pd);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Field validation
     *
     * @param email
     * @param passw
     * @return
     */
    private boolean isValid(String email, String passw) {
        boolean isValid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            editTextEmail.setError("Please enter valid email Id!");
            editTextEmail.requestFocus();
        } else {
            editTextEmail.setError(null);
        }
        if (passw.isEmpty()) {
            isValid = false;
            editTextPassword.setError("Please enter password!");
            editTextPassword.requestFocus();
        } else {
            editTextEmail.setError(null);
        }
        return isValid;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Optional.ofNullable(authStateListener).isPresent()) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        }
    }

}