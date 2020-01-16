

public  abstract class Player {
	
	private String name;
	private int color;
	
	public Player(String name, int color) {
        this.name = name;
        this.color = color;
	}
	
	public String getName() {
        return name;
	}
	
	public int getMType() {
        return color;
	}
	
	public abstract int determineMove(Board board);
	
	public void makeMove(Board board) { 
	
		
	}
	

}
