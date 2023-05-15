package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.LinkedList;
import java.util.Queue;

public class QueueManager extends AbstractBehavior<QueueManager.Message> {

    public interface Message {};



    //---- Types of message, which this class received ----
    public record AddSongMessage(Song song, ActorRef<KaraokeSinger.Message> singer, ActorRef<PlaybackClient.Message> pbClient) implements Message{}

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

    private Behavior<Message> onAddSongMessage(AddSongMessage msg) {
        ActorRef<KaraokeSinger.Message> singer = msg.singer;
        Song song = msg.song;
        if(queue.size() == 0 && isReady){
            msg.pbClient.tell(new PlaybackClient.PlaySongMessage(song, singer));
            isReady = false;
        }else{
            queue.add(new ChosenSong(song, singer));
        }
        return this;
    }

    private Behavior<Message> onReadyMessage(ReadyMessage msg) {

        if(queue.size() == 0){
            this.getContext().getLog().info("No more song in queue!");
            isReady = true;
        }else{
            ChosenSong chosenSong = queue.getFirst();
            msg.pbClient.tell(new PlaybackClient.PlaySongMessage(chosenSong.getSong(),chosenSong.getSinger()));
            isReady = false;
        }
        return this;
    }
}
