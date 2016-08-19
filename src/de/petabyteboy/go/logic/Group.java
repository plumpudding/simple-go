package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.petabyteboy.go.Position;

public class Group {

	private List<Token> tokens = new ArrayList<Token>();
	private transient List<Token> tokensToAdd; 
	private transient Set<Position> freedoms = new HashSet<Position>();
	private transient boolean inactive;
	private final int playerId;
	public transient boolean badMove;
	
	public Group(Token t, boolean b) {
		playerId = Controller.getInstance().getGS().getPlayerNum(t.getPlayer());
		getPlayer().addGroup(this);
		tokens.add(t);
		t.setGroup(this);
		
		if (!b)
			rebuild();
		
		System.out.println(System.currentTimeMillis() + " New group discovered. Size: " + tokens.size() + ". Freedoms: " + freedoms.size());
	}
	
	public Player getPlayer() {
		return Controller.getInstance().getGS().getPlayers()[playerId];
	}
	
	public void rebuild() {
		if (inactive)
			return;
		
		if (tokensToAdd == null)
			tokensToAdd = new ArrayList<Token>();
		else
			tokensToAdd.clear();
		
		for (Token t : tokens)
			discoverNeighbours(t);
		
		for (Token t : tokensToAdd)
			tokens.add(t);
		
		buildFreedoms();
		
		if (freedoms.size() == 0) {
			this.destroy(false);
		} else {
			badMove = false;
			System.out.println(System.currentTimeMillis() +" Group updated. Size: " + tokens.size() + ". Freedoms: " + freedoms.size());
		}
	}
	
	public void destroy(boolean b) {
		System.out.println(System.currentTimeMillis() +" Group " + this + " destroyed!");
		if (getPlayer() == Controller.getInstance().getGS().getActivePlayer() && !b) {
			System.out.println("bad move!");
			badMove = true;
			return;
		}
		
		this.setInactive(b);
		
		tokens.clear();
	}
	
	public void discoverNeighbours(Token t) {
		List<Token> tokensToCheck = t.getNeighbours();
		tokensToCheck.removeAll(tokens);
		tokensToCheck.removeAll(tokensToAdd);
		
		for (Token token : tokensToCheck) {
			Group otherGroup = token.getGroup();
			
			if (otherGroup != null)
				otherGroup.setInactive(false);
			
			addToken(token);
			discoverNeighbours(token);
		}
	}
	
	private void setInactive(boolean b) {
		inactive = true;
		
		if (b)
			getPlayer().removeGroupNow(this);
		else
			getPlayer().removeGroup(this);
	}

	public void addToken(Token t) {
		t.setGroup(this);
		tokensToAdd.add(t);
	}
	
	public void buildFreedoms() {
		if (freedoms == null)
			freedoms = new HashSet<Position>();
		else 
			freedoms.clear();
		
		for (Token t : tokens) {
			freedoms.addAll(t.getFreedoms());
		}
	}
	
	public int getSize() {
		return tokens.size();
	}

	public void removeToken(Token token) {
		tokens.remove(token);
	}

	public List<Token> getTokens() {
		return tokens;
	}
}
