package de.petabyteboy.go.engine.graphics;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import de.petabyteboy.go.engine.Game;
import de.petabyteboy.go.logic.Controller;
import de.petabyteboy.go.logic.Group;
import de.petabyteboy.go.logic.Player;
import de.petabyteboy.go.logic.Token;

public class Renderer {
	
	private long window;
	private Texture bgTexture;
	private float offsetX;
	private float offsetY;
	private int boardSize;
	private float tokenSizeX;
	private float tokenSizeY;
	
	private Renderer() {
	}

	private static Renderer instance;

	public static Renderer getInstance() {
		if (instance == null)
			instance = new Renderer();

		return instance;
	}

	public void init() {
		
		this.window = Game.getInstance().getWindow();
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);

		GL.createCapabilities();
		
		GL11.glClearColor(0.9f, 0.7f, 0.5f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		bgTexture = new Texture("res/bg.png");
		
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
	}

	public void render() {
		int windowHeight = Game.getInstance().getWindowHeight();
		int windowWidth = Game.getInstance().getWindowWidth();
		int borderX = Game.getInstance().getBorderX();
		int borderY = Game.getInstance().getBorderY();
		boardSize = Controller.getInstance().getGS().getBoardSize();
		
		if (windowHeight > windowWidth) {
			offsetX = ((float)(borderX) / windowWidth) * 2;
			offsetY = ((float)(borderY) / windowHeight) * 2;
			tokenSizeX = (((float)(windowWidth - (borderX * 2)) / boardSize) / windowWidth) * 2;
			tokenSizeY = (((float)(windowWidth - (borderX * 2)) / boardSize) / windowHeight) * 2;
		} else {
			offsetX = ((float)(borderX) / windowWidth * 2);
			offsetY = ((float)(borderY) / windowHeight * 2);
			tokenSizeX = (((float)(windowHeight - (borderY * 2)) / boardSize) / windowWidth) * 2;
			tokenSizeY = (((float)(windowHeight - (borderY * 2)) / boardSize) / windowHeight) * 2;
		}

//		System.out.println(windowWidth);
//		System.out.println(borderX);
//		System.out.println(boardSize * tokenSizeX + 2 * offsetX);
//		System.out.println();

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		drawBg();
		drawGridLines();
		
		//draw tokens
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1f, 1f, 1f);
		
		for (Player p : Controller.getInstance().getGS().getPlayers())
			for (Group g : p.getGroups())
				for (Token token : g.getTokens())
					if (token != null)
						drawToken(token);

		GLFW.glfwSwapBuffers(window);
		GLFW.glfwWaitEvents();
	}
	
	private void drawBg() {
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1f, 1f, 1f);
		bgTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(-1f, -1f);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(1f, -1f);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(1f, 1f);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(-1f, 1f);
		GL11.glEnd();
	}
	
	private void drawGridLines() {

		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0.4f, 0.3f, 0.3f);
		GL11.glTranslatef(-1f + (offsetX + tokenSizeX / 2), 1f - (offsetY + tokenSizeY / 2), 0f);
		GL11.glLineWidth(2.0f);

		for (int i = 0; i < boardSize; i++) {
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(0f, 0f);
			GL11.glVertex2f(0f, -1 * (tokenSizeY * (boardSize-1)));
			GL11.glEnd();
			GL11.glTranslatef(tokenSizeX, 0f, 0f);
		}
		
		GL11.glLoadIdentity();
		GL11.glTranslatef(-1f + (offsetX + tokenSizeX / 2), 1f - (offsetY + tokenSizeY / 2), 0f);

		for (int i = 0; i < boardSize; i++) {
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(0f, 0f);
			GL11.glVertex2f(tokenSizeX * (boardSize-1), 0f);
			GL11.glEnd();
			GL11.glTranslatef(0f, -tokenSizeY, 0f);
		}
	}
	
	private void drawToken(Token t) {
		GL11.glLoadIdentity();
		t.getPlayer().getTexture().bind();
		GL11.glTranslatef(-1f + (offsetX + tokenSizeX * t.getX()), 1f - (offsetY + tokenSizeY * (t.getY()+1)), 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0f, 1f);
		GL11.glVertex2f(0f, 0f);
		GL11.glTexCoord2f(0f, 0f);
		GL11.glVertex2f(0f, tokenSizeY);
		GL11.glTexCoord2f(1f, 0f);
		GL11.glVertex2f(tokenSizeX, tokenSizeY);
		GL11.glTexCoord2f(1f, 1f);
		GL11.glVertex2f(tokenSizeX, 0f);
	    GL11.glEnd();
	}
	
}
