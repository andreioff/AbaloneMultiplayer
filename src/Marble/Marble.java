package Marble;

public class Marble {

	private int mColor;

	public Marble(int color) {
		//1 - Red; 2 - White; 3 - Blue; 4 - Black;
		if (color > 2 && color <= 4) {
			mColor = color;
		} else {
			mColor = 1;
		}

	}

	 public int getColor() {
			 return mColor;
		 }

	 public String toString() {
	 	return Integer.toString(mColor);
	 }
}
	 
	 
	 
	
		
	
	


