package dev.lucasfransson.shrinkmechanic.world.tiles;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.world.objects.IRandomizable;

public class GrassTile extends Tile implements IRandomizable {

	public GrassTile() {
		super(new Sprite(Sprite.getTextureFromPath("/grass.png")));
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public void randomize(Random rnd) {
		getMainSprite().randomizeColorOffset(rnd, 0.05);
	}
}