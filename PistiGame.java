/*

   Class: CS 1302/02
   Term: Summer 2017
   Name: Tori McCullah
   Instructor: Tejaswini NALAMOTHU
   
   
                 ********  FINAL PROJECT: PISTI   ***********
   
*/


import javafx.animation.PathTransition;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;

import javafx.application.Application;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.util.Duration;
import java.util.ArrayList;


public class PistiGame extends Application {

   private BorderPane pane = new BorderPane();
   private Button startButton = new Button("START");
   private Button dealButton = new Button("DEAL");
   private Button finalButton = new Button("FINAL ROUND!");
   private Button resultButton = new Button("RESULT");
   private CardDeck deck = new CardDeck();
   private InteractivePlayer player = new InteractivePlayer();
   private Robot robot = new Robot();
   private ArrayList<String> middlePile = new ArrayList<>();
   private ArrayList<ImageView> middlePileImages = new ArrayList<>();
   
   private final double X1 = 45, Y1 = 350, X2 = 550, Y_R = 100, Y_P = 600;
   private final double Y_M = Y1 - 62.5, X_M = X2 + 12.5; 
   // subtract half the card height bc fade in uses center of image (125/2)
   // add 50 (turn over card placed here) subtract half the card width (75/2)
   private int count = 10; // will determine how much to offset cards by
   private int roundNumber = 1; // number of round represented
   private int turnNumber = 0; // turn in round represented
   private String lastToMatch;
   
   @Override
   public void start(Stage stage) {

      Scene scene = new Scene(pane, 1200, 700);
      pane.setStyle("-fx-background-color: green;");
      startButton.setAlignment(Pos.CENTER);
      pane.setCenter(startButton);
      
      for (int i = 0; i < 4; i++)
         deck.drawNext(middlePileImages, middlePile);
         // remove card from deck store to middle pile
      deal();
         
      pane.getChildren().add(deck.getBackOfCards().get(0)); 
      // add card back to represent deck and specified location below
      deck.getBackOfCards().get(0).setX(X1 - 37.5);
      deck.getBackOfCards().get(0).setY(Y_M);

      
      startButton.setOnAction(e -> {     
         pane.getChildren().remove(startButton); // remove start button
         startST(); // initialize startST method (sequential transition)
         round1(); // invoke round1() method
         dealButton.setAlignment(Pos.BOTTOM_LEFT); // align "deal" button
         pane.setBottom(dealButton); // set button at bottom of pane
         
      });
      
      dealButton.setOnAction(e -> {
         newRound(); // initiate newRound() method
         deal(); // invoke deal() method
         dealAnimate(); // invoke method to animate the deal
         round1(); // invoke the round play
         roundNumber++; // increase roundNumber
         if (roundNumber == 5) { // roundNumber is use to to "stop" deals
            pane.getChildren().remove(dealButton); // button is removed
            finalButton.setAlignment(Pos.BOTTOM_LEFT); 
            pane.setBottom(finalButton); 
            // new button replaces it to initiate final round
            roundNumber++;
         }
      }); 
      
      finalButton.setOnAction(e -> {
         pane.getChildren().remove(finalButton); 
         // remove final round button so user cant over-deal cards that aren't there
         // in deck and to prevent error
         newRound(); // invoke newRound()
         deal(); // invoke deal()
         //dealAnimate(); // invoke animation of deal
         finalRound();
         resultButton.setAlignment(Pos.BOTTOM_RIGHT);
         pane.setBottom(resultButton);
      });
      
      resultButton.setOnAction(e -> {
         pane.getChildren().remove(resultButton);
         lastMoves();
      });
         

      stage.setScene(scene); // set scene in stage
      stage.setTitle("Pisti"); // set title for window
      stage.show(); // display GUI
   }
   
   public static void main(String[] args) {
      Application.launch(); // launch program
   }
      
   public PathTransition ptLine(ImageView iv, double seconds, double startX, 
      double startY, double endX, double endY) {
      
      pane.getChildren().add(iv);
      PathTransition path = new PathTransition(Duration.millis(seconds * 1000),
         new Line(startX, startY, endX, endY), iv);
      return path;
   } // path transition to reduce reduncancy and amount of lines using seconds
   
   public FadeTransition fadeOut(ImageView iv, double seconds) {
      FadeTransition ft = new FadeTransition(Duration.millis(seconds * 1000), iv);
      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      return ft;
   } // fadeOut method to fade out an image
   
   public FadeTransition fadeIn(ImageView iv, double seconds, double x, double y) {
      FadeTransition ft = new FadeTransition(Duration.millis(seconds * 1000), iv);
      pane.getChildren().add(iv);
      iv.setX(x);
      iv.setY(y);
      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      return ft;
   } // fadeIn method to fade in image of card at specified points
   
