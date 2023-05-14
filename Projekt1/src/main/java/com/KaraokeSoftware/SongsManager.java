package com.KaraokeSoftware;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {

    private HashMap<String, ArrayList<Song>> artistsList = new HashMap<String, ArrayList<Song>>();

    private SongsManager() {};

    public void addSong(String artistsName, String titel, int duration) {
        Song newSong = new Song(titel, duration);
        if (artistsList.containsKey(artistsName)){
            artistsList.put(artistsName, new ArrayList<Song>());
        }
        artistsList.get(artistsName).add(newSong);
    }

    public ArrayList<Song> getArtistsSongs(String artistsName){
        return artistsList.get(artistsName);
    }
}
