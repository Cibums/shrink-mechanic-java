package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import javafx.scene.input.KeyCode;

public class LocalPlayer extends Player implements ITickable {

	private final InputManager input;
	private double walkSpeed = 3;

	public LocalPlayer(InputManager input) {
		this.input = input;
		this.setSize(new Vector2(0.1, 0.05));
		this.setHasCollision(true);
		getMainSprite().setSpriteAlignment(SpriteAlignment.TOP);
	}

	@Override
	protected boolean shouldCollideWith(GameObject other) {
		return !(other instanceof DroppedItem);
	}

	@Override
	public void update(double deltaTime) {
		double dx = 0;
		double dy = 0;

		if (input.isKeyHeld(KeyCode.W))
			dy += walkSpeed;
		if (input.isKeyHeld(KeyCode.S))
			dy -= walkSpeed;
		if (input.isKeyHeld(KeyCode.A))
			dx -= walkSpeed;
		if (input.isKeyHeld(KeyCode.D))
			dx += walkSpeed;

		moveWithCollision(dx, dy, deltaTime);
	}

	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}