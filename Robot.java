/*

   Class: CS 1302/02
   Term: Summer 2017
   Name: Tori McCullah
   Instructor: Tejaswini NALAMOTHU
   
   
                 ********  FINAL PROJECT: PISTI   ***********
   
*/

import java.util.ArrayList;
import java.util.Collections;
import java.math.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Robot extends Player {
   private int round = 0;
   
   public Robot() {
      super();
   }
   
   public String playCard(String cardOnTable) {
      String play = "";
      
      for (int i = 0; i < super.hand.size(); i++) {
         if (matchIndex(super.getACard(i), cardOnTable) >= 0) {
            play = "" + super.getACard(i);
            break;
         }
         else if (jackIndex(super.getACard(i)) >= 0 ) {
            if (round == 0 & super.getHand().size() > 1)
               play = "" + super.getACard(i + 1);
            else if (round != 0 && cardOnTable.contains("BLANK") && 
               super.getHand().size() > 1)
               play = "" + super.getACard(i + 1);
            else
               play = "" + super.getACard(i);
            break;
         }
         else {
            play = "" + super.getACard(i);
         }
      }
      round++;
      return play;
   }
   
   public int matchIndex(String card, String tableCard) {
      int match = -1;
      if (super.score.hasMatch(card, tableCard))
         match = super.hand.indexOf(card);
      return match;
   }
   
   public int jackIndex(String card) {
      int j = -1;
      if (super.score.hasJack(card))
         j = super.hand.indexOf(card);
      return j;
   }
   
   @Override
   public boolean playedAJack(String card) {
      if (jackIndex(card) >= 0)
         return true;
      else
         return false;
   }
   
   @Override
   public boolean madeAMatch(String card, String tableCard) {
      if (matchIndex(card, tableCard) >= 0 && jackIndex(card) >= 0)
         return true;
      else if (matchIndex(card, tableCard) >= 0)
         return true;
      else
         return false;
   }

}