package dev.lucasfransson.shrinkmechanic.engine.rendering;

import dev.lucasfransson.shrinkmechanic.engine.IPositioned;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import javafx.scene.input.KeyCode;

public class Camera implements ITickable, IPositioned {

	private IPositioned target;
	private Vector2 position;
	private double lerpSpeed = 8.0;
	private double zoom = 1.5;

	private final InputManager input;

	public Camera(IPositioned target, InputManager input) {
		this.target = target;
		this.position = target.getPosition();
		this.input = input;
	}

	@Override
	public void update(double deltaTime) {
		Vector2 current = position;
		Vector2 targetPos = target.getPosition();

		double newX = current.x()
				+ (targetPos.x() - current.x()) * lerpSpeed * deltaTime;
		double newY = current.y()
				+ (targetPos.y() - current.y()) * lerpSpeed * deltaTime;

		position = new Vector2(newX, newY);

		double scroll = input.consumeScrollDelta();
		if (scroll != 0 && input.isKeyHeld(KeyCode.ALT)) {
			zoom += scroll * 0.001;
			zoom = Math.clamp(zoom, 0.25, 5.0);
		}
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = Math.clamp(zoom, 0.25, 5.0);
	}
}