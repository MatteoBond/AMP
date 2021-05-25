package com.example.amp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button play, pause;
    TextView songDescription;
    int position;

    MediaPlayer mediaPlayer;


    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Here we have similar code to the content player but instead only has the two buttons and does not have the seek bar feature
        songDescription = view.findViewById(R.id.songDescription);

        Intent i = getActivity().getIntent();
        Bundle bundle = i.getExtras();
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        songDescription.setSelected(true);

        songDescription.setText(songName);

        view = inflater.inflate(R.layout.fragment_music, container, false);
        pause = (Button) view.findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null){
                    mediaPlayer.pause();
                }
            }
        });
        play = (Button) view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songDescription.equals("Mocking Bird")) {
                    if(mediaPlayer != null){
                    }
                    else{
                        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.mockingbird);
                    }
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }
                else if (songDescription.equals("Everything Black")) {
                    if(mediaPlayer == null){
                        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.everythingblack);
                    }
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                else {

                }
            }
        });
        songDescription = (TextView) view.findViewById(R.id.songDescription);
        // Inflate the layout for this fragment
        return view;
    }
}