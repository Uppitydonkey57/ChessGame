package entities;

import engine.*;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.*;

public class Player extends Entity {

	Camera camera;
	
	public void start() {

		transform.order = -1000;
		
		camera = new Camera();
		manager.rend.SetCamera(camera);
		camera.position = transform.position;
	}

	public void update() {
		
		if (input.mouseButtonDown(0)) {	
			Block block = new Block();
			block.transform.position = manager.rend.getWorldCoordinates(input.mousePosition());
			System.out.println(manager.rend.getWorldCoordinates(input.mousePosition()));
			System.out.println(transform.position);
			manager.addEntity(block);
			
		}
		manager.rend.SetCamera(camera);
		if (input.getKeyPressed(GLFW_KEY_UP)) {
			transform.position.y += 0.1;
		}

		if (input.getKeyPressed(GLFW_KEY_DOWN)) {
			transform.position.y -= 0.1;
		}

		if (input.getKeyPressed(GLFW_KEY_LEFT)) {
			transform.position.x -= 0.1;
		}

		if (input.getKeyPressed(GLFW_KEY_RIGHT)) {
			transform.position.x += 0.1;
		}
	}

}
