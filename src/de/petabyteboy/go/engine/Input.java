package de.petabyteboy.go.engine;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import de.petabyteboy.go.logic.Controller;

public class Input {
	
	private long window;
	
	public Input() {
	}
	
	public void init() {
		this.window = Game.getInstance().getWindow();
	}
	
	public void pollMouseInput(int button, int action) {
		if (button != GLFW.GLFW_MOUSE_BUTTON_1 || action != GLFW.GLFW_RELEASE)
			return;

		int borderX = Game.getInstance().getBorderX();
		int borderY = Game.getInstance().getBorderY();
		int boardSize = Controller.getInstance().getGS().getBoardSize();
		
		DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
		
		GLFW.glfwGetCursorPos(window, xPos, yPos);
		
		int targetX = (int) ((xPos.get() - borderX) / (Game.getInstance().getWindowWidth() - 2 * borderX) * boardSize);
		int targetY = (int) ((yPos.get() - borderY) / (Game.getInstance().getWindowHeight() - 2 * borderY) * boardSize);
		
		if (targetX < 0 || targetX >= boardSize || targetY < 0 || targetY >= boardSize)
			return;
		
		Controller.getInstance().mouseClick(targetX, targetY);
	}

}
