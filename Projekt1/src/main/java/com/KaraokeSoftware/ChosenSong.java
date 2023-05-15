package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;

public class ChosenSong {

    private final ActorRef<KaraokeSinger.Message> singer;
    private final Song song;

    public ChosenSong(Song song, ActorRef<KaraokeSinger.Message> singer){
        this.singer = singer;
        this.song = song;
    }

    public ActorRef<KaraokeSinger.Message> getSinger(){ return singer;}
    public Song getSong() { return song;}
}
