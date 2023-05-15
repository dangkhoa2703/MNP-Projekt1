package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;


public record ChosenSong(Song song, ActorRef<KaraokeSinger.Message> singer) { }
