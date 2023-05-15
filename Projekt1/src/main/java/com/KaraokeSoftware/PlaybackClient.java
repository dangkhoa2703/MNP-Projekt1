package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;


public class PlaybackClient extends AbstractBehavior<PlaybackClient.Message> {

    public interface Message {};


    public record PlaySongMessage(Song song, ActorRef<KaraokeSinger.Message> singer) implements Message{}


    public static Behavior<Message> create() {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new PlaybackClient(context, timers)));
    }

    private final TimerScheduler<PlaybackClient.Message> timers;

    private PlaybackClient(ActorContext<Message> context, TimerScheduler<PlaybackClient.Message> timers) {
        super(context);
        this.timers = timers;

        Message msg = new ExampleMessage("test123");
        this.timers.startSingleTimer(msg, msg, Duration.ofSeconds(10));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlaySongMessage.class, this::onPlaySongMessage)
                .build();
    }

    private Behavior<Message> onPlaySongMessage(PlaySongMessage msg) {
        getContext().getLog().info("I have send myself this message after 10 Seconds: {}", msg.someString);
        return this;
    }
}
