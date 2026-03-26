package dev.lucasfransson.shrinkmechanic.world.tiles;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class WaterTile extends LiquidTile {

	private static final Animation WATER_ANIMATION = new Animation("/water",
			2.0);

	public WaterTile() {
		super(new Sprite(WATER_ANIMATION));
	}

	@Override
	public void onDestroy() {
	}
}