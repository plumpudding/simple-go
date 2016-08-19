package de.petabyteboy.go.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import de.petabyteboy.go.engine.graphics.Renderer;
import de.petabyteboy.go.logic.Controller;

public class Game {

	private String version = "0.0.1-SNAPSHOT";
	private long window;
	private Renderer renderer = Renderer.getInstance();
	private Input input = new Input();
	private Controller ctrl = Controller.getInstance();
	private int windowWidth = 800;
	private int windowHeight = 800;
	private int borderX = 40;
	private int borderY = 40;

	private Game() {
	}

	private static Game instance;

	public static Game getInstance() {
		if (instance == null)
			
			instance = new Game();
		return instance;
	}

	public void run() {
		init();

		while (!GLFW.glfwWindowShouldClose(window))
			loop();

		GLFW.glfwTerminate();
		GLFW.glfwDestroyWindow(window);
	}

	public void loop() {
		renderer.render();
	}

	public void init() {
		System.out.println("Starting SimpleGo version " + version + "...");

		if (!GLFW.glfwInit())
			System.err.println("GLFW initialization failed!");
		
//		GLCapabilities caps = GL.createCapabilities();
//
//        if (caps.OpenGL32) {
//            System.out.println("Asking for OpenGL3.2 any profile");
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
//            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);
//            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_FALSE);
//        } else if (caps.OpenGL21) {
//            System.out.println("Using OpenGL 2.1");
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
//            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
//        } else {
//            throw new RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is "
//                                       + "supported, you may want to update your graphics driver.");
//        }
		
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		window = GLFW.glfwCreateWindow(windowWidth, windowHeight, "SimpleGo", 0, 0);

		if (window == 0l)
			throw new RuntimeException("Could not create window");

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				Controller.getInstance().saveState();
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});
		
		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			input.pollMouseInput(button, action);
		});
		
		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
			GL11.glViewport(0, 0, width, height);
			this.windowWidth = width;
			this.windowHeight = height;
			if (width > height) {
				this.borderX = (int)((float)(width - height) / 2 + (float)(height) / 100 * 5);
				this.borderY = (int)((float)(height) / 100 * 5);
			} else if (width < height) {
				this.borderX = (int)((float)(width) / 100 * 5);
				this.borderY = (int)((float)(height - width) / 2 + (float)(width) / 100 * 5);
			} else {
				this.borderX = (int)((float)(width) / 100 * 5);
				this.borderY = (int)((float)(width) / 100 * 5);
			}
		});
		
		renderer.init();
		ctrl.init();
		input.init();
	}

	public long getWindow() {
		return window;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public int getBorderX() {
		return borderX;
	}

	public int getBorderY() {
		return borderY;
	}

}
