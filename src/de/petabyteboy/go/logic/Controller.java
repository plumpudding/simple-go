package de.petabyteboy.go.logic;

import de.petabyteboy.go.engine.graphics.Texture;

public class Controller {

	private GameBoard board = new GameBoard();
	private int playerCount = 2;
//	private int playerCount = 4;
	private Player[] players = new Player[playerCount];
	private int activePlayer = 0;
	
	public GameBoard getGameBoard() {
		return board;
	}
	
	private static Controller instance;
	
	private Controller() {
	}
	
	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		
		return instance;
	}
	
	public void init() {
		players[0] = new Player(new Texture("res/black.png"));
		players[1] = new Player(new Texture("res/white.png"));
//		players[2] = new Player(new Texture("res/3.png"));
//		players[3] = new Player(new Texture("res/4.png"));
	}
	
	private void nextPlayer() {
		activePlayer++;
		
		if (activePlayer == playerCount)
			activePlayer = 0;
	}

	public void mouseClick(int x, int y) {
		if (getTokenAt(x, y) == null) {
			if (placeToken(x, y))
				nextPlayer();
		}
	}
	
	public boolean placeToken(int x, int y) {

		board.getGrid()[x][y] = new Token(x, y, players[activePlayer]);

		for (Player p : getOtherPlayers())
			p.rebuildGroups();
		
		getActivePlayer().rebuildGroups();
		
		if (getTokenAt(x, y).getGroup() == null)
			new Group(getTokenAt(x, y));
		
		if (getTokenAt(x, y).getGroup().badMove /* || board.compareGrids() */) {
			if (getTokenAt(x, y).getGroup().getSize() == 1)
				getActivePlayer().removeGroupNow(board.getGrid()[x][y].getGroup());
			else
				getTokenAt(x, y).getGroup().removeToken(getTokenAt(x, y));
			
			board.getGrid()[x][y] = null;
			return false;
		}
		
		board.genCurrentGrid();
		board.saveCurrentGrid();
		return true;
	}
	
	private Player[] getOtherPlayers() {
		Player[] otherPlayers = new Player[playerCount-1];
		
		int i = 0;
		for (Player p : players) {
			if (p != getActivePlayer()) {
				otherPlayers[i] = p;
				i++;
			}
		}
		
		return otherPlayers;
	}
	
	public Token getTokenAt(int x, int y) {
		if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize())
			return null;
		
		return board.getGrid()[x][y];
	}

	public Player getPlayerAt(int x, int y) {
		Token token = getTokenAt(x, y);
		
		if (token == null)
			return null;
		
		return token.getPlayer();
	}
	
	public Player getActivePlayer() {
		return players[activePlayer];
	}

	public int getPlayerNum(Player player) {
		if (player == null)
			return -1;
		
		for (int i = 0; i < players.length; i++)
			if (players[i] == player)
				return i;
		
		return -1;
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
}
