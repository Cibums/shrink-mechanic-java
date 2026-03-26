package dev.lucasfransson.shrinkmechanic.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.ICollisionAware;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public abstract class Entity extends GameObject
		implements
			IRenderable,
			ITickable,
			ICollisionAware {

	private final List<Sprite> sprites = new ArrayList<>();
	private CollisionSystem collisionSystem;

	protected Entity(Sprite sprite) {
		sprites.add(sprite);
		this.getMainSprite().setRenderingLayer(2);
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
		if (dx != 0) {
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
	}

	private boolean isCollidingWithAny() {
		if (collisionSystem == null) {
			return false;
		}
		for (GameObject other : collisionSystem
				.getNearbyCollidables(this.getPosition(), 2.0)) {
			if (other == this)
				continue;
			if (overlapsAABB(other))
				return true;
		}
		return false;
	}

	private boolean overlapsAABB(GameObject other) {
		double aHalfW = this.getSize().getX() / 2.0;
		double aHalfH = this.getSize().getY() / 2.0;
		double bHalfW = other.getSize().getX() / 2.0;
		double bHalfH = other.getSize().getY() / 2.0;

		Vector2 aPos = this.getPosition();
		Vector2 bPos = other.getPosition();

		return Math.abs(aPos.getX() - bPos.getX()) < (aHalfW + bHalfW)
				&& Math.abs(aPos.getY() - bPos.getY()) < (aHalfH + bHalfH);
	}

	@Override
	public void setCollisionSystem(CollisionSystem collisionSystem) {
		this.collisionSystem = collisionSystem;
	}
}