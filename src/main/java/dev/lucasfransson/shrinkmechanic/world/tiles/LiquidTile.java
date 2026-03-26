package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import javafx.scene.image.Image;

public abstract class LiquidTile extends Tile {

	protected LiquidTile(Image texture) {
		super(texture == null
				? Renderable.getTextureFromPath("/liquid_default.png")
				: texture);
		initLiquidTile();
	}

	protected LiquidTile(Animation animation) {
		super(animation);
		initLiquidTile();
	}

	private void initLiquidTile() {
		this.setSpriteYOffset(-2);
		this.setHasCollision(true);
		this.setCanBePlacedOn(false);
	}
}
