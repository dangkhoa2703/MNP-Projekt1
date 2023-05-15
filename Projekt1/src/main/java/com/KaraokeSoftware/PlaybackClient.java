package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;


public class PlaybackClient extends AbstractBehavior<PlaybackClient.Message> {

    public interface Message {};

    public record PlaySongMessage(Song song, ActorRef<KaraokeSinger.Message> singer) implements Message{}

    public record SongFinishedMessage() implements Message{}

    public static Behavior<Message> create(ActorRef<QueueManager.Message> queMan) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new PlaybackClient(context, timers, queMan)));
    }

    private final TimerScheduler<PlaybackClient.Message> timers;
    private final ActorRef<QueueManager.Message> queMan;
    private int duration;

    private PlaybackClient(ActorContext<Message> context, TimerScheduler<PlaybackClient.Message> timers, ActorRef<QueueManager.Message> queMan) {
        super(context);
        this.timers = timers;
        this.queMan = queMan;


    }

    private void playSong(){
        Message msg = new SongFinishedMessage();
        this.timers.startSingleTimer(msg, msg, Duration.ofSeconds(duration));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlaySongMessage.class, this::onPlaySongMessage)
                .onMessage(SongFinishedMessage.class, this::onSongFinishedMessage)
                .build();
    }

    private Behavior<Message> onPlaySongMessage(PlaySongMessage msg) {
        this.duration = msg.song.getDuration();
        playSong();
        return this;
    }

    private Behavior<Message> onSongFinishedMessage(SongFinishedMessage msg) {
        queMan.tell(new QueueManager.ReadyMessage(getContext().getSelf()));

        return this;
    }
}
