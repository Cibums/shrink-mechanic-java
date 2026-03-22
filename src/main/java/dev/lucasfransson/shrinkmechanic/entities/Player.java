package dev.lucasfransson.shrinkmechanic.entities;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import javafx.scene.input.KeyCode;

public class Player extends Renderable implements ITickable {

	private InputManager input;
	private double walkSpeed = 3;

	public Player(InputManager input) {
		super(Renderable.getTextureFromPath("/player.png"));
		this.input = input;
		this.setRenderingLayer(100);
	}

	@Override
	public void update(double deltaTime) {

		if (input.isKeyHeld(KeyCode.W)) {
			this.moveVertically(walkSpeed * deltaTime);
		}

		if (input.isKeyHeld(KeyCode.S)) {
			this.moveVertically(-walkSpeed * deltaTime);
		}

		if (input.isKeyHeld(KeyCode.A)) {
			this.moveHorizontally(-walkSpeed * deltaTime);
		}

		if (input.isKeyHeld(KeyCode.D)) {
			this.moveHorizontally(walkSpeed * deltaTime);
		}

		System.out.println("X: " + this.getPosition().getX() + "; Y: "
				+ this.getPosition().getY());
	}

	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}
