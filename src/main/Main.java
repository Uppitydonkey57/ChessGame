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
			System.out.println("Enter your name");
			Scanner scanner = new Scanner(System.in);
			String name = scanner.nextLine();
			client.write("USER:" + name);
			client.read();
			String teamMessage = client.read();
			if (teamMessage.compareTo("TEAM:WHITE") == 0) {
				System.out.println(teamMessage + "?");
				isWhite = true;
			} else if (teamMessage.compareTo("TEAM:BLACK") == 0) {
				isWhite = false;
			}
			boolean processingAction;
			client.start();
		}
		
		//starting res: 1280 980
		Window window = new Window(1860, 1860, "CHESS BY NATE AND ZACH!");

		Renderer rend = new Renderer(window, new Vector2f(4.1f, 4.1f));

		Input input = new Input(window);
		
		CommandParser parser = new CommandParser(client);
		
		EntityManager entityManager = new EntityManager(rend, input);
		entityManager.client = client;
		entityManager.parser = parser;

		
		Camera camera = new Camera();
		camera.position = new Vector2f(3.5f,3.5f);
		rend.SetCamera(camera);
		
		new BoardGenerator().generateBoard(entityManager, isWhite);
		
		Chat chat = new Chat(client);
		chat.start();

		while (!window.shouldClose()) {
			rend.clearScreen(new Vector4f(0.07f, 0.01f, 0.0f, 0.0f));
			entityManager.update();
			entityManager.render();
			input.update();
			window.update();
			parser.Parse();

		}
		
		System.out.println("closing");
		chat.close();
		if (client != null) {
			client.write("BYE");
			client.end();
		}
		window.close();
		
	}
}
