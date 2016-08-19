package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.List;

public class Controller {

	private List<Token> newTokens = new ArrayList<Token>();
	private GameState gs;
	private TimeMachine ts = new TimeMachine();
	
	private static Controller instance;
	
	private Controller() {
	}
	
	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		
		return instance;
	}
	
	public void init() {
		gs = new GameState(2, 9);
	}

	public void mouseClick(int x, int y) {
		if (getTokenAt(x, y) == null) {
			if (placeToken(x, y)) {

				ts.saveCurrentState();
				System.out.println(ts.getCurrentState());

			} else {
				ts.restoreState(0);
			}
			gs.nextPlayer();
		}
	}
	
	public boolean placeToken(int x, int y) {
		
		Token t = new Token(x, y, gs.getActivePlayer());
		newTokens.add(t);
		
		for (Player p : gs.getOtherPlayers())
			p.rebuildGroups();

		gs.getActivePlayer().rebuildGroups();
		
		newTokens.remove(t);
		
		if (t.getGroup() == null)
			new Group(t, false);
		
		if (t.getGroup().badMove || !ts.isMoveValid()) {
			return false;
		}
		
		return true;
	}
	
	public Token getTokenAt(int x, int y) {
		for (Token t : newTokens)
			if (t.getX() == x && t.getY() == y)
				return t;
		
		for (Player p : gs.getPlayers())
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
	
	public GameState getGS() {
		return gs;
	}
	
	public void setGS(GameState gs) {
		this.gs = gs;
	}

	public void saveState() {
		
	}
	
}
