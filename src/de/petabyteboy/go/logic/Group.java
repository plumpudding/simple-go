package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.petabyteboy.go.Position;

public class Group {

	private List<Token> tokens = new ArrayList<Token>(); 
	private List<Token> tokensToAdd = new ArrayList<Token>(); 
	private Set<Position> freedoms = new HashSet<Position>();
	private boolean inactive = false;
	private Player player;
	public boolean badMove = false;
	
	public Group(Token t) {
		player = t.getPlayer();
		tokens.add(t);
		t.setGroup(this);
		
		rebuild();
		
//		System.out.println("New group discovered. Size: " + tokens.size() + ". Freedoms: " + freedoms.size());
		player.addGroup(this);
	}
	
	public void rebuild() {
		if (inactive)
			return;
		
		tokensToAdd.clear();
		
		for (Token t : tokens)
			discoverNeighbours(t);
		
		for (Token t : tokensToAdd)
			tokens.add(t);
		
		buildFreedoms();
		
		if (freedoms.size() == 0) {
			this.destroy();
//			System.out.println("Group destroyed!");
		} else {
			badMove = false;
//			System.out.println("Group updated. Size: " + tokens.size() + ". Freedoms: " + freedoms.size());
		}
	}
	
	public void destroy() {
		if (this.player == Controller.getInstance().getActivePlayer()) {
			System.out.println("bad move!");
			badMove = true;
			return;
		}
		
		this.setInactive();

		for (Token t : tokens)
			t.destroy();
	}
	
	public void discoverNeighbours(Token t) {
		List<Token> tokensToCheck = t.getNeighbours();
		tokensToCheck.removeAll(tokens);
		tokensToCheck.removeAll(tokensToAdd);
		
		for (Token token : tokensToCheck) {
			Group otherGroup = token.getGroup();
			
			if (otherGroup != null)
				otherGroup.setInactive();
			
			addToken(token);
			discoverNeighbours(token);
		}
	}
	
	private void setInactive() {
		inactive = true;
		player.removeGroup(this);
	}

	public void addToken(Token t) {
		t.setGroup(this);
		tokensToAdd.add(t);
	}
	
	public void buildFreedoms() {
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
