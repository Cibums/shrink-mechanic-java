package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public abstract class Item {

	private final Sprite sprite;

	public Item(Sprite sprite) {
		this.sprite = sprite;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public abstract String getName();
}
