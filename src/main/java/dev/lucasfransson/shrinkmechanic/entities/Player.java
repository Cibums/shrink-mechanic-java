package dev.lucasfransson.shrinkmechanic.entities;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
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
		this.setSize(new Vector2(32, 32));
		this.setRenderingLayer(1);
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
	}

	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}
