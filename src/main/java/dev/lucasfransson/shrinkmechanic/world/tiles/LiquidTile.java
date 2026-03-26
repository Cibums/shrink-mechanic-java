package dev.lucasfransson.shrinkmechanic.world.tiles;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public abstract class LiquidTile extends Tile {

	protected LiquidTile(Sprite sprite) {
		super(sprite);
		getMainSprite().setSpriteYOffset(-2);
		this.setHasCollision(true);
		this.setCanBePlacedOn(false);
	}
}