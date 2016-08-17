package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.List;

import de.petabyteboy.go.engine.graphics.Texture;

public class Controller {

	private int playerCount = 2;
//	private int playerCount = 4;
	private Player[] players = new Player[playerCount];
	private int activePlayer = 0;
	private int boardSize = 9;
	private List<Token> newTokens = new ArrayList<Token>();
	
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
		
		Token t = new Token(x, y, getActivePlayer());
		newTokens.add(t);
		
		for (Player p : getOtherPlayers())
			p.rebuildGroups();

		getActivePlayer().rebuildGroups();
		
		newTokens.remove(t);
		
		if (t.getGroup() == null)
			new Group(t, false);
		System.out.println(t.getGroup());
		
		if (t.getGroup().badMove /* || board.compareGrids() */) {
			if (t.getGroup().getSize() == 1)
				t.getGroup().destroy(true);
			else
				t.getGroup().removeToken(t);
			
			return false;
		}
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
		for (Token t : newTokens)
			if (t.getX() == x && t.getY() == y)
				return t;
		
		for (Player p : players)
			for (Group g : p.getGroups())
				for (Token t : g.getTokens())
					if (t.getX() == x && t.getY() == y)
						return t;
		
		return null;
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

	public int getBoardSize() {
		return boardSize;
	}
	
}
