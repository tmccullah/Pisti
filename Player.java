/*

   Class: CS 1302/02
   Term: Summer 2017
   Name: Tori McCullah
   Instructor: Tejaswini NALAMOTHU
   
   
                 ********  FINAL PROJECT: PISTI   ***********
   
*/


import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public abstract class Player {

   public ArrayList<String> hand = new ArrayList<>();
   public ArrayList<ImageView> handImages = new ArrayList<>();
   public ArrayList<String> collected = new ArrayList<>();
   public ArrayList<ImageView> collectedImages = new ArrayList<>();
   public ScoreCard score = new ScoreCard();
   public final String JACK = "J.png";
   // add array list of card backs from collected and hand later
   
   public Player() {
   }
   
   public ArrayList<String> getHand() {
      return hand;
   }
   
   public ArrayList<ImageView> getHandImages() {
      return handImages;
   }
   
   public ArrayList<String> getCollected() {
      return collected;
   }   
      
   public ArrayList<ImageView> getCollectedImages() {
      return collectedImages;
   }
   
   public ScoreCard getScore() {
      return score;
   }   
   
   public void addToHand(String card) {
      hand.add(card);
   }
   
   public void addToHandImages(ImageView card) {
      handImages.add(card);
   }
   
   public void addToCollected(String card) {
      collected.add(card);
   }
   
   public void addToCollectedImages(ImageView card) {
      collectedImages.add(card);
   }

   public String getACard(int i) {
      if (hand.size() > i)
         return getHand().get(i);
      else
         return null;
   }
   
   public ImageView getACardImage(int i) {
      if (handImages.size() > i)
         return getHandImages().get(i);
      else
         return null;
   }
   
   public String getCollectedCard(int i) {
      if (collected.size() > i)
         return getCollected().get(i);
      else
         return null;
   }
   
   public ImageView getCollectedCardImage(int i) {
      if (collectedImages.size() > i)
         return getCollectedImages().get(i);
      else
         return null;
   }
      
   public abstract boolean playedAJack(String card);
   public abstract boolean madeAMatch(String card1, String card2);
   
   // public abstract int addScore(); ??? maybe
   
   public void removeCard(int i) {
      hand.remove(i);
   }
   
   public void has2Clubs() {
      for (String s: getCollected())
         if (s.contains("Cards/Clubs/2.png"))
            score.club2();
   } // add to score if collected has 2 of clubs
   
   public void has10Diamonds() {
      for (String s: getCollected())
         if (s.contains("Cards/Diamonds/10.png"))
            score.diamond10();
   } // add to score if collected has 10 of diamonds
   
   public void otherPoints() {
      for (String s: getCollected()) {
         if (s.contains(JACK))
            score.faceAceOr10();
         else if (s.contains("Q.png"))
            score.faceAceOr10();
         else if (s.contains("K.png"))
            score.faceAceOr10();
         else if (s.contains("A.png"))
            score.faceAceOr10();
         else if (s.contains("10.png"))
            score.faceAceOr10();
      }
   } // add to score if a face card, ace, or 10 are in collected
   
   public void compareSize(Player p1, Player p2) {
      int p1Size = p1.getCollected().size();
      int p2Size = p2.getCollected().size();
      if (p1Size > p2Size)
         p1.score.moreCards();
      else if (p2Size > p1Size)
         p2.score.moreCards();
   } // compare size of collected array lists in two player objects
   // does nothing if they are same size
   
   public void removeBlanks() {
      getCollected().remove("BLANK");
   }
}