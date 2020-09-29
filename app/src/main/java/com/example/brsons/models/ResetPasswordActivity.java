package com.example.brsons.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brsons.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextInputLayout userEmail;
    Button userPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.reset_email);
        userPassword = findViewById(R.id.reset_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        /**
         * Click lister to trigger reset authentication password
         */
        userPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isValid(userEmail.getEditText().getText().toString())) {
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseAuth.sendPasswordResetEmail(userEmail.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "Reset Password link sent to your Email", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    private boolean isValid(String email) {
        boolean isValid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            userEmail.setError("Please enter valid email Id!");
            userEmail.requestFocus();
        } else {
            userEmail.setError(null);
        }
        return isValid;
    }
}