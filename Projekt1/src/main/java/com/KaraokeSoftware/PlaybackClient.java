/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;


public class PlaybackClient extends AbstractBehavior<PlaybackClient.Message> {

    public interface Message {}

    //------  Messages, which PlaybackClient receive ---------

    // receive the next song in queue to play
    public record PlaySongMessage(Song song, ActorRef<KaraokeSinger.Message> singer) implements Message{}

    // notifying, that a song finished
    public record SongFinishedMessage() implements Message{}

    //------------------------------------------------------------------------

    public static Behavior<Message> create(ActorRef<QueueManager.Message> queMan) {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new PlaybackClient(context, timers, queMan)));
    }

    private final TimerScheduler<PlaybackClient.Message> timers;
    private final ActorRef<QueueManager.Message> queMan;

    private PlaybackClient(ActorContext<Message> context, TimerScheduler<PlaybackClient.Message> timers, ActorRef<QueueManager.Message> queMan) {
        super(context);
        this.timers = timers;
        this.queMan = queMan;
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlaySongMessage.class, this::onPlaySongMessage)
                .onMessage(SongFinishedMessage.class, this::onSongFinishedMessage)
                .build();
    }

    // when receive the next song from QueueManager,
    // send this song the KaraokeSinger, who order this song
    private Behavior<Message> onPlaySongMessage(PlaySongMessage msg) {
        int duration = msg.song.getDuration();
        msg.singer.tell(new KaraokeSinger.StartSingingMessage(msg.song));
        Message mess = new SongFinishedMessage();
        this.timers.startSingleTimer(mess, mess, Duration.ofSeconds(duration));
        return this;
    }

    // when notifying, that a song is finished,
    // ask QueueManager for the next song
    private Behavior<Message> onSongFinishedMessage(SongFinishedMessage msg) {
        getContext().getLog().info("Done");
        queMan.tell(new QueueManager.ReadyMessage(getContext().getSelf()));
        return this;
    }
}
