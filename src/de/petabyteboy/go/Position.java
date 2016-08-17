package de.petabyteboy.go;

import de.petabyteboy.go.logic.Controller;

public class Position {
	
	public int x;
	public int y;
	
	private Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private static Position[][] instances;
	
	public static Position getInstance(int x, int y) {
		int boardSize = Controller.getInstance().getGameBoard().size;
		
		if (x < 0 || y < 0 || x >= boardSize || y >= boardSize)
			return null;
		
		if (instances == null)
			instances = new Position[boardSize][boardSize];
		
		if (instances[x][y] == null)
			instances[x][y] = new Position(x, y);
		
		return instances[x][y];
	}
	

}
