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

public class Library extends AbstractBehavior<Library.Message> {

    public interface Message {}

    //--- Messages, which Library receive -----

    // ask Library to send back a list of all artists
    public record ListArtistsMessage(ActorRef<KaraokeSinger.Message> singer) implements Message {  }

    // ask Library to send back a list of all songs of an artist
    public record GetSongsMessage(String artistsName, ActorRef<KaraokeSinger.Message> singer) implements Message { }

    //------------------------------------------------------------


    public static Behavior<Message> create(SongsManager songsManager) {
        return Behaviors.setup(context -> new Library(context, songsManager));
    }

    private final SongsManager songsManager;


    private Library(ActorContext<Message> context, SongsManager songsManager) {
        super(context);
        this.songsManager = songsManager;
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(ListArtistsMessage.class, this::onListArtistsMessage)
                .onMessage(GetSongsMessage.class, this::onGetSongsMessage)
                .build();
    }

    // send a KaraokeSinger a list of all artist
    private Behavior<Message> onListArtistsMessage(ListArtistsMessage msg) {
        msg.singer.tell(new KaraokeSinger.ArtistsListsMessage(songsManager.getArtists()));
        return this;
    }

    // send a KaraokeSinger a list of all songs of an artist
    private Behavior<Message> onGetSongsMessage(GetSongsMessage msg){
        msg.singer.tell(new KaraokeSinger.SongsListMessage(songsManager.getArtistsSongs(msg.artistsName)));
        return this;
    }
}
