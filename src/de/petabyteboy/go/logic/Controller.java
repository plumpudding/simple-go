package de.petabyteboy.go.logic;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;

public class Controller {

	private List<Token> newTokens = new ArrayList<Token>();
	private GameState gs;
	private Gson gson = new Gson();
	private String clientId = String.valueOf(Math.random() * 1000);
	
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
//		if (getTokenAt(x, y) == null) {
//			if (placeToken(x, y)) {
//
//				ts.saveCurrentState();
//				System.out.println(ts.getCurrentState());
//
//			} else {
//				ts.restoreState(0);
//			}
//			gs.nextPlayer();
//		}

		String url = "http://localhost:4000";
		BasicHttpClient client = new BasicHttpClient(url);
		client.setConnectionTimeout(2000);
		ParameterMap params = client.newParams().add("x", String.valueOf(x)).add("y", String.valueOf(y)).add("player", String.valueOf(gs.activePlayer)).add("clientId", clientId);
		HttpResponse response = client.get("/placeToken", params);
		String state = response.getBodyAsString();
		restoreState(state);
		
//		try {
//			URLConnection connection = new URL(url + "placeToken?x="+x+"&y="+y+"&p="+gs.activePlayer).openConnection();
//			connection.setRequestProperty("Accept-Charset", "UTF-8");
//			InputStream response = connection.getInputStream();
//			state = response.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		System.out.println(state);
		
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
	
	public void restoreState(String state) {
		Controller.getInstance().setGS(gson.fromJson(state, GameState.class));
	}
	
}