   public void startST() {
      final double T = 1; // time
      SequentialTransition st = new SequentialTransition();
      // transition will deal cards to player/robot then to middle
      st.getChildren().addAll(
         ptLine(player.getACardImage(0), T, X1, Y1, X2, Y_P),
         ptLine(robot.getACardImage(0), T, X1, Y1, X2, Y_R),
         ptLine(player.getACardImage(1), T, X1, Y1, X2 + 50, Y_P),
         ptLine(robot.getACardImage(1), T, X1, Y1, X2 + 50, Y_R),
         ptLine(player.getACardImage(2), T, X1, Y1, X2 + 100, Y_P),
         ptLine(robot.getACardImage(2), T, X1, Y1, X2 + 100, Y_R),
         ptLine(player.getACardImage(3), T, X1, Y1, X2 + 150, Y_P),
         ptLine(robot.getACardImage(3), T, X1, Y1, X2 + 150, Y_R),
         ptLine(middlePileImages.get(0), T, X1, Y1, X2, Y1),
         ptLine(middlePileImages.get(1), T, X1, Y1, X2, Y1),
         ptLine(middlePileImages.get(2), T, X1, Y1, X2, Y1),
         ptLine(middlePileImages.get(3), T, X1, Y1, X2 + 50, Y1), 
         fadeIn(deck.getBackOfCards().get(1), 0.1, X_M, Y_M));
      st.play();
         // will need to switch fadeIn, just done to understand how I will
         // implement animation will switch back of cards and revealed cards
         // will keep cards revealed until bot preforms correctly
         // and will need to remove old node after fadeIn
      System.out.println(middlePile); 
      // to view whats in middle pile to check my work
   }
   
   public void round1() {
   
      String s0 = player.getACard(0);
      String s1 = player.getACard(1);
      String s2 = player.getACard(2);
      String s3 = player.getACard(3);
      
      player.getACardImage(0).setOnMouseClicked(me -> {
         makeAMove(s0, 0); 
         // if card 0 in users hand is clicked makeAMove method invoked on set card
      });
      player.getACardImage(1).setOnMouseClicked(me -> {
         makeAMove(s1, 1);
      }); // action for card 1 being clicked on
      player.getACardImage(2).setOnMouseClicked(me -> {
         makeAMove(s2, 2);  
      }); // action for card 2
      player.getACardImage(3).setOnMouseClicked(me -> {
         makeAMove(s3, 3);
      }); // action for card 3
   }
   
   public ImageView robotPlay() {
      String s = "";
      ImageView iv = new ImageView();
      s = robot.playCard(middlePile.get(middlePile.size() - 1));
      // invoke robot's playCard method to decide what card to select
      iv = robot.getACardImage(robot.hand.indexOf(s));
      pane.getChildren().remove(iv); // remove previous image from robot hand
      return iv; // returns image of card that will be played
   }
   
  
   public void makeAMove(String playersCard, int i) {
   
      boolean playerResult = roundResult(player, playersCard, "Player");
      // result of round (did the player win or nothing happen)
      
      player.removeCard(player.hand.indexOf(playersCard));
      middlePile.add(playersCard); // add whichever players card to pile
      middlePileImages.add(player.getACardImage(i));
      // add card and its imageview to middle pile
      
      if (playerResult)
         middlePile.add("BLANK");
      final int r = robot.handImages.indexOf(robotPlay()); 
      // find index of robot's card that will be played
      pane.getChildren().remove(player.getACardImage(i));

      boolean robotResult = roundResult(robot, robot.getACard(r), "Robot");
      // result of round (did the robot win or did nothing happen
         
      sqPlay(playerResult, robotResult, i, r); // invoke sqPlay method
      
      middlePile.add(robot.getACard(r)); // add robot's card to pile
      middlePileImages.add(robot.getACardImage(r));   
      robot.removeCard(r); // remove card from robots hand
      robot.handImages.remove(r);
      // add robot card and imageview to middle pile 
      turnNumber += 1;  // add turnNumber to show a turn was processed
   }
   
   
   public void roundWinner(Player p) { // method for winner of the round
      for (int i = 0; i < middlePile.size(); i++)
         p.addToCollected(middlePile.get(i)); // adds card to its collected pile
      for (int i = 0; i < middlePileImages.size(); i++)
         p.addToCollectedImages(middlePileImages.get(i)); // adds image to collected
      middlePile.clear(); // clear middle pile
      middlePileImages.clear(); // clear images
      middlePile.add("BLANK"); // add blank (used to prevent comparing to "nothing"
      // and elim indexOutOfBounds from occuring
      middlePileImages.add(deck.getBackOfCards().get(deck.deck_size - 1));
      System.out.println(middlePile + "\n" + middlePileImages);
      // add these two so that middle piles arent empty and no errors occur
      // when cards are laid down on empty table
   }
      
