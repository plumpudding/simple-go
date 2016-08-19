package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.petabyteboy.go.Position;

public class Token {
	
	private final int x;
	private final int y;
	private final int playerId;
	private int groupId = -1;
	
	public Token(int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.playerId = Controller.getInstance().getGS().getPlayerNum(player);
	}

	public Player getPlayer() {
		return Controller.getInstance().getGS().getPlayers()[playerId];
	}
	
	public List<Token> getNeighbours() {
		List<Token> neighbours = new ArrayList<Token>();

		if (Controller.getInstance().getPlayerAt(x - 1, y) == getPlayer())
			neighbours.add(Controller.getInstance().getTokenAt(x - 1, y));
		if (Controller.getInstance().getPlayerAt(x + 1, y) == getPlayer())
			neighbours.add(Controller.getInstance().getTokenAt(x + 1, y));
		if (Controller.getInstance().getPlayerAt(x, y - 1) == getPlayer())
			neighbours.add(Controller.getInstance().getTokenAt(x, y - 1));
		if (Controller.getInstance().getPlayerAt(x, y + 1) == getPlayer())
			neighbours.add(Controller.getInstance().getTokenAt(x, y + 1));
		
		return neighbours;
	}

	public Group getGroup() {
		if (groupId >=0 && getPlayer().getGroups().size() > groupId)
			return getPlayer().getGroups().get(groupId);
		
		return null;
	}

	public void setGroup(Group group) {
		groupId = getPlayer().getGroups().indexOf(group);
	}
	
	public Set<Position> getFreedoms() {
		Set<Position> freedoms = new HashSet<Position>();

		if (Controller.getInstance().getTokenAt(x - 1, y) == null)
			freedoms.add(Position.getInstance(x - 1, y));
		if (Controller.getInstance().getTokenAt(x + 1, y) == null)
			freedoms.add(Position.getInstance(x + 1, y));
		if (Controller.getInstance().getTokenAt(x, y - 1) == null)
			freedoms.add(Position.getInstance(x, y - 1));
		if (Controller.getInstance().getTokenAt(x, y + 1) == null)
			freedoms.add(Position.getInstance(x, y + 1));
		
		freedoms.remove(null);
		
		return freedoms;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
