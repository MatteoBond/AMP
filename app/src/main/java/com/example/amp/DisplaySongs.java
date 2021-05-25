package com.example.amp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class DisplaySongs extends AppCompatActivity{
    // To create the recycler view I used these 2 youtube videos
    // https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=6&ab_channel=CodinginFlow
    // https://www.youtube.com/watch?v=cBwaJYocb9I&ab_channel=TVACStudio

    // Reference to our recycler view
    private RecyclerView mFirestoreList;

    // Here we created 3  member variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference songRef = db.collection("Songs"); //Reference to our Firestore collection

    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_songs);

        // Here we are able to order the recycler view in Descending view of Artists
        Query query  = songRef.orderBy("Artist", Query.Direction.DESCENDING);
        //This is where we get our query into the adapter
        FirestoreRecyclerOptions<SongsModel> options = new FirestoreRecyclerOptions.Builder<SongsModel>().setQuery(query, SongsModel.class).build();

        // Assign our adapter variable and we pass the options object
        adapter = new SongAdapter(options);

        mFirestoreList = findViewById(R.id.firestore_list);
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

        // Here we are setting what happens when one of the items in the recycler view is clicked
        adapter.setOnSongClickListener(new SongAdapter.OnSongListener() {
            @Override
            public void onSongClick(DocumentSnapshot documentSnapshot, int position) {
                // In this method we made it so that when an item is clicked it will go to the ContentPlayerActivity and will take with it
                // the song name and its position
                // I used this video to make the items in the recycler view clickable https://www.youtube.com/watch?v=3WR4QAiVuCw&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=6&ab_channel=CodinginFlowCodinginFlowVerified
                SongsModel song = documentSnapshot.toObject(SongsModel.class);
                String name = song.getName();
                startActivity(new Intent(getApplicationContext(), ContentPlayerActivity.class)
                        .putExtra("songname", name)
                        .putExtra("pos", position));
            }
        });
    }


    // This starts listening for database changes, when the app is in the foreground
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // This stops listening and will stop updating the database, when the app goes into the background in order to save resources
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}