   public void newRound() {
      player.hand.clear(); // new round means clear players hand
      robot.hand.clear(); // clear robots hand
      player.handImages.clear(); // clear players images in hand
      robot.handImages.clear(); // clear robots images in hand
      turnNumber = 0; // clear turns since new round is starting
   }
   
   public boolean roundResult(Player p, String card, String name) {
      boolean answer = false; // initialize result
      if (p.playedAJack(card) && 
            p.madeAMatch(card, middlePile.get(middlePileImages.size() - 1))) {
         p.score.doublePisti();
         System.out.println(name + " plays Jack - double Pisti! SCORE: " 
            + p.score.getScore());
         answer = true; // if player played a jack and it was a match
         
      }
      else if (p.playedAJack(card)) { // if player plays a jack
         if (roundNumber == 1 && turnNumber != 0) {
            p.score.pisti();
            System.out.println(name + " plays Jack - Pisti! SCORE: " 
               + p.score.getScore() + "\n\tCollected: " + p.getCollected() 
               + " & in Hand: " + p.getHand());
            answer = true;
         }
         else if (roundNumber != 1)  {
            p.score.pisti();
            System.out.println(name + " plays Jack - Pisti! SCORE: " 
               + p.score.getScore() + "\n\tCollected: " + p.getCollected() 
               + " & in Hand: " + p.getHand());
            answer = true;
         }
         else { // if first round on first turn, pisti cant be won
            System.out.println(name + " made a match! SCORE: " + p.score.getScore() 
               + "\n\tCollected: " + p.getCollected() + " & in Hand: " + p.getHand());
            answer = false;
         }
      }
      else if (p.madeAMatch(card, middlePile.get(middlePileImages.size() - 1))) { 
         System.out.println(name + " made a match! SCORE: " + p.score.getScore() 
            + "\n\tCollected: " + p.getCollected() + " & in Hand: " + p.getHand());
         answer = true;
      } // did a player make a match
      else {
         System.out.println(name + " plays " + card.substring(5, card.length() - 4)
            + " SCORE: " + p.score.getScore() + "\n\tCollected: " + p.getCollected() 
            + " & in Hand: " + p.getHand());
         answer = false;
      } // otherwise, no match and no jack, no winner won 
      p.removeBlanks();
      return answer; // return result from turn
   }
   
   
   public void sqPlay(boolean p, boolean r, int pIndex, int rIndex) {
      SequentialTransition st = new SequentialTransition();
      PathTransition pt1 = ptLine(middlePileImages.get(middlePileImages.size() - 1), 
         1, X2 + (pIndex * 50), Y_P, X2 + 50 + count, Y1);
      st.getChildren().add(pt1);
      int s = middlePileImages.size();
      // initiate players move
      if (p) { // if player won the turn
         for (ImageView iv: middlePileImages) {
            st.getChildren().add(fadeOut(iv, 0.1));
         } // fade out images in middle pile
         st.getChildren().add(fadeOut(middlePileImages.get(middlePileImages.size() - 1), 0.1));
         if (roundNumber == 6)
            roundWinnerFinal(player);
         else
            roundWinner(player); // declare player as winner to add cards to collected
         count = 10; // restart count position
         PathTransition pt2 = ptLine(robot.getACardImage(rIndex), 3, 
            X2 + (rIndex * 50), Y_R, X2, Y1); // add robot's play animation
         st.getChildren().add(pt2);
         lastToMatch = "p";
      }
      else if (r) { // if robot won
         PathTransition pt2 = ptLine(robot.getACardImage(rIndex), 3, 
            X2 + (rIndex * 50), Y_R, X2 + 50 + count, Y1);
         st.getChildren().add(pt2); // play robot's move
         
         for (ImageView iv: middlePileImages) {
            st.getChildren().add(fadeOut(iv, 0.1));
         } // then fade out cards in middle pile
         st.getChildren().add(fadeOut(robot.getACardImage(rIndex), 0.1));
         if (roundNumber == 6)
            roundWinnerFinal(robot);
         else
            roundWinner(robot); // robot wins round, add cards to collected
         count = 10; // increase count for position of cards to play from
         lastToMatch = "r";
      }
      else {
         count += 10; // increase count
         PathTransition pt2 = ptLine(robotPlay(), 3, X2 + (rIndex * 50), Y_R, 
            X2 + 50 + count, Y1);
         st.getChildren().add(pt2); // add robot's play animation
         count += 10; // continue to increase count bc adding to middle pile
      }
            
      st.play(); // play animation determined by turn result of what should play
   }
   
   
   public void deal() {
      for (int i = 0; i < 4; i++) {
         deck.drawNext(player.handImages, player.hand); 
         // remove card from deck and add to player's hand
         deck.drawNext(robot.handImages, robot.hand);
         // remove card from deck and add to robot's hand
      }
   }
   
