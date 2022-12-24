package entities;

import engine.Entity;
import engine.Texture;

public class Block extends Entity {
	public void start() {
		transform.order = 10;
		texture = new Texture("src/resources/textures/guy2.png");
	}
	
	public void update() {
		
	}
}
