//samples of code taken from: https://www.youtube.com/watch?v=ejbX9MO2ems&list=PLqGVXjXwMHAuENy4b2G3n58Co6_hRYZGV&index=24&ab_channel=ChiragKachhadiya

package com.example.amp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    //creating ImageView variables to make images interactive
    private ImageView settings;
    private ImageView songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage); //display activity_homepage xml file

        settings= findViewById(R.id.home_settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //OnClickListener method for settings image
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class)); //when clicked, start the Settings activity
//                overridePendingTransition(0,0);
            }
        });

        songs= findViewById(R.id.music_image);
        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //OnClickListener for settings image
                startActivity(new Intent(getApplicationContext(), DisplaySongs.class)); //when clicked, start the DisplaySongs activity
//                overridePendingTransition(0,0);
            }
        });
    }
}