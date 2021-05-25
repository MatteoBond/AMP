//samples of code taken from: https://www.youtube.com/watch?v=CQ5qcJetYAI&ab_channel=BenO%27Brien

package com.example.amp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {

    //creating variables for later use
    private ImageView profilepic;
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); //display activity_homepage xml file

        profilepic = findViewById(R.id.profilepicture); //instantiates ImageView with respective ImageView from xml file
        Button logout = findViewById(R.id.logoutbutton); //creates and instantiates Button with respective Button from xml file

        profilepic.setOnClickListener(new View.OnClickListener(){ //runs when the profile picture image is selected
            @Override
            public void onClick(View view){
                choosePic(); //runs the choosePic() method
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signs out user
                FirebaseAuth.getInstance().signOut();

                //informs user that have been logged out
                Toast.makeText(SettingsActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SettingsActivity.this, StartActivity.class); //creates a new intent to be executed later
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //specifying how the intent should be handled

                startActivity(intent); //starts the previously created intent
            }
        });
    }

    private void choosePic() {
        Intent intent = new Intent(); //creates new intent object that will be responsible for opening the gallery on the device
        intent.setType("image/*"); //setting the intent type
        intent.setAction(Intent.ACTION_GET_CONTENT); //setting the action of the intent which will return images as specified by the type
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){ //checking if the result code matches and whether an image was selcted
            imageUri = data.getData(); //assigning the selected image to imageUri
            profilepic.setImageURI(imageUri);  //updating ImageView to the image that was selected
            uploadImage(); //calls the uploadImage() method
        }
    }

    private void uploadImage() {

        final String randomKey = UUID.randomUUID().toString(); //creating a random key
        final ProgressDialog pd = new ProgressDialog(this); //creating a new ProgressDialog object which will serve as a loading bar
        pd.setTitle("Uploading image..."); //Setting ProgressDialog title
        pd.show(); //display the ProgressDialog on the screen

        //creating and instantiating variables needed for uploading image to firestore
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        //uploading image to firestore
        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(TaskSnapshot taskSnapshot) {
                pd.dismiss(); //remove the ProgressDialog from the screen

                //informs the user that the image has been successfully uploaded
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        pd.dismiss(); //remove the ProgressDialog from the screen

                        //informs the user that the image failed to upload
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<TaskSnapshot>() { //creating a listener that will be called periodically as the putFile method executes
                    @Override
                    public void onProgress(@NonNull @NotNull TaskSnapshot snapshot) {

                        //calculating the percentage of the upload by dividing the number of bytes that have been uploaded by the total number of bytes and then multiplying that number by 100
                        double progressPercent = 100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        pd.setMessage("Percentage: " + progressPercent + "%"); //sets the message of the progress bar to the new percentage
                    }
                });
    }
}