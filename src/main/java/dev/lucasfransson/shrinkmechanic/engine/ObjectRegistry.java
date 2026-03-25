package dev.lucasfransson.shrinkmechanic.engine;
import java.util.ArrayList;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;

public class ObjectRegistry {
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;
	private final ArrayList<GameObject> allGameObjects;

	private GameWorld world;

	public ObjectRegistry(TickSystem tickSystem, RenderSystem renderSystem) {
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.allGameObjects = new ArrayList<>();
	}

	public <T extends GameObject> T instantiate(T object) {

		if (world != null) {
			object.setWorld(world);
		}

		if (object instanceof Renderable r)
			renderSystem.register(r);
		if (object instanceof ITickable t)
			tickSystem.register(t);

		allGameObjects.add(object);
		return object;
	}

	public <T extends GameObject> void destroy(T object) {
		if (object instanceof Renderable r)
			renderSystem.unregister(r);
		if (object instanceof ITickable t)
			tickSystem.unregister(t);

		allGameObjects.remove(object);
	}

	public List<GameObject> getNearbyCollidables(Vector2 position,
			double range) {
		return allGameObjects.stream().filter(GameObject::hasCollision)
				.filter(obj -> obj.getPosition().distance(position) <= range)
				.toList();
	}

	public void setWorld(GameWorld world) {
		System.out.println("Setting world to " + world);
		this.world = world;
	}
}