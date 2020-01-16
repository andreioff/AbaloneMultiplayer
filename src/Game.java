import java.util.Scanner;

import ss.utils.TextIO;


public class Game {
	
 private Board board;
 
 private Player[] players;
 
 private int nrPlayers;
 
 private int current;
 
 public Game(int nrPlayers) { 
	 board = new Board(nrPlayers);
	 current = 0;
	 players = new Player[nrPlayers];
	 
	 for(int i = 0;i<=nrPlayers;i++) { 
		 
		 
		players[i] = new Player(String name, int color);
	 }
 }
	 
	 public void start() {
		 boolean continueGame = true;
		 while (continueGame) {
			 reset();
			 play();
			 System.out.println("Do you want a rematch?(y/n)");
			 continueGame = TextIO.getBoolean();		 
			 }
		 
	 }
	 
	 private void reset() {
	        current = 0;
	        board.setup(nrPlayers);
	    }
	 
	 private void update() {
	        System.out.println("\nThis is the current game situation: \n\n" + board.toString()
	                + "\n");
	    }
	 
	 private void play() {
	    	update();
	    	while(!board.hasWinner()) {
	    		while(current <= nrPlayers) {
	    			players[current].makeMove(board);
	    		current++;
	    		update();
	    		}
	    		current = 0;
	    	}
	    		printOutcome();
	    	}
	 
	 private void printOutcome() {
		 String[] colors[];
		 int[] score = board.getScore();
	      int max = score[0];
	      int index = 0;
	      int i;
	      for(i = 0; i<= nrPlayers;i++) {
	    	  if(score[i] > max) {
	    		  max = score[i];
	    		  index = i;  
	    	  }
	    	  if()
	    
	    	  
	      }
	      
	 }	 
	 
 }
 

