package dev.lucasfransson.shrinkmechanic.engine.rendering;

import dev.lucasfransson.shrinkmechanic.engine.IPositioned;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public class Camera implements ITickable, IPositioned {

	private IPositioned target;
	private Vector2 position;
	private double lerpSpeed = 8.0;

	public Camera(IPositioned target) {
		this.target = target;
		this.position = target.getPosition();
	}

	@Override
	public void update(double deltaTime) {
		Vector2 current = position;
		Vector2 targetPos = target.getPosition();

		double newX = current.getX()
				+ (targetPos.getX() - current.getX()) * lerpSpeed * deltaTime;
		double newY = current.getY()
				+ (targetPos.getY() - current.getY()) * lerpSpeed * deltaTime;

		position = new Vector2(newX, newY);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}
}
