package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import javafx.scene.image.Image;

public abstract class Entity extends Renderable implements ITickable {

	private CollisionSystem collisionSystem;

	protected Entity(Image texture) {
		super(texture);
	}

	protected void moveWithCollision(double dx, double dy, double deltaTime) {

		if (dx != 0) {
			this.setFlipX(dx < 0);
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

	public void setCollisionSystem(CollisionSystem collisionSystem) {
		this.collisionSystem = collisionSystem;
	}
}