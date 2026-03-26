package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;
import javafx.scene.paint.Color;

public abstract class WorldObject extends GameObject
		implements
			IRenderable,
			IDestroyable {

	private final List<Sprite> sprites = new ArrayList<>();
	private Vector2 positionOffset = Vector2.zero();

	protected WorldObject(Sprite sprite) {
		sprite.setRenderingLayer(1);
		sprites.add(sprite);
		this.getMainSprite().setRenderingLayer(2);
		this.setSize(new Vector2(0.3125, 0.3125));
		this.setHasCollision(true);
	}

	protected Sprite getMainSprite() {
		return sprites.getFirst();
	}

	protected void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	@Override
	public List<Sprite> getSprites() {
		return Collections.unmodifiableList(sprites);
	}

	@Override
	public void setPosition(Vector2Int position) {
		super.setPosition(new Vector2(position.getX() + positionOffset.getX(),
				position.getY() + positionOffset.getY()));
	}

	protected void applyPositionRandomization(Random rnd, double posRange) {
		positionOffset = new Vector2((rnd.nextDouble() * 2 - 1) * posRange,
				(rnd.nextDouble() * 2 - 1) * posRange);
	}

	protected void applySizeRandomization(Random rnd, double scaleMin,
			double scaleMax) {
		double scale = scaleMin + rnd.nextDouble() * (scaleMax - scaleMin);
		getMainSprite().setScale(scale);
		setSize(new Vector2(getSize().getX() * scale,
				getSize().getY() * scale));
	}

	protected void applyColorOffsetRandomization(Random rnd,
			double colorRange) {
		Sprite s = getMainSprite();
		Color base = s.getTint() != null ? s.getTint() : Color.WHITE;
		double r = Math.clamp(
				base.getRed() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		double g = Math.clamp(
				base.getGreen() + (rnd.nextDouble() * 2 - 1) * colorRange, 0,
				1);
		double b = Math.clamp(
				base.getBlue() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		s.setTint(new Color(r, g, b, 1.0));
	}

	protected void applyTintRandomization(Random rnd, Color... colors) {
		getMainSprite().setTint(colors[rnd.nextInt(colors.length)]);
	}
}