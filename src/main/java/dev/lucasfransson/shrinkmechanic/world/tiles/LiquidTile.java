package dev.lucasfransson.shrinkmechanic.world.tiles;
import javafx.scene.image.Image;

public abstract class LiquidTile extends Tile {

	public LiquidTile(Image texture) {
		super(texture == null
				? new Image(LiquidTile.class.getResource("/liquid_default.png")
						.toExternalForm())
				: texture);

		this.setSpriteYOffset(-8);
		this.setHasCollision(true);
	}
}
