package dev.lucasfransson.shrinkmechanic.entities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.IGameSystem;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;

public class EntitySystem implements IGameSystem {

	private final Set<Entity> entities = new LinkedHashSet<>();

	@Override
	public void tryRegister(Object object) {
		if (object instanceof Entity e)
			register(e);
	}

	private void register(Entity e) {
		entities.add(e);
	}

	@Override
	public void tryUnregister(Object object) {
		if (object instanceof Entity e)
			unregister(e);
	}

	private void unregister(Entity e) {
		entities.remove(e);
	}

	public List<Entity> getEntitiesInRange(Vector2 center, double range) {
		List<Entity> result = new ArrayList<>();
		for (Entity e : entities) {
			Vector2 pos = e.getPosition();
			if (Math.abs(pos.x() - center.x()) <= range
					&& Math.abs(pos.y() - center.y()) <= range) {
				result.add(e);
			}
		}
		return result;
	}
}
