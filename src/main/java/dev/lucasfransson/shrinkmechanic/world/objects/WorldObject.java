package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
		super.setPosition(new Vector2(position.getX() + positionOffset.getX(),
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

	protected void applyPositionRandomization(Random rnd, double posRange) {
		positionOffset = new Vector2((rnd.nextDouble() * 2 - 1) * posRange,
				(rnd.nextDouble() * 2 - 1) * posRange);
	}

	protected void applySizeRandomization(Random rnd, double scaleMin,
			double scaleMax) {
		sizeScale = scaleMin + rnd.nextDouble() * (scaleMax - scaleMin);
		setSize(new Vector2(getSize().getX() * sizeScale,
				getSize().getY() * sizeScale));
	}

	protected void applyColorOffsetRandomization(Random rnd,
			double colorRange) {
		Color base = getTint() != null ? getTint() : Color.WHITE;
		double r = Math.clamp(
				base.getRed() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		double g = Math.clamp(
				base.getGreen() + (rnd.nextDouble() * 2 - 1) * colorRange, 0,
				1);
		double b = Math.clamp(
				base.getBlue() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		this.setTint(new Color(r, g, b, 1.0));
	}

	protected void applyTintRandomization(Random rnd, Color... colors) {
		this.setTint(colors[rnd.nextInt(colors.length)]);
	}

}
