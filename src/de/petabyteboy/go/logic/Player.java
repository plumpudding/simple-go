package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.List;

import de.petabyteboy.go.engine.graphics.Texture;

public class Player {
	
	private int score;
	private String texturePath;
	private transient Texture texture;
	private List<Group> groups = new ArrayList<Group>();
	private transient List<Group> groupsToRemove;
	
	public Player(String texturePath) {
		this.texturePath = texturePath;
	}
	
	public int getScore() {
		return score;
	}
	
	public void rebuildGroups() {
		if (groupsToRemove == null)
			groupsToRemove = new ArrayList<Group>();
		else
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
		if (texture == null)
			texture = new Texture(texturePath);
		
		return texture;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
}
