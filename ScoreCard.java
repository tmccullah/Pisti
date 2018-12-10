/*

   Class: CS 1302/02
   Term: Summer 2017
   Name: Tori McCullah
   Instructor: Tejaswini NALAMOTHU
   
   
                 ********  FINAL PROJECT: PISTI   ***********
   
*/


//// could change to an interface, change some things around and edit player class to implement this one
/// however, I chose not to

import java.util.ArrayList;

public class ScoreCard {

   private int score = 0;
   
   public ScoreCard() {
   }
   
   public int getScore() {
      return score;
   }
   
   public void pisti() {
      score += 10;
   }
   
   public void doublePisti() {
      score += 20;
   }
   
   public void club2() {
      score += 2;
   }
   
   public void diamond10() {
      score += 3;
   }
   
   public void faceAceOr10() {
      score += 1;
   }
   
   public void moreCards() {
      score += 3;
   }
   
   public boolean hasMatch(String playedCard, String cardOnTable) {
      boolean answer = false;
      String matchS;
      final int MATCH_SIZE = 6;
      int cardLength = playedCard.length();
      
      if (cardLength > MATCH_SIZE) {
         matchS = playedCard.substring(cardLength - MATCH_SIZE);   
         if (cardOnTable.contains(matchS))
            answer = true;
      }
      return answer;
   }


   public boolean hasJack(String card) {
      final String JACK = "/J.png";
      boolean answer = false;
      
      if (card.contains(JACK))
         answer = true;
            
      return answer;
   }
   
}