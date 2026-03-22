package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;

public class GrassTile extends Tile {

	public GrassTile() {
		super(Renderable.getTextureFromPath("/grass.png"));
	}

	@Override
	public void onDestroy() {

	}

}
