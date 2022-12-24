package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.lwjgl.glfw.GLFWVidMode;

public class Window {
	public long window;

	public int width, height;

	public Window(int width, int height, String title) {
		glfwInit();
		
		this.width = width;
		this.height = height;
		
		window = glfwCreateWindow(width, height, title, 0, 0);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void setFullscreen(boolean isFull) {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);

		glfwSetWindowMonitor(window, monitor, 0, 0, width, height, mode.refreshRate());
	}

	public void close() {
		glfwTerminate();
	}
}