   public void dealAnimate() {
      final double T = 1; // time
      SequentialTransition st = new SequentialTransition();
      st.getChildren().addAll(
         ptLine(player.getACardImage(0), T, X1, Y1, X2, Y_P),
         ptLine(robot.getACardImage(0), T, X1, Y1, X2, Y_R),
         ptLine(player.getACardImage(1), T, X1, Y1, X2 + 50, Y_P),
         ptLine(robot.getACardImage(1), T, X1, Y1, X2 + 50, Y_R),
         ptLine(player.getACardImage(2), T, X1, Y1, X2 + 100, Y_P),
         ptLine(robot.getACardImage(2), T, X1, Y1, X2 + 100, Y_R),
         ptLine(player.getACardImage(3), T, X1, Y1, X2 + 150, Y_P),
         ptLine(robot.getACardImage(3), T, X1, Y1, X2 + 150, Y_R));
      st.play();
   } // animation adding to deal() method to show user what is happening
   
   public void finalRound() {
      Rectangle r = new Rectangle(X1 - 37.5, Y_M, 75, 125);
      r.setFill(Color.GREEN); 
      //pane.getChildren().add(r);
      FadeTransition ft = new FadeTransition(Duration.millis(100), r);
      pane.getChildren().add(r);
      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      
      final double T = 1; // time
      SequentialTransition st = new SequentialTransition();
      st.getChildren().addAll(
         ptLine(player.getACardImage(0), T, X1, Y1, X2, Y_P),
         ptLine(robot.getACardImage(0), T, X1, Y1, X2, Y_R),
         ptLine(player.getACardImage(1), T, X1, Y1, X2 + 50, Y_P),
         ptLine(robot.getACardImage(1), T, X1, Y1, X2 + 50, Y_R),
         ptLine(player.getACardImage(2), T, X1, Y1, X2 + 100, Y_P),
         ptLine(robot.getACardImage(2), T, X1, Y1, X2 + 100, Y_R),
         ptLine(player.getACardImage(3), T, X1, Y1, X2 + 150, Y_P),
         ptLine(robot.getACardImage(3), T, X1, Y1, X2 + 150, Y_R),
         ft);
      st.play();
      
      round1();  
      
   }
   
   
   public void roundWinnerFinal(Player p) { // method for winner of the round
   
      for (int i = 0; i < middlePile.size(); i++)
         p.addToCollected(middlePile.get(i)); // adds card to its collected pile
      for (int i = 0; i < middlePileImages.size(); i++)
         p.addToCollectedImages(middlePileImages.get(i)); // adds image to collected
      middlePile.clear(); // clear middle pile
      middlePileImages.clear(); // clear images
      middlePile.add("BLANK"); // add blank (used to prevent comparing to "nothing"
      // and elim indexOutOfBounds from occuring
      middlePileImages.add(deck.getCardBackImage());
      System.out.println(middlePile + "\n" + middlePileImages);
      // add these two so that middle piles arent empty and no errors occur
      // when cards are laid down on empty table
   }
   
   public void lastMoves() {
      SequentialTransition st = new SequentialTransition();
      
      for (ImageView iv: middlePileImages) {
         st.getChildren().add(fadeOut(iv, 0.1));
      } // fade out images in middle pile
      st.getChildren().add(fadeOut(middlePileImages.get(middlePileImages.size() - 1), 0.1));
      
      st.play();
      
      if (lastToMatch.equals("p")) {
         for (String s: middlePile)
            player.addToCollected(s);
      }
      else {
         for (String s: middlePile)
            robot.addToCollected(s);
      }
      
      player.removeBlanks();
      robot.removeBlanks();
      player.has2Clubs();
      player.has10Diamonds();
      robot.has2Clubs();
      robot.has10Diamonds();
      player.otherPoints();
      robot.otherPoints();
      player.compareSize(player, robot);
      
      System.out.print(player.getCollected() + "\nSCORE: " + player.score.getScore());
      System.out.print(robot.getCollected() + "\nSCORE: " + robot.score.getScore());
      
      String winner = "";
      if (player.score.getScore() > robot.score.getScore())
         winner = "\n\nPLAYER WINS!!!";
      else if (robot.score.getScore() > player.score.getScore())
         winner = "\n\nROBOT WINS!!!";
      else
         winner = "\n\nITS A TIE!!!";
      String s = "\nPlayer's Score: " + player.score.getScore() 
         + "\nComputer's Score: " + robot.score.getScore();
      
      winner = winner + s;
      
      ImageView balloons = new ImageView(new Image("balloonsLine.png"));
      Label win = new Label(winner, balloons);
      //lb1.setContentDisplay(ContentDisplay.TOP);
      win.setTextFill(Color.WHITE);
      pane.setCenter(win);
      win.setFont(new Font("Impact", 30));
         
   }
} 