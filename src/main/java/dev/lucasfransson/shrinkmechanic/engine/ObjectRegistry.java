package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public class ObjectRegistry {

	private final List<IGameSystem> systems;

	public ObjectRegistry(IGameSystem... systems) {
		this.systems = List.of(systems);
	}

	public <T> T instantiate(T object) {
		for (IGameSystem system : systems) {
			system.tryRegister(object);
		}

		if (object instanceof GameObject g)
			g.setSpawner(this::instantiate);

		if (object instanceof IDestroyable m)
			m.setDestroyCallback(() -> destroy(object));

		return object;
	}

	public <T> void destroy(T object) {
		for (IGameSystem system : systems) {
			system.tryUnregister(object);
		}
	}
}