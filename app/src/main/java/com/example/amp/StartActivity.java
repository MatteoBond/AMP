//samples of code taken from: https://www.youtube.com/watch?v=xJ_6eMKpVLQ&ab_channel=ProgrammingKnowledge
//https://www.youtube.com/watch?v=ItqsK5uhLBg&ab_channel=ProgrammingKnowledge

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class StartActivity extends AppCompatActivity {

    //creating variables for use
    private Button login, register;
    private EditText userEmail, userPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); //display activity_start xml file

        login = findViewById(R.id.login); //initialising login button with login button from xml
        register = findViewById(R.id.registerMain); //initialising register button with register button from xml

        //initialising EditText variables with respective EditText from xml
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance(); //auth variable is initialised with the instance of the Firebase project
        // that is being used for this app

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //OnClickListener method for register button
                startActivity(new Intent(StartActivity.this, RegisterActivity.class)); //when clicked, start the Register activity
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //OnClickListener method for login button

                //text_email and text_password variables initialised with the contents entered in EditText
                //using toString() method to parse to string

                String text_email = userEmail.getText().toString();
                String text_password = userPassword.getText().toString();

                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){

                    //if one or both of the variable are empty, a toast will inform the user that the credentials are empty
                    Toast.makeText(StartActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();

                } else if (text_password.length() < 6){ //checks if the password is less than 6 characters
                    //if the password is less than 6 characters, a toast will inform the user that the password is too short
                    Toast.makeText(StartActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();

                }else{
                    //loginUser method is called with the text_email and text_password variables passed
                    loginUser(text_email, text_password); //calling login variables and passing the text_email and text_password variables
                }
            }
        });
    }

    private void loginUser(String email, String password){

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) { //this method will be run if the user is successfully authorised
                //Display toast that says Login successful!
                Toast.makeText(StartActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartActivity.this, HomePage.class)); //start the HomePage activity
                finish();
            }
        });

        auth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                //Display toast that says Login is unsuccessful!
                Toast.makeText(StartActivity.this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}