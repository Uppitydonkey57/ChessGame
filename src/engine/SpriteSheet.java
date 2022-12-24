package engine;

import org.joml.*;

public class SpriteSheet {
	public int spriteWidth;
	public int spriteHeight;
	public Vector2f coords;
	
	public SpriteSheet(int spriteWidth, int spriteHeight, Vector2f coords) {
		this.coords = coords;
		this.spriteHeight = spriteHeight;
		this.spriteWidth = spriteWidth;
	}
}
