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
import akka.actor.typed.javadsl.*;

import java.time.Duration;
import java.util.Random;


public class Spawner extends AbstractBehavior<Spawner.Message> {

    public interface Message {}
    //----- Message, which Spawner receive

    public record CreateSingerMessage() implements Message {  }

    //-------------

    public static Behavior<Message> create( ActorRef<Library.Message> lib, ActorRef<QueueManager.Message> queMan, ActorRef<PlaybackClient.Message> pbClient ) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new Spawner(context, timers, lib, queMan, pbClient )));
    }

    private final TimerScheduler<Spawner.Message> timers;
    private final ActorRef<Library.Message> lib;
    private final ActorRef<QueueManager.Message> queMan;
    private final ActorRef<PlaybackClient.Message> pbClient;

    private Spawner(ActorContext<Message> context, TimerScheduler<Spawner.Message> timers, ActorRef<Library.Message> lib, ActorRef<QueueManager.Message> queMan,  ActorRef<PlaybackClient.Message> pbClient ) {
        super(context);
        this.timers = timers;
        this.lib =  lib;
        this.queMan = queMan;
        this.pbClient = pbClient;
        createSinger();
    }

    // send a message to self after 2-12 second
    private void createSinger(){
        Message msg = new CreateSingerMessage();
        Random r = new Random();
        int randomNumber = r.nextInt(2,13);
        this.timers.startSingleTimer(msg, msg, Duration.ofSeconds(randomNumber));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(CreateSingerMessage.class, this::onCreateSingerMessage)
                .build();
    }

    // After receive a message from self, Spawner will spawn a KaraokeSinger
    // and wait for another 2-12 to send a create-singer message to self
    private Behavior<Message> onCreateSingerMessage(CreateSingerMessage msg) {
        this.getContext().spawnAnonymous(KaraokeSinger.create(lib, queMan, pbClient));
        createSinger();
        return this;
    }
}
