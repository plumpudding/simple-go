package de.petabyteboy.go.logic;

public class GameBoard {
	
	public int size = 13;
	private Token[][] grid = new Token[size][size];
	private int[][] currentGrid = new int[size][size];
	private int[][][] timeMachine = new int [3][size][size];
	
	public GameBoard() {
	}
	
	public Token[][] getGrid() {
		return grid;
	}

	public int getSize() {
		return size;
	}
	
	public boolean genCurrentGrid() {
		currentGrid = new int[size][size];
		boolean ret = true;

		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++) {
				currentGrid[x][y] = Controller.getInstance().getPlayerNum(Controller.getInstance().getPlayerAt(x, y));
				if (currentGrid[x][y] != timeMachine[1][x][y])
					ret = false;
			}
		
		return ret;
	}
		
	public void saveCurrentGrid() {
		timeMachine[1] = timeMachine[0];
		timeMachine[0] = currentGrid;
	}
	
	public int[][] getCurrentGrid() {
		return currentGrid;
	}
	
	public boolean compareGrids() {
		return genCurrentGrid();
	}

}
