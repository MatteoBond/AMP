package com.example.amp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    //creating variables for use
    private EditText email, password;
    private Button register;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); //display activity_register xml file

        email = findViewById(R.id.email); //initialising EditText object with EditText from xml

        password = findViewById(R.id.password); //initialising EditText object with EditText from xml

        register = findViewById(R.id.register); //initialising register button with register button from xml

        auth = FirebaseAuth.getInstance();//auth variable is initialised with the instance of the Firebase project
        // that is being used for this app

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //text_email and text_password variables initialised with the contents entered in EditText
                //using toString() method to parse to string
                String text_email = email.getText().toString();
                String text_password = password.getText().toString();

                //checks whether the text_email & text_password variables are empty
                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){

                    //if one or both of the variable are empty, a toast will inform the user that the credentials are empty
                    Toast.makeText(RegisterActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();

                } else if (text_password.length() < 6){ //checks if the password is less than 6 characters
                    //if the password is less than 6 characters, a toast will inform the user that the password is too short
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();

                }else{
                    //registerUser method is called with the text_email and text_password variables passed
                    registerUser(text_email, text_password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Toast informs the user that the registration of their new account is successful
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomePage.class)); //Start the HomePage activity
                    finish();
                }else {
                    //Toast informs the user that the registration was not successful
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}