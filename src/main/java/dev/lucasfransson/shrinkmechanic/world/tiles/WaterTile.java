package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;

public class WaterTile extends LiquidTile {

	public static final Animation water = new Animation("/water", 5.0);

	public WaterTile() {
		super(water);
	}

	@Override
	public void onDestroy() {

	}

}
