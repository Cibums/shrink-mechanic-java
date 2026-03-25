package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;
import javafx.scene.image.Image;

public abstract class WorldObject extends Renderable implements IDestroyable {

	private Vector2 positionOffset = Vector2.zero();
	private double sizeScale = 1.0;

	protected WorldObject(Image texture) {
		super(texture == null
				? Renderable.getTextureFromPath("/default.png")
				: texture);

		this.setSize(new Vector2(0.3125, 0.3125));
		this.setRenderingLayer(1);
		this.setHasCollision(true);
	}

	@Override
	public void setPosition(Vector2Int position) {
		super.setPosition(new Vector2(
				position.getX() + positionOffset.getX(),
				position.getY() + positionOffset.getY()));
	}

	@Override
	public Vector2 getSpriteSize() {
		Vector2 base = super.getSpriteSize();
		return new Vector2(base.getX() * sizeScale, base.getY() * sizeScale);
	}

	@Override
	public double getSpriteYOffset() {
		return super.getSpriteYOffset() * sizeScale;
	}

	protected void applyRandomization(Random rnd, double posRange,
			double scaleMin, double scaleMax) {
		positionOffset = new Vector2(
				(rnd.nextDouble() * 2 - 1) * posRange,
				(rnd.nextDouble() * 2 - 1) * posRange);
		sizeScale = scaleMin + rnd.nextDouble() * (scaleMax - scaleMin);
		setSize(new Vector2(
				getSize().getX() * sizeScale,
				getSize().getY() * sizeScale));
	}
}
