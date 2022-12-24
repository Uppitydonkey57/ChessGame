package entities;

import org.joml.*;

import engine.Entity;
import engine.Texture;

public class Tile extends Entity {
	public boolean isBlack;
	
	public void start() {
		texture = new Texture("src/resources/textures/BoardTile.png");
		color = new Vector4f(0.7f, 0.5f, 0.43f, 1.0f);
	}
	
	public void update() {
		
	}
	
	public void swap() {
		color = new Vector4f(0.3f, 0.1f, 0.02f, 1.0f);
		
	}
}
