package com.example.amp;

public class SongsModel {

    private String Artist;
    private String Name;
    private long Duration;

    private SongsModel(){

    }

    private SongsModel(String Artist, String Name, long Duration){

        this.Artist = Artist;
        this.Name = Name;
        this.Duration = Duration;

    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(long duration) {
        Duration = duration;
    }


}
