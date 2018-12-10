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


public class CardDeck {
   
   private ArrayList<String> deck = new ArrayList<>();
   private ArrayList<ImageView> backOfCards = new ArrayList<>();
   private ArrayList<ImageView> deckImages = new ArrayList<>();
   private final String JACK = "J.png";
   private static final String BACK = "Cards/Back.jpg";
   private static final int WIDTH = 75, HEIGHT = 125;
   public int deck_size = 52; // constant for outside access

   public CardDeck() {
      addCards(deck);
      addCardBacks(backOfCards);
      shuffleDeck(); // invoke shuffling of deck
   } // invoke file names of cards to be written and all images of card backs
   
   public ArrayList<String> getDeck() {
      return deck;
   } // string of names of cards in deck
   
   public ArrayList<ImageView> getDeckImages() {
      return deckImages;
   } // images of cards in deck
   
   public ArrayList<ImageView> getBackOfCards() {
      return backOfCards;
   } // back of card (like the logo of the card)
   
   public void drawNext(ArrayList<ImageView> toImages, ArrayList<String> toList) {
      // just remove backs for now
      if (hasCards()) {
         addCardToIV(deckImages, toImages);
         addCardToS(deck, toList);
         backOfCards.remove(0);
         deck_size--;
      }
   }
   
   public boolean hasCards() {
      if (deck.isEmpty())
         return false;
      else
         return true;
   } 
   
   private void shuffleDeck() {
      Collections.shuffle(deck);
      String s = deck.get(3);
      while (s.contains(JACK)) {
         Collections.shuffle(deck);
         s = deck.get(3);
      }
      addDeckImages(deckImages, deck);
   } 
   /* add images only after deck gets shuffled
      that way, if cardImages is empty, we know the deck has not been shuffled
      also, it keeps shuffling until a jack is not the first card
      meaning the first card will be the card turned over in the middle pile */
   
   private static ArrayList<String> addCards(ArrayList<String> list) {
   
      String cardName = "", suit = "";
      
      for (int j = 0; j < 4; j++) {
      
         if (j == 0) 
            suit = "Cards/Clubs/";
         else if (j == 1) 
            suit = "Cards/Diamonds/";
         else if (j == 2) 
            suit = "Cards/Hearts/";
         else 
            suit = "Cards/Spades/";
            
         // outter loop will initiate name of the suit of the card
            
         for (int i = 2; i < 15; i++) { 
         // start at 2 so it will corespond to card value
         
            if (i < 11)
               cardName = suit + i + ".png"; // i.e. numbers 2-10
            else if (i == 11)
               cardName = suit + "J.png";
            else if (i == 12)
               cardName = suit + "Q.png";
            else if (i == 13)
               cardName = suit + "K.png";
            else
               cardName = suit + "A.png";
            
            // inner loop will initiate the value of the card
            // add suit with card value to get the name of the card
            // this will be the file name the card is saved under
               
            list.add(cardName); // add card name to the list
         }
      }
      return list; // list now is a string of the file names of the cards
   }
   
   
   private static ArrayList<ImageView> addDeckImages
      (ArrayList<ImageView> ivList, ArrayList<String> fileNames) {
      
      try {
         for (int i = 0; i < fileNames.size(); i++)
            ivList.add(imageSize(fileNames.get(i)));
         return ivList;
      }
            
      catch(Exception ex) {
         System.out.print("Image files not saved under correct directory or "
            + "\nupdate file names in addCards method from Deck class.");
         return ivList;
      }
   }

   private static ArrayList<ImageView> addCardBacks(ArrayList<ImageView> ivList) {
      
      final int DECKSIZE = 52;
      try {
         for (int i = 0; i < DECKSIZE; i++)
            ivList.add(imageSize(BACK));
         return ivList;
      }
            
      catch(Exception ex) {
         System.out.print("Image files not saved under correct directory or "
            + "\nupdate file names in addCards method from Deck class.");
         return ivList;
      }
   }
   
   private static ImageView imageSize(String image) {
      ImageView iv = new ImageView(new Image(image));
      iv.setFitWidth(WIDTH);
      iv.setFitHeight(HEIGHT);  
      return iv;
   }
   
   
   private static void addCardToS(ArrayList<String> addFromList, 
      ArrayList<String> addToList) {
      
      addToList.add(addFromList.get(0)); // add to
      addFromList.remove(0); // remove from
   } 
   
   private static void addCardToIV(ArrayList<ImageView> addFromList, 
      ArrayList<ImageView> addToList) {
      
      addToList.add(addFromList.get(0)); // add to
      addFromList.remove(0); // remove from
   }
   
   public ImageView getCardBackImage() {
      return new ImageView(BACK);
   }
   
}