package de.petabyteboy.go.logic;

import java.util.Stack;

import com.google.gson.Gson;

public class TimeMachine {

	private Gson gson = new Gson();
	private Stack<String> oldStates = new Stack<String>();
	private String currentState;
	
	public void genCurrentState() {
		currentState = gson.toJson(Controller.getInstance().getGS());
	}
	
	public boolean isMoveValid() {
		genCurrentState();
		if (oldStates.size() > 2 && oldStates.get(oldStates.size()-2) != null) {
			System.out.println("===");
			System.out.println(currentState);
			System.out.println(oldStates.get(oldStates.size()-1));
			System.out.println(oldStates.get(oldStates.size()-2));
			System.out.println("===");
			return !(oldStates.get(oldStates.size()-2).equals(currentState));
		} else
			return true;
	}
	
	public void saveCurrentState() {
		genCurrentState();
		oldStates.push(currentState);
	}
	
	public void restoreState(int t) {
		Controller.getInstance().setGS(gson.fromJson(oldStates.get(oldStates.size()-1), GameState.class));
	}

	public String getCurrentState() {
		return currentState;
	}
	
}
