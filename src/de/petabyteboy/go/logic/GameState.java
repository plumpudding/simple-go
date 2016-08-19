package de.petabyteboy.go.logic;

public class GameState {

	private int playerCount;
	private int boardSize;
	private Player[] players;
	private int activePlayer = 0;

	public GameState(int playerCount, int boardSize) {
		this.playerCount = playerCount;
		this.boardSize = boardSize;
		this.players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			System.out.println("res/" + i+1 + ".png");
			players[i] = new Player("res/" + (i+1) + ".png");
		}
	}
	
	public int getPlayerCount() {
		return playerCount;
	}

	public Player[] getPlayers() {
		return players;
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
	
	public Player[] getOtherPlayers() {
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

	public int getBoardSize() {
		return boardSize;
	}
	
	public void nextPlayer() {
		activePlayer++;
		
		if (activePlayer == getPlayerCount())
			activePlayer = 0;
	}

}
