/*

   Class: CS 1302/02
   Term: Summer 2017
   Name: Tori McCullah
   Instructor: Tejaswini NALAMOTHU
   
   
                 ********  FINAL PROJECT: PISTI   ***********
   
*/

import java.util.ArrayList;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.PathTransition;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class InteractivePlayer extends Player {
   
   public InteractivePlayer() {
      super();
   }
   
   @Override
   public boolean playedAJack(String card) {
      return super.score.hasJack(card);
   }
   
   @Override
   public boolean madeAMatch(String playedCard, String tableCard) {
      return super.score.hasMatch(playedCard, tableCard);
   }
   
}