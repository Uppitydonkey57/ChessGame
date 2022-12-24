package engine;

import java.util.ArrayList;
import multiplayer.*;

public class EntityManager {
	public ArrayList<Entity> entities = new ArrayList<Entity>();

	public Renderer rend;
	Input input;
	public Client client;
	public CommandParser parser;

	public EntityManager(Renderer rend, Input input) {
		this.rend = rend;
		this.input = input;
	}

	public void addEntity(Entity entity) {
		entity.manager = this;
		entity.input = input;
		entities.add(entity);
		entity.start();
	}

	public void removeEntity(Entity entity) {
		int entityIndex = -1;

		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == entity) {
				entityIndex = i;
				break;
			}
		}

		if (entityIndex == -1)
			return;

		entities.remove(entityIndex);
	}
	
	public Entity findObjectWithTag(String tag) {
		for (Entity entity : entities) {
			if (entity.tag == tag) {
				return entity;
			}
		}
		
		return null;
	}
	
	public ArrayList<Entity> findObjectsWithTag(String tag) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		
		for (Entity entity : entities) {
			if (entity.tag == tag) {
				list.add(entity);
			}
		}
		
		return list;
	}

	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	public void render() {
		for (Entity entity : entities) {
			RenderData entityRenderData = entity.getRenderData();
			if (entityRenderData.spriteSheet == null) {
				rend.render(entityRenderData.shader, entityRenderData.texture, entityRenderData.transform,
						    entityRenderData.color);
			} else {
				rend.render(entityRenderData.shader, entityRenderData.texture, entityRenderData.transform,
					        entityRenderData.color, entityRenderData.spriteSheet.spriteWidth, 
					        entityRenderData.spriteSheet.spriteHeight, entityRenderData.spriteSheet.coords);
			}
		}
	}
}
