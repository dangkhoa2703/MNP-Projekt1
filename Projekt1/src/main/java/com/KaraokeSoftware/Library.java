package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Library extends AbstractBehavior<Library.Message> {

    public interface Message {}

    public record ListArtistsMessage(ActorRef<KaraokeSinger.Message> singer) implements Message {  }

    public record GetSongsMessage(String songsName, ActorRef<KaraokeSinger.Message> singer) implements Message { }

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
                .build();
    }

    private Behavior<Message> onListArtistsMessage(ListArtistsMessage msg) {
        msg.singer.tell(new KaraokeSinger.ArtistListsMessage(songsManager.getArtists()));
        return this;
    }

//    private Behavior<Message> onGetSongsMessage(KaraokeSinger.GetSongsMessage msg){
//        return this;
//    }
}
