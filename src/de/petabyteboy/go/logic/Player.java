package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.List;

import de.petabyteboy.go.engine.graphics.Texture;

public class Player {
	
	private int score;
	private Texture texture;
	private List<Group> groups = new ArrayList<Group>();
	private List<Group> groupsToRemove = new ArrayList<Group>();
	
	public Player(Texture texture) {
		this.texture = texture;
	}
	
	public int getScore() {
		return score;
	}
	
	public void rebuildGroups() {
		groupsToRemove.clear();
		
		for (Group g : groups)
			g.rebuild();
		
		for (Group g : groupsToRemove)
			groups.remove(g);
	}

	public void addGroup(Group group) {
		groups.add(group);
	}
	
	public void removeGroup(Group group) {
		groupsToRemove.add(group);
	}
	
	public void removeGroupNow(Group group) {
		groups.remove(group);
	}

	public Texture getTexture() {
		return texture;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
}
