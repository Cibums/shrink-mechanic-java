package dev.lucasfransson.shrinkmechanic.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.IGameSystem;

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
}
