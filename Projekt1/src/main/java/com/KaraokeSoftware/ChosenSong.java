/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
Minh Hieu Le (222117)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorRef;


public record ChosenSong(Song song, ActorRef<KaraokeSinger.Message> singer) { }
