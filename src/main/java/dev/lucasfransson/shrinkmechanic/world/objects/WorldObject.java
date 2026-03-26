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
		super.setPosition(new Vector2(position.x() + positionOffset.x(),
				position.y() + positionOffset.y()));
	}

	protected void applyPositionRandomization(Random rnd, double posRange) {
		positionOffset = new Vector2((rnd.nextDouble() * 2 - 1) * posRange,
				(rnd.nextDouble() * 2 - 1) * posRange);
	}

	protected void applySizeRandomization(Random rnd, double scaleMin,
			double scaleMax) {
		double scale = scaleMin + rnd.nextDouble() * (scaleMax - scaleMin);
		getMainSprite().setScale(scale);
		setSize(new Vector2(getSize().x() * scale, getSize().y() * scale));
	}
}