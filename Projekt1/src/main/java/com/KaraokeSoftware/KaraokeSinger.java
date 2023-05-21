/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
Tran Long Huynh (236582)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class KaraokeSinger extends AbstractBehavior<KaraokeSinger.Message> {

    public interface Message {}

    /*---- Messages, which this class receive ----*/

    // receive a list of all artist
    public record ArtistsListsMessage(Set<String> artists) implements Message{}

    // receive a list of all song of an artist
    public record SongsListMessage(ArrayList<Song> songs) implements Message{}

    // notifying message
    public record StartSingingMessage(Song song) implements Message{}

    /*----------------*/
    
    public static Behavior<Message> create(ActorRef<Library.Message> lib , ActorRef<QueueManager.Message> queMan, ActorRef<PlaybackClient.Message> pbClient) {
        return Behaviors.setup(context -> new KaraokeSinger(context, lib, queMan, pbClient));
    }

    private final ActorRef<Library.Message> lib;
    private final ActorRef<QueueManager.Message> queMan;
    private final ActorRef<PlaybackClient.Message> pbClient;

    private KaraokeSinger(ActorContext<Message> context, ActorRef<Library.Message> lib, ActorRef<QueueManager.Message> queMan, ActorRef<PlaybackClient.Message> pbClient) {
        super(context);
        this.lib = lib;
        this.queMan = queMan;
        this.pbClient = pbClient;
        lib.tell(new Library.ListArtistsMessage(getContext().getSelf()));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(ArtistsListsMessage.class, this::onArtistsListMessage)
                .onMessage(SongsListMessage.class, this::onSongsListMessage)
                .onMessage(StartSingingMessage.class, this:: onStartSingingMessage)
                .build();
    }

    // when receive a list of artist, chose a random artist and ask library a list of all song of this artist
    private Behavior<Message> onArtistsListMessage(ArtistsListsMessage msg) {
        Random random = new Random();
        String[] artists = msg.artists.toArray(new String[0]);
        String randomArtist = artists[random.nextInt(artists.length)];
        lib.tell(new Library.GetSongsMessage(randomArtist, getContext().getSelf()));
        return this;
    }

    // first KaraokeSinger ask QueueManager whether the queue is empty. If queue is empty => send song to PlaybackClient
    // If queue not empty, send song to QueueManager
    private Behavior<Message> onSongsListMessage(SongsListMessage msg) {
        Random r = new Random();
        Song song = msg.songs.get(r.nextInt(msg.songs.size()));
        queMan.tell(new QueueManager.AddSongMessage(song,this.getContext().getSelf(),pbClient));
        return this;
    }



    private Behavior<Message> onStartSingingMessage(StartSingingMessage msg){
        String artist = msg.song.getArtist();
        String title = msg.song.getTitle();
        int duration = msg.song.getDuration();
        this.getContext().getLog().info("Start singing: {} - {} for {} second", artist,title,duration);
        return this;
    }
}
