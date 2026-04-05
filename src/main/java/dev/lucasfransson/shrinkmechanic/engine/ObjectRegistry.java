package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;
import java.util.function.Consumer;

public class ObjectRegistry {

	private final List<IGameSystem> systems;
	private Consumer<Object> postInstantiateHook;

	public ObjectRegistry(IGameSystem... systems) {
		this.systems = List.of(systems);
	}

	public <T> T instantiate(T object) {
		for (IGameSystem system : systems) {
			system.tryRegister(object);
		}

		if (object instanceof GameObject g)
			g.setSpawner(this::instantiate);

		if (object instanceof IManaged m)
			m.setDestroyCallback(() -> destroy(object));

		if (postInstantiateHook != null)
			postInstantiateHook.accept(object);

		return object;
	}

	public void setPostInstantiateHook(Consumer<Object> hook) {
		this.postInstantiateHook = hook;
	}

	public <T> void destroy(T object) {
		for (IGameSystem system : systems) {
			system.tryUnregister(object);
		}
	}
}