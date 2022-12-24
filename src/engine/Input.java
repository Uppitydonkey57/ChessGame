package engine;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.*;
import java.util.ArrayList;

public class Input {

	private Window window;

	private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	private ArrayList<Integer> keysDown = new ArrayList<Integer>();

	private ArrayList<Integer> mouseButtonsPressed = new ArrayList<Integer>();
	private ArrayList<Integer> mouseButtonsDown = new ArrayList<Integer>();

	public Input(Window window) {
		this.window = window;
		glfwSetMouseButtonCallback(window.window, mouseButtonCallback);
		glfwSetKeyCallback(window.window, keyCallback);
	}

	public boolean mouseButtonDown(int button) {
		for (int currentButton : mouseButtonsDown) {
			if (currentButton == button) {
				return true;
			}
		}
		return false;
	}

	public boolean mouseButtonPressed(int button) {
		for (int currentButton : mouseButtonsPressed) {
			if (currentButton == button) {
				return true;
			}
		}
		return false;
	}

	public boolean mouseButtonUp(int button) {
		return false;
	}

	public Vector2f mousePosition() {
		Vector2f position = new Vector2f(0f, 0f);
		DoubleBuffer xbuffer = BufferUtils.createDoubleBuffer(4);
		DoubleBuffer ybuffer = BufferUtils.createDoubleBuffer(4);
		glfwGetCursorPos(window.window, xbuffer, ybuffer);
		return new Vector2f((float)xbuffer.get(), (float)ybuffer.get());
	}

	GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
		public void invoke(long windowID, int button, int action, int mods) {
			if (action == GLFW_PRESS) {
				mouseButtonsDown.add(button);
			} else if (action == GLFW_RELEASE) {
				if (mouseButtonsPressed.size() <= 0)
					return;

				int buttonIndex = -1;

				for (int i = 0; i < mouseButtonsPressed.size(); i++) {
					if (mouseButtonsPressed.get(i) == button) {
						buttonIndex = i;
						break;
					}
				}

				mouseButtonsPressed.remove(buttonIndex);
			}
		}
	};

	public boolean getKeyDown(int key) {
		for (int currentKey : keysDown) {
			if (currentKey == key) {
				return true;
			}
		}
		return false;
	}

	public boolean getKeyPressed(int key) {
		for (int currentKey : keysPressed) {
			if (currentKey == key) {
				return true;
			}
		}
		return false;
	}

	public int getKeyPressedInt(int key) {
		for (int currentKey : keysPressed) {
			if (currentKey == key) {
				return 1;
			}
		}
		return 0;
	}

	GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		public void invoke(long windowID, int key, int scancode, int action, int mods) {
			if (action == GLFW_PRESS) {
				keysDown.add(key);
			} else if (action == GLFW_RELEASE) {
				if (keysPressed.size() <= 0)
					return;

				int keyIndex = 0;

				for (int i = 0; i < keysPressed.size(); i++) {
					if (keysPressed.get(i) == key) {
						keyIndex = i;
						break;
					}
				}

				keysPressed.remove(keyIndex);
			}
		}
	};

	public void update() {
		for (int key : keysDown) {
			keysPressed.add(key);
		}

		for (int button : mouseButtonsDown) {
			mouseButtonsPressed.add(button);
		}

		keysDown.clear();
		mouseButtonsDown.clear();
	}
}
