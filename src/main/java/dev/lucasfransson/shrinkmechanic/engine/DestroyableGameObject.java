package dev.lucasfransson.shrinkmechanic.engine;

import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public abstract class DestroyableGameObject extends GameObject
		implements
			IManaged,
			IDestroyable {

	private Runnable destroyCallback;
	private boolean destroyed = false;

	@Override
	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

	public void destroy() {
		destroyed = true;

		onDestroy();

		if (destroyCallback != null)
			destroyCallback.run();
	}

	protected boolean isDestroyed() {
		return destroyed;
	}
}
