package main;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import multiplayer.*;
import engine.*;
import entities.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {

	public static void main(String args[]) throws UnknownHostException, IOException {
		boolean useServer = false;
		
		Client client = null;
		
		boolean isWhite = true;
		
		if (useServer) {
			client = new Client(false);
			//client.start();
			
			client.read();
			System.out.println("Enter your name");
			Scanner scanner = new Scanner(System.in);
			String name = scanner.nextLine();
			client.write(name);
			String teamMessage = client.read();
			if (teamMessage.compareTo("white") == 0) {
				System.out.println(teamMessage + "?");
				isWhite = true;
			} else if (teamMessage.compareTo("black") == 0) {
				isWhite = false;
			}
			boolean processingAction;
		}
		//starting res: 1280 980
		Window window = new Window(1860, 1860, "CHESS BY NATE AND ZACH!");

		Renderer rend = new Renderer(window, new Vector2f(4.1f, 4.1f));

		Input input = new Input(window);
		
		EntityManager entityManager = new EntityManager(rend, input);
		entityManager.client = client;

		
		Camera camera = new Camera();
		camera.position = new Vector2f(3.5f,3.5f);
		rend.SetCamera(camera);
		
		new BoardGenerator().generateBoard(entityManager, isWhite);

		while (!window.shouldClose()) {
			rend.clearScreen(new Vector4f(0.07f, 0.01f, 0.0f, 0.0f));
			entityManager.update();
			entityManager.render();
			input.update();
			window.update();

		}

		client.write("/quit");
		window.close();
		
	}
}
