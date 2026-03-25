package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import javafx.scene.input.KeyCode;

public class Player extends Entity {

	private final InputManager input;
	private double walkSpeed = 3;

	public Player(InputManager input) {
		super(Renderable.getTextureFromPath("/player.png"));
		this.input = input;
		this.setSize(new Vector2(0.2, 0.2));
		this.setRenderingLayer(1);
		this.setHasCollision(true);
		this.setSpriteAlignment(SpriteAlignment.TOP);
	}

	@Override
	public void update(double deltaTime) {
		double dx = 0, dy = 0;

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