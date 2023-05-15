package com.KaraokeSoftware;

/**
 * a song consist of titel and durartion( in second )
 */
public class Song {

    private final String artist;
    private final String titel;
    private final int duration;

    public Song(String artist, String titel, int duration){
        this.artist = artist;
        this.titel = titel;
        this.duration = duration;
    }

    public String getArtist(){ return artist;}
    public String getTitel(){ return titel;}
    public int getDuration(){return duration;}
}
