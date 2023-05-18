/*
Projekt von:

Dang Khoa Nguyen (214267)
Lars Klichta (232078)
 */

package com.KaraokeSoftware;

import akka.actor.typed.ActorSystem;

import java.io.IOException;
public class AkkaStart {
  public static void main(String[] args) {

    SongsManager songsManager = new SongsManager();
    songsManager.addSong("Queen","We Will Rock You", 5);
    songsManager.addSong("Queen", "Bohemian Rhapsody", 11);
    songsManager.addSong("Queen", "Don't Stop Me Now", 8);
    songsManager.addSong("Bon Jovi","Living On A Prayer", 9);
    songsManager.addSong("Bon Jovi",  "It's My Life", 9);
    songsManager.addSong("Bon Jovi","You Give Love A Bad Name", 8);
    songsManager.addSong("Led Zeppelin","Stairway To Heaven", 15);
    songsManager.addSong("Led Zeppelin", "Black Dog",9);
    songsManager.addSong("Led Zeppelin","Immigrant Song",10);
    songsManager.addSong("Eagles","Hotel California", 13);
    songsManager.addSong("Nivana", "Smells Like Teen Spirit", 10);
    songsManager.addSong("Lynyrd Skynyrd", "Free Bird", 15);
    songsManager.addSong("AC/DC","Back In Black", 5);
    songsManager.addSong("AC/DC","Thunderstruck",7);
    songsManager.addSong("AC/DC","T.N.T",8);
    songsManager.addSong("Gun n' Roses", "Sweet Child o' Mine", 6);
    songsManager.addSong("Gun n' Roses","November Rain",15);
    songsManager.addSong("Gun n' Roses","Welcome To The Jungle",8);
    songsManager.addSong("Rainbow","Stargazer",15);
    songsManager.addSong("John Denver","Country Roads", 4);
    songsManager.addSong("Israel \"IZ\" Kamakawiwo", "Some Where Over The Rainbow",8 );
    songsManager.addSong("a-ha", "Take On Me", 9);
    songsManager.addSong("George Michael", "Careless whisper",10);

    final ActorSystem<AkkaMainSystem.Create> messageMain = ActorSystem.create(AkkaMainSystem.create(songsManager), "akkaMainSystem");

    messageMain.tell(new AkkaMainSystem.Create());

    try {
      System.out.println(">>> Press ENTER to exit <<<");
      System.in.read();
    } catch (IOException ignored) {
    } finally {
      messageMain.terminate();
    }
  }
}
