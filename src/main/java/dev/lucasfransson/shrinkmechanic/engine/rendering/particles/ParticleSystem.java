package dev.lucasfransson.shrinkmechanic.engine.rendering.particles;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.IManaged;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public class ParticleSystem implements IRenderable, ITickable, IManaged {

	private Vector2 position;
	private Sprite sprite;
	private Runnable destroyCallback;

	public ParticleSystem(Vector2 position, Animation animation) {
		this.position = position;
		this.sprite = new Sprite(animation);
		this.sprite.setRenderingLayer(5);
		this.sprite.setSynced(false);
	}

	@Override
	public List<Sprite> getSprites() {
		return List.of(sprite);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	private double t = 0;
	@Override
	public void update(double deltaTime) {
		t += deltaTime;

		if (t >= sprite.getCurrentAnimation().getPlayTime()) {
			destroy();
		}
	}

	@Override
	public void destroy() {
		if (destroyCallback != null)
			destroyCallback.run();
	}

	@Override
	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

}
