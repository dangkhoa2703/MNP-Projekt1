/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.LinkedList;

public class QueueManager extends AbstractBehavior<QueueManager.Message> {

    public interface Message {}



    //---- Types of message, which this class received ----

    // receive a song to add to queue
    public record AddSongMessage(Song song, ActorRef<KaraokeSinger.Message> singer, ActorRef<PlaybackClient.Message> pbClient) implements Message{}

    // notifying, that next song in queue should be sent
    public record ReadyMessage(ActorRef<PlaybackClient.Message> pbClient) implements Message{}

    //-------------------------------------------------------------------------------------


    public static Behavior<Message> create() {
        return Behaviors.setup(QueueManager::new);
    }


    private final LinkedList<ChosenSong> queue;
    private boolean isReady = true;

    private QueueManager(ActorContext<Message> context) {
        super(context);
        this.queue = new LinkedList<>();
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(AddSongMessage.class, this::onAddSongMessage)
                .onMessage(ReadyMessage.class, this::onReadyMessage)
                .build();
    }


    // when receive a song order, add it to queue
    // if queue is empty and no song is being play in PlaybackClient, forward this song to playbackClient
    private Behavior<Message> onAddSongMessage(AddSongMessage msg) {
        ActorRef<KaraokeSinger.Message> singer = msg.singer;
        Song song = msg.song;
        if(queue.size() == 0 && isReady){
            msg.pbClient.tell(new PlaybackClient.PlaySongMessage(song, singer));
            getContext().getLog().info("Next song: {} - {}",song.getArtist(),song.getTitle());
            isReady = false;
        }else{
            queue.add(new ChosenSong(song, singer));
            getContext().getLog().info("Added song: \"{} - {}\" to waiting list",song.getArtist(), song.getTitle());
        }
        return this;
    }

    // send next song in queue to PlaybackClient in order to play song
    private Behavior<Message> onReadyMessage(ReadyMessage msg) {

        if(queue.size() == 0){
            this.getContext().getLog().info("No more song in queue!");
            isReady = true;
        }else{
            ChosenSong chosenSong = queue.remove();
            msg.pbClient.tell(new PlaybackClient.PlaySongMessage(chosenSong.song(),chosenSong.singer()));
            getContext().getLog().info("Next song: {} - {}",chosenSong.song().getArtist(),chosenSong.song().getTitle());
            isReady = false;
        }
        return this;
    }
}
