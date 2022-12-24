package engine;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.joml.*;

public abstract class Entity {
	protected Texture texture = new Texture("src/resources/textures/test.png");
	protected Shader shader = new Shader("src/resources/shaders/TextureShader.glsl");
	protected Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	protected Vector2f colliderSize;
	public Transform transform = new Transform(new Vector2f(0.0f, 0.0f), 0f, new Vector2f(1.0f, 1.0f), 0);
	public EntityManager manager;
	public SpriteSheet spriteSheet = null;
	protected String tag = "";

	public Input input;

	public Entity() {
		
	}

	public abstract void start();

	public abstract void update();

	public void instantiate(Entity entity, Vector2f position) {
		entity.transform.position = position;
		manager.addEntity(entity);
	}
	public RenderData getRenderData() {
		RenderData data = new RenderData();
		data.color = color;
		data.shader = shader;
		data.transform = transform;
		data.texture = texture;
		data.spriteSheet = spriteSheet;
		return data;
	}
	
	public boolean isMouseOver() {
		Vector2f collider = getColliderSize();
		Vector2f mousePos = manager.rend.getWorldCoordinates(input.mousePosition());
		Vector2f position = new Vector2f(transform.position).sub(new Vector2f(transform.scale).div(2));
		
		if (mousePos.x >= position.x && mousePos.x <= position.x + collider.x &&
			mousePos.y >= position.y && mousePos.y <= position.y + collider.y) {
			return true;
		}
		
		return false;
	}
	
	boolean checkCollision(Vector2f position, Entity two) // AABB - AABB collision
	{
	    // collision x-axis
	    boolean collisionX = position.x + getColliderSize().x >= two.transform.position.x &&
	        two.transform.position.x + two.getColliderSize().x >= position.x;
	    // collision y-axis?
	    boolean collisionY = position.y + getColliderSize().y >= two.transform.position.y &&
	        two.transform.position.y + two.getColliderSize().y >= position.y;
	    // collision only if on both axes
	    return collisionX && collisionY;
	}  
	
	public boolean isColliding(Vector2f position, String tag) {
		ArrayList<Entity> checkList = (tag == null) ? manager.entities : manager.findObjectsWithTag(tag);
		
		for (Entity entity : checkList) {
			if (checkCollision(position, entity)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Entity> getCollisions(Vector2f position, String tag) {
		ArrayList<Entity> checkList = (tag == null) ? manager.entities : manager.findObjectsWithTag(tag);
		ArrayList<Entity> collisionList = new ArrayList<Entity>();
		
		for (Entity entity : checkList) {
			if (entity != this && checkCollision(position, entity)) {
				collisionList.add(entity);
			}
		}
		
		return collisionList;
	}
	
	
	public Vector2f getColliderSize() {
		return (colliderSize != null) ? colliderSize : transform.scale;
	}

	protected float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}
}
