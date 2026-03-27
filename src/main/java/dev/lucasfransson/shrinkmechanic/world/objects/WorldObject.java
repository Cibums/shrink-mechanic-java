package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.DestroyableGameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.items.ItemDrop;

public abstract class WorldObject extends DestroyableGameObject
		implements
			IRenderable {

	private final List<Sprite> sprites = new ArrayList<>();

	protected WorldObject(Sprite sprite) {
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

	public List<ItemDrop> getDrops() {
		return List.of();
	}

	protected void applyPositionRandomization(Random rnd, double posRange) {
		Vector2 offset = new Vector2((rnd.nextDouble() * 2 - 1) * posRange,
				(rnd.nextDouble() * 2 - 1) * posRange);
		getMainSprite().setOffset(offset);
	}

	protected void applySizeRandomization(Random rnd, double scaleMin,
			double scaleMax) {
		double scale = scaleMin + rnd.nextDouble() * (scaleMax - scaleMin);
		getMainSprite().setScale(scale);
		setSize(new Vector2(getSize().x() * scale, getSize().y() * scale));
	}
}