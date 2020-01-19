package Marble;

public class Marble {
	public static final String[] colors = {"Red", "White", "Blue", "Black"};
	private int mColor;

	public Marble(int color) {
		//1 - Red; 2 - White; 3 - Blue; 4 - Black;
		if (color > 1 && color <= 4) {
			mColor = color;
		} else {
			mColor = 1;
		}

	}

	 public int getColorNr() {
			 return mColor;
	 }

	 public String getColor() {
		return colors[mColor];
	 }

	 public static int getTeammateColor(int color) {
		switch (color) {
			case 1: return 3;
			case 2: return 4;
			case 3: return 1;
			case 4: return 2;
			default: return color;
		}
	 }

	 public String toString() {
	 	return colors[mColor];
	 }
}
	 
	 
	 
	
		
	
	


