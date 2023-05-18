/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
 */

package com.KaraokeSoftware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 *  Manage a list of song, can return a list of artists name and their song
 */
public class SongsManager {

    private final HashMap<String, ArrayList<Song>> artistsList = new HashMap<>();

    public SongsManager() {}

    public void addSong(String artistsName, String title, int duration) {
        Song newSong = new Song(artistsName, title, duration);
        if (!artistsList.containsKey(artistsName)){
            artistsList.put(artistsName, new ArrayList<>());
        }
        artistsList.get(artistsName).add(newSong);
    }

    public ArrayList<Song> getArtistsSongs(String artistsName){
        return artistsList.get(artistsName);
    }
    public Set<String> getArtists() {return artistsList.keySet();}
}
