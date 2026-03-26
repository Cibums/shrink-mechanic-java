package dev.lucasfransson.shrinkmechanic.engine.rendering;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;

public class SpriteEntry {

	private final Sprite sprite;
	private final Vector2 worldPosition;

	public SpriteEntry(Sprite sprite, Vector2 ownerPosition) {
		this.sprite = sprite;
		this.worldPosition = new Vector2(
				ownerPosition.getX() + sprite.getOffset().getX(),
				ownerPosition.getY() + sprite.getOffset().getY());
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Vector2 getWorldPosition() {
		return worldPosition;
	}

	public double getRenderingZOffset() {
		return sprite.getRenderingLayer() - worldPosition.getY();
	}
}