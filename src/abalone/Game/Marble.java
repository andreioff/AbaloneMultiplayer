package abalone.Game;

public class Marble {

	public static final String[] colors = {"White", "Black", "Yellow", "Red"};

	private int mColor;

	/**
	 * Marble constructor.
	 * @param color = the color that this marble just have
	 * @ensures mColor >= 1 && mColor <= 4
	 **/
	public Marble(int color) {
		//1 - White; 2 - Black; 3 - Yellow; 4 - Red;
		if (color > 1 && color <= 4) {
			mColor = color;
		} else {
			mColor = 1;
		}
	}

	/**
	 * Returns the color's number of this marble.
	 * @returns an integer containing the index of the color if this marble
	 */
	public int getColorNr() {
		return mColor;
	}

	/**
	 * Returns the color of this marble
	 * @returns a String object containing the color of this marble
	 */
	public String getColor() {
		return colors[mColor - 1];
	}

	/**
	 * Get the current color's teammate's color.
	 * @param color the color for which the teammate's color should be returned
	 * @returns an integer with the index of the teammate's color of the given color or 0 if the given color index
	 * does not exists
	 * @requires color >= 1 && color <= 4
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

	/**
	 * Returns the next color that should make a move based on the number of players.
	 * @param color = the current color that is making a move
	 * @param players = the number of players of the game
	 * @returns an integer containing the index of the next color that should make a move
	 * @requires color >= 1 && color <= 4
	 * @requires players >= 2 && players <= 4
	 * @ensures return >= 1 && return <= 4
	 */
	public static int getNextColor(int color, int players) {
		color++;
		if (color == players + 1) color = 1;
		return color;
	}
}
