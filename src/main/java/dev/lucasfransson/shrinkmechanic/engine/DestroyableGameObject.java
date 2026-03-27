package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.items.ItemDrop;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public abstract class DestroyableGameObject extends GameObject
		implements
			IDestroyable {

	private Runnable destroyCallback;
	private boolean destroyed = false;

	@Override
	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

	public void destroy() {
		destroyed = true;
		if (destroyCallback != null)
			destroyCallback.run();
	}

	protected boolean isDestroyed() {
		return destroyed;
	}

	public List<ItemDrop> getDrops() {
		return List.of();
	}
}
