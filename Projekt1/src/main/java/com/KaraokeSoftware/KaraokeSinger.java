package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Set;

public class KaraokeSinger extends AbstractBehavior<KaraokeSinger.Message> {

    public interface Message {};

    /*---- Messages, which are sent by this class ----*/
    public record ArtistListsMessage(Set<String> artists) implements Message{}

    /*----------------*/
    
    public static Behavior<Message> create(ActorRef<Library.Message> lib) {
        return Behaviors.setup(context -> new KaraokeSinger(context, lib));
    }

    private final ActorRef<Library.Message> lib;

    private KaraokeSinger(ActorContext<Message> context, ActorRef<Library.Message> lib) {
        super(context);
        this.lib = lib;
        lib.tell(new Library.ListArtistsMessage(getContext().getSelf()));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(lib.ListArtistsMessage.class, this::onExampleMessage)
                .build();
    }

    private Behavior<Message> onExampleMessage(ListArtistsMessageMessage msg) {
        getContext().getLog().info("I ({}) got a message: ExampleMessage({},{})", this.name, msg.someReference, msg.someString);
        return this;
    }
}
