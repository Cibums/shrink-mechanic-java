package dev.lucasfransson.shrinkmechanic.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.ICollidable;
import dev.lucasfransson.shrinkmechanic.engine.ICollisionAware;
import dev.lucasfransson.shrinkmechanic.engine.IManaged;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public abstract class Entity extends GameObject
		implements
			IRenderable,
			ICollisionAware,
			IManaged {

	private static final double SEPARATION_FORCE = 0.001;

	private final List<Sprite> sprites = new ArrayList<>();
	private CollisionSystem collisionSystem;
	private Runnable destroyCallback;
	private Vector2 velocity = Vector2.zero();
	private double drag = 0.0;
	private boolean flipSpriteOnMove = true;

	protected Entity(Sprite sprite) {
		sprites.add(sprite);
		this.getMainSprite().setRenderingLayer(2);
		this.setHasCollision(true);
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void addVelocity(Vector2 velocity) {
		this.velocity = this.velocity.add(velocity);
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}

	protected void applyPhysics(double deltaTime) {
		for (GameObject other : getNearbyCollidables(1.5)) {
			if (!(other instanceof Entity) || other == this)
				continue;
			if (!overlapsAABB(other))
				continue;
			Vector2 dir = getPosition().subtract(other.getPosition());
			double len = Math.sqrt(dir.x() * dir.x() + dir.y() * dir.y());
			if (len > 0.001) {
				velocity = velocity
						.add(new Vector2(dir.x() / len * SEPARATION_FORCE,
								dir.y() / len * SEPARATION_FORCE));
			} else {
				velocity = velocity.add(new Vector2(SEPARATION_FORCE, 0));
			}
		}

		if (velocity.x() == 0 && velocity.y() == 0)
			return;
		moveWithCollision(velocity.x(), velocity.y(), deltaTime);
		double scale = Math.max(0, 1.0 - drag * deltaTime);
		velocity = new Vector2(velocity.x() * scale, velocity.y() * scale);
		if (Math.abs(velocity.x()) < 0.01 && Math.abs(velocity.y()) < 0.01)
			velocity = Vector2.zero();
	}

	protected Sprite getMainSprite() {
		return sprites.getFirst();
	}

	protected void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	@Override
	public List<Sprite> getSprites() {
		return Collections.unmodifiableList(sprites);
	}

	protected void moveWithCollision(double dx, double dy, double deltaTime) {
		if (dx != 0 && flipSpriteOnMove) {
			getMainSprite().setFlipX(dx < 0);
		}

		moveHorizontally(dx * deltaTime);
		if (isCollidingWithAny()) {
			moveHorizontally(-dx * deltaTime);
		}

		moveVertically(dy * deltaTime);
		if (isCollidingWithAny()) {
			moveVertically(-dy * deltaTime);
		}

		for (GameObject other : getNearbyCollidables(2.0)) {
			if (other != this && other instanceof ICollidable c
					&& overlapsAABB(other)) {
				c.onCollidedWith(this);
			}
		}
	}

	protected boolean shouldCollideWith(GameObject other) {
		return true;
	}

	private boolean isCollidingWithAny() {
		if (collisionSystem == null) {
			return false;
		}
		for (GameObject other : collisionSystem
				.getNearbyCollidables(this.getPosition(), 2.0)) {
			if (other == this)
				continue;
			if (!shouldCollideWith(other))
				continue;
			if (overlapsAABB(other))
				return true;
		}
		return false;
	}

	protected List<GameObject> getNearbyCollidables(double range) {
		if (collisionSystem == null)
			return List.of();
		return collisionSystem.getNearbyCollidables(getPosition(), range);
	}

	protected boolean overlapsAABB(GameObject other) {
		double aHalfW = this.getSize().x() / 2.0;
		double aHalfH = this.getSize().y() / 2.0;
		double bHalfW = other.getSize().x() / 2.0;
		double bHalfH = other.getSize().y() / 2.0;

		Vector2 aPos = this.getPosition();
		Vector2 bPos = other.getPosition();

		return Math.abs(aPos.x() - bPos.x()) < (aHalfW + bHalfW)
				&& Math.abs(aPos.y() - bPos.y()) < (aHalfH + bHalfH);
	}

	public void setDestroyCallback(Runnable destroyCallback) {
		this.destroyCallback = destroyCallback;
	}

	protected void destroy() {
		if (destroyCallback != null)
			destroyCallback.run();
	}

	@Override
	public void setCollisionSystem(CollisionSystem collisionSystem) {
		this.collisionSystem = collisionSystem;
	}

	public void setFlipSpriteOnMove(boolean state) {
		this.flipSpriteOnMove = state;
	}
}