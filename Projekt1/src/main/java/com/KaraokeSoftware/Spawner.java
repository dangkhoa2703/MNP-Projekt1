package com.KaraokeSoftware;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;


public class Spawner extends AbstractBehavior<Spawner.Message> {

    public interface Message {};


    public record CreateSingerMessage() implements Message {  }

    public static Behavior<Message> create() {
        return Behaviors.setup(context -> Behaviors.withTimers(timers -> new Spawner(context, timers)));
    }

    private final TimerScheduler<Spawner.Message> timers;

    private Spawner(ActorContext<Message> context, TimerScheduler<Spawner.Message> timers) {
        super(context);
        this.timers = timers;

        Message msg = new CreateSingerMessage();

        Random r = new Random();
        int randomNumber = r.nextInt(13);
        while (randomNumber < 2) {
            randomNumber = r.nextInt(13);
        }
        this.timers.startSingleTimer(msg, msg, Duration.ofSeconds(randomNumber));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(CreateSingerMessage.class, this::onCreateSingerMessage)
                .build();
    }

    private Behavior<Message> onCreateSingerMessage(CreateSingerMessage msg) {
        getContext().getLog().info("I have send myself this message after 10 Seconds: {}", msg.someString);
        return this;
    }
}
