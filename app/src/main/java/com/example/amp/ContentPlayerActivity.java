package com.example.amp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.io.IOException;
import java.security.Key;

public class ContentPlayerActivity extends AppCompatActivity {
    // I used this page to understand the MediaPlayer https://developer.android.com/reference/android/media/MediaPlayer

    Button playbutton, pausebutton;
    TextView songname, durationstart, durationend;
    SeekBar seekBar;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    MediaPlayer mediaPlayer;
    int position;
    FirestoreRecyclerAdapter<SongsModel, SongAdapter.SongHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_player);

        playbutton = findViewById(R.id.playbutton);
        pausebutton = findViewById(R.id.pausebutton);
        songname = findViewById(R.id.songname);
        durationstart = findViewById(R.id.durationstart);
        durationend =  findViewById(R.id.durationend);
        seekBar = findViewById(R.id.seekbar);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        // Retrieving song name from intent
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        songname.setSelected(true);

        // TextView songname will have the text that was from the intent of the DisplySongs.java
        songname.setText(songName);


        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here the song that will be played depends on the Songname that was selected from the recycler view
                if (songName.equals("Mocking Bird")) {
                    //If the musicplayer does not detect any song playing then it will play the selected song from the raw folder
                    if(mediaPlayer == null){
                        mediaPlayer = MediaPlayer.create(ContentPlayerActivity.this, R.raw.mockingbird);
                    }

                    mediaPlayer.start();

                    // This link was used for the Seek Bar - https://www.youtube.com/watch?v=aZdcrU_QGzo&list=PL9vy4y29rrd4x5pAbowit8gpjsXAai0yF&index=5&ab_channel=FormaLab

                    String duration = millisecondsToString(mediaPlayer.getDuration());
                    durationend.setText(duration);

                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  // This allows the user to move the seekbar cursor to go further
                        @Override                                                               // into the song or to go back to a specific part of the song
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                            if(isFromUser){
                                mediaPlayer.seekTo(progress);
                                seekBar.setProgress(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    new Thread(new Runnable() { // This will put the current duration of the song and will change if the seekbar cursor is moved
                        @Override
                        public void run() {
                            while(mediaPlayer != null){
                                if(mediaPlayer.isPlaying()){
                                    try{
                                        final double current = mediaPlayer.getCurrentPosition();
                                        final String elapsedTime = millisecondsToString((int) current);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                durationstart.setText(elapsedTime);
                                                seekBar.setProgress((int) current);
                                            }
                                        });
                                        Thread.sleep(1000);

                                    }catch (InterruptedException e){}
                                }
                            }
                        }
                    }).start();

                }

                // Here the song that will be played depends on the Songname that was selected from the recycler view
                else if (songName.equals("Everything Black")) {
                    if(mediaPlayer == null){
                        //If the musicplayer does not detect any song playing then it will play the selected song from the raw folder
                        mediaPlayer = MediaPlayer.create(ContentPlayerActivity.this, R.raw.everythingblack);
                    }
                    mediaPlayer.start();
                    String duration = millisecondsToString(mediaPlayer.getDuration());
                    durationend.setText(duration);

                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //This allows the user to move the seekbar cursor to go further
                        @Override                                                               // into the song or to go back to a specific part of the song
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                            if(isFromUser){
                                mediaPlayer.seekTo(progress);
                                seekBar.setProgress(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    new Thread(new Runnable() { // This will put the current duration of the song and will change if the seekbar cursor is moved
                        @Override
                        public void run() {
                            while(mediaPlayer != null){
                                if(mediaPlayer.isPlaying()){
                                    try{
                                        final double current = mediaPlayer.getCurrentPosition();
                                        final String elapsedTime = millisecondsToString((int) current);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                durationstart.setText(elapsedTime);
                                                seekBar.setProgress((int) current);
                                            }
                                        });
                                        Thread.sleep(1000);

                                    }catch (InterruptedException e){}
                                }
                            }
                        }
                    }).start();
                }
                else {

                }

            }

        });

        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }

        });
    }

    public String millisecondsToString(int time){
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes + ":";
        if(seconds < 10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return elapsedTime;
    }

    // This was the attempt of adding the fragment when the back button of Android OS as pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.container, new MusicFragment());
            fragmentTransaction.commit();

        }
        return true;
    }
}