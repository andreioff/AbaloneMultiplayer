package src.Marble;

public class Marble {

	private int mColor;

	public Marble(int color) {
		// 0 - Empty; 1 - Red; 2 - White; 3 - Blue; 4 - Black;
		if (color >= 1 && color <= 4) {
			mColor = color;
		} else {
			mColor = 0;
		}

	}

	 public int getColor() {
			 return mColor;
		 }

	 public String toString() {
	 	return Integer.toString(mColor);
	 }
}
	 
	 
	 
	
		
	
	


