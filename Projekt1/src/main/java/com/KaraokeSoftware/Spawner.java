package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Spawner extends AbstractBehavior<Spawner.Message> {

    public interface Message {};

    public record ExampleMessage(ActorRef<AkkaMainSystem.Create> someReference, String someString) implements Message {  }

    public static Behavior<Message> create(String name) {
        return Behaviors.setup(context -> new Spawner(context, name));
    }

    private final String name;

    private Spawner(ActorContext<Message> context, String name) {
        super(context);
        this.name = name;
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(ExampleMessage.class, this::onExampleMessage)
                .build();
    }

    private Behavior<Message> onExampleMessage(ExampleMessage msg) {
        getContext().getLog().info("I ({}) got a message: ExampleMessage({},{})", this.name, msg.someReference, msg.someString);
        return this;
    }
}
