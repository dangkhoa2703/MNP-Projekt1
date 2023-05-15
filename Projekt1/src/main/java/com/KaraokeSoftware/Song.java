package com.KaraokeSoftware;

/**
 * a song consist of titel and durartion( in second )
 */
public class Song {

    private final String artist;
    private final String title;
    private final int duration;

    public Song(String artist, String title, int duration){
        this.artist = artist;
        this.title = title;
        this.duration = duration;
    }

    public String getArtist(){ return artist;}
    public String getTitle(){ return title;}
    public int getDuration(){return duration;}
}
