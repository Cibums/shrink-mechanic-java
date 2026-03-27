package dev.lucasfransson.shrinkmechanic.engine;

import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public abstract class DestroyableGameObject extends GameObject
		implements
			IDestroyable {

	private Runnable destroyCallback;

	@Override
	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

	public void destroy() {
		if (destroyCallback != null)
			destroyCallback.run();
	}
}
