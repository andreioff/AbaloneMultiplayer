package abaloneMultiplayer;

public class Marble {
	private int marble;
	private int mColor;
	
	 public static final int EMPTY = 0;
	 public static final int RED = 1;
	 public static final int WHITE = 2;
	 public static final int BLUE = 3;
	 public static final int BLACK= 4;
	 
	 public Marble(int marble) {
		 this.marble = marble;
	 }
	 
	 public void color(int mColor) {
		 if (marble == 0) {
			 mColor = EMPTY;
		 }
		 if(marble == 1) {
			 mColor = RED;
		 }
		 if(marble == 2) {
			 mColor = WHITE;
		 }
		 if(marble == 3) {
			 mColor = BLUE;
		 }
		 if(marble == 4) {
			 mColor = BLACK;
		 }
	 }
		 
		 public int getColor() {
			 return mColor;
		 }
		 
		 public String toString() {
			 return "This " + marble + " is" + mColor;
		 }
		 
		 
	 }
	 
	 
	 
	
		
	
	


