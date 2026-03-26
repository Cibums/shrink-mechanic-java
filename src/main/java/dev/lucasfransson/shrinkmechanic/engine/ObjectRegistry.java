package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;

public class ObjectRegistry {

	private final List<IGameSystem> systems;

	public ObjectRegistry(IGameSystem... systems) {
		this.systems = List.of(systems);
	}

	public <T> T instantiate(T object) {
		for (IGameSystem system : systems) {
			system.tryRegister(object);
		}
		return object;
	}

	public <T> void destroy(T object) {
		for (IGameSystem system : systems) {
			system.tryUnregister(object);
		}
	}

	public void addGameSystem(IGameSystem system) {
		systems.add(system);
	}
}