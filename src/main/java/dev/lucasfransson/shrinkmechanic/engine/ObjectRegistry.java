package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;
import java.util.function.BiConsumer;

import dev.lucasfransson.shrinkmechanic.items.ItemDrop;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public class ObjectRegistry {

	private final List<IGameSystem> systems;
	private BiConsumer<List<ItemDrop>, Vector2> dropHandler;

	public ObjectRegistry(IGameSystem... systems) {
		this.systems = List.of(systems);
	}

	public void setDropHandler(
			BiConsumer<List<ItemDrop>, Vector2> dropHandler) {
		this.dropHandler = dropHandler;
	}

	public <T> T instantiate(T object) {
		for (IGameSystem system : systems) {
			system.tryRegister(object);
		}

		if (object instanceof IDestroyable m) {
			m.setDestroyCallback(() -> {
				if (dropHandler != null
						&& object instanceof DestroyableGameObject dgo) {
					List<ItemDrop> drops = dgo.getDrops();
					if (!drops.isEmpty())
						dropHandler.accept(drops, dgo.getPosition());
				}
				destroy(object);
			});
		}

		return object;
	}

	public <T> void destroy(T object) {
		for (IGameSystem system : systems) {
			system.tryUnregister(object);
		}
	}
}