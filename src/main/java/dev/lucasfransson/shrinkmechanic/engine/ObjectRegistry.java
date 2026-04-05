package dev.lucasfransson.shrinkmechanic.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObjectRegistry {

	private final List<IGameSystem> systems;
	private final List<Consumer<Object>> postInstantiateHooks = new ArrayList<>();

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

		for (Consumer<Object> hook : postInstantiateHooks) {
			hook.accept(object);
		}

		return object;
	}

	public void addPostInstantiateHook(Consumer<Object> hook) {
		this.postInstantiateHooks.add(hook);
	}

	public <T> void destroy(T object) {
		for (IGameSystem system : systems) {
			system.tryUnregister(object);
		}
	}
}