package abalone.Game;

public class Marble {

	public static final String[] colors = {"White", "Black", "Blue", "Red"};

	private int mColor;

	/**
	 * Marble constructor.
	 * @param color != null
	 **/
	public Marble(int color) {
		//1 - White; 2 - Black; 3 - Blue; 4 - Red;
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

	/**
	 * Get the current color's teammate's color.
	 * @param color != null
	 * @return
	 **/
	public static int getTeammateColor(int color) {
		switch (color) {
			case 1: return 3;
			case 2: return 4;
			case 3: return 1;
			case 4: return 2;
			default: return 0;
		}
	}

	public static int getNextColor(int color, int players) {
		color++;
		if (color == players + 1) color = 1;
		return color;
	}

	public String toString() {
		return colors[mColor];
	}
}
