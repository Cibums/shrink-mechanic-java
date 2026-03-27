package dev.lucasfransson.shrinkmechanic.entities;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.ICollidable;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.items.Item;

public class DroppedItem extends Entity implements ITickable, ICollidable {

	private static final double DRAG = 4.0;
	private static final double MIN_SPEED = 1.5;
	private static final double MAX_SPEED = 3.0;

	public DroppedItem(Item item) {
		super(item.getSprite());
		this.setSize(new Vector2(0.25, 0.25));
		this.getMainSprite().setSpriteAlignment(SpriteAlignment.CENTER);
		this.setDrag(DRAG);
		this.setFlipSpriteOnMove(false);
		Random rnd = new Random();
		double angle = rnd.nextDouble() * 2 * Math.PI;
		double speed = MIN_SPEED + rnd.nextDouble() * (MAX_SPEED - MIN_SPEED);
		setVelocity(
				new Vector2(Math.cos(angle) * speed, Math.sin(angle) * speed));
	}

	@Override
	protected boolean shouldCollideWith(GameObject other) {
		return !(other instanceof DroppedItem);
	}

	@Override
	public void onCollidedWith(GameObject other) {
		if (other instanceof LocalPlayer)
			destroy();
	}

	@Override
	public void update(double deltaTime) {
		applyPhysics(deltaTime);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}
}
