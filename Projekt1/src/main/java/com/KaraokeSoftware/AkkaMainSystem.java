/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class AkkaMainSystem extends AbstractBehavior<AkkaMainSystem.Create> {

    public static class Create {}

    public static Behavior<Create> create(SongsManager songsManager) {

        return Behaviors.setup(context -> new AkkaMainSystem(context, songsManager));
    }

    private final SongsManager songsManager;

    private AkkaMainSystem(ActorContext<Create> context, SongsManager songsManager) {
        super(context);
        this.songsManager = songsManager;
    }

    @Override
    public Receive<Create> createReceive() {
        return newReceiveBuilder().onMessage(Create.class, this::onCreate).build();
    }

    private Behavior<Create> onCreate(Create command) {
        //#create-actors
        ActorRef<Library.Message> lib = this.getContext().spawnAnonymous(Library.create(songsManager));
        ActorRef<QueueManager.Message> queMan = this.getContext().spawnAnonymous(QueueManager.create());
        ActorRef<PlaybackClient.Message> pbClient = this.getContext().spawnAnonymous(PlaybackClient.create(queMan));
        ActorRef<Spawner.Message> spawner = this.getContext().spawnAnonymous(Spawner.create(lib,queMan,pbClient));
        //#create-actors

        spawner.tell(new Spawner.CreateSingerMessage());
        return this;
    }
}
