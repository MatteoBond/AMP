package com.example.amp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

public class SongAdapter extends FirestoreRecyclerAdapter <SongsModel, SongAdapter.SongHolder> {

    // The Adapter basically gets data from our datasource into the recycler view
    // Since this is a FireStoreRecyclerAdapter then not only do we need to pass the class name , SongHolder, but we also have to pass the
    // model class, SongsModel.

    // I used this video to aid me with this adapter class https://www.youtube.com/watch?v=lAGI6jGS4vs&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=4&ab_channel=CodinginFlow


    private OnSongListener listener;

    public SongAdapter(@NonNull @NotNull FirestoreRecyclerOptions<SongsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull SongAdapter.SongHolder holder, int position, @NonNull @NotNull SongsModel model) {
        // SongsModel model in this method helps us because we don't have to keep our own dataset like in a normal adapter
        // Here we will tell the adapter what we want to put in each view of our list_song.xml file (which is the design of the recycler view)
        holder.list_artist.setText(model.getArtist());
        holder.list_name.setText(model.getName());
        holder.list_duration.setText(String.valueOf(model.getDuration())); // Since we cannot have a number as a setText we used String.value of to change the value into a string
    }

    @NonNull
    @NotNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Here we tell the adapter which layout it has to inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song, parent, false);
        return new SongHolder(view); // This is then passed to the constructor of the SongHolder
    }

    class SongHolder extends RecyclerView.ViewHolder{ //This is a viewholder

        //Variables that we have in our list_song.xml layout
        TextView list_artist;
        TextView list_name;
        TextView list_duration;

        public SongHolder(View itemView){
            super(itemView);

            //This is where we assign the variables to the viewHolder
            list_artist = itemView.findViewById(R.id.list_artist);
            list_name = itemView.findViewById(R.id.list_name);
            list_duration = itemView.findViewById(R.id.list_duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onSongClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }

    public interface OnSongListener{
        void onSongClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnSongClickListener(OnSongListener listener){
        this.listener = listener;
    }
}
