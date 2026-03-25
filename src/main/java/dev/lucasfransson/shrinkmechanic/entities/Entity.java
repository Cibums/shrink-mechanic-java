package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import javafx.scene.image.Image;

public abstract class Entity extends Renderable implements ITickable {

	public Entity(Image texture) {
		super(texture);
		this.setHasCollision(true);
	}

	public void move(double x, double y) {
		move(new Vector2(x, y));
	}

	public void moveHorizontally(double speed) {
		move(speed, 0);
	}

	public void moveVertically(double speed) {
		move(0, speed);
	}

	public void move(Vector2 velocity) {

		GameWorld world = getWorld();

		if (world == null) {
			System.out.println("World is null");
			return;
		}

		// --- Move X ---
		Vector2 newPosX = new Vector2(getPosition().getX() + velocity.getX(),
				getPosition().getY());

		if (!collides(newPosX)) {
			setPosition(newPosX);
		}

		// --- Move Y ---
		Vector2 newPosY = new Vector2(getPosition().getX(),
				getPosition().getY() + velocity.getY());

		if (!collides(newPosY)) {
			setPosition(newPosY);
		}
	}

	private boolean collides(Vector2 newPos) {

		GameWorld world = getWorld();

		double range = Math.max(getSize().getX(), getSize().getY())
				/ GameWorld.gridElementSize + 1;

		for (GameObject obj : world.getRegistry().getNearbyCollidables(newPos,
				range)) {

			if (obj == this)
				continue;

			if (intersects(newPos, this.getSize(), obj)) {
				return true;
			}
		}

		return false;
	}

	private boolean intersects(Vector2 pos, Vector2 size, GameObject other) {
		return pos.getX() < other.getPosition().getX() + other.getSize().getX()
				&& pos.getX() + size.getX() > other.getPosition().getX()
				&& pos.getY() < other.getPosition().getY()
						+ other.getSize().getY()
				&& pos.getY() + size.getY() > other.getPosition().getY();
	}
}
