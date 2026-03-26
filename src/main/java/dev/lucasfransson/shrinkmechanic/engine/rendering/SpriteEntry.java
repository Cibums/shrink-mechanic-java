package dev.lucasfransson.shrinkmechanic.engine.rendering;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;

public class SpriteEntry {

	private final Sprite sprite;
	private final Vector2 worldPosition;

	public SpriteEntry(Sprite sprite, Vector2 ownerPosition) {
		this.sprite = sprite;
		this.worldPosition = new Vector2(
				ownerPosition.x() + sprite.getOffset().x(),
				ownerPosition.y() + sprite.getOffset().y());
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Vector2 getWorldPosition() {
		return worldPosition;
	}

	public double getRenderingZOffset() {
		return sprite.getRenderingLayer() - worldPosition.y();
	}
}