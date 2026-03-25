package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;

public class WaterTile extends LiquidTile {

	public WaterTile() {
		super(Renderable.getTextureFromPath("/water.png"));
	}

	@Override
	public void onDestroy() {

	}

}
