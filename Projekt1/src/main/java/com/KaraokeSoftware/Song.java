/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
Tran Long Huynh (236582)
 */

package com.KaraokeSoftware;

/**
 * a song consist of titel and durartion( in second )
 */
public record Song(String artist, String title, int duration) {

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }
}
