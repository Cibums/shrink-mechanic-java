package dev.lucasfransson.shrinkmechanic.world.tiles;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class GrassTile extends Tile {

	public GrassTile() {
		super(new Sprite(Sprite.getTextureFromPath("/grass.png")));
	}

	@Override
	public void onDestroy() {
	}
